/**
 * 
 */
package com.hitech.dms.web.dao.inv.create;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.invoice.MachineInvoiceDtlEntity;
import com.hitech.dms.web.entity.invoice.MachineInvoiceHDREntity;
import com.hitech.dms.web.entity.invoice.MachineInvoiceItemDtlEntity;
import com.hitech.dms.web.entity.machinepo.MachinePODtlEntity;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.inv.common.response.InvoiceTypeResponseModel;
import com.hitech.dms.web.model.inv.create.request.InvoiceCreateRequestModel;
import com.hitech.dms.web.model.inv.create.request.InvoiceMachDtlCreateRequestModel;
import com.hitech.dms.web.model.inv.create.response.InvoiceCreateResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class InvoiceCreateDaoImpl implements InvoiceCreateDao {
	private static final Logger logger = LoggerFactory.getLogger(InvoiceCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private CommonServiceClient commonServiceClient;

	public DealerDTLResponseModel fetchDealerDTLByDealerId(String authorizationHeader, BigInteger dealerId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDealerDTLByDealerId invoked..");
		}
		DealerDTLResponseModel responseModel = null;
		try {
			DealerDTLRequestModel requestModel = new DealerDTLRequestModel();
			requestModel.setDealerId(dealerId);
			requestModel.setIsFor("SALESINVOICE");
			HeaderResponse headerResponse = commonServiceClient.fetchDealerDTLByDealerId(authorizationHeader,
					requestModel);
			Object object = headerResponse.getResponseData();
			if (object != null) {
				try {
					com.google.gson.Gson gson = new com.google.gson.Gson();
					String jsonString = gson.toJson(object);
					responseModel = gson.fromJson(jsonString, DealerDTLResponseModel.class);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				}
			}
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return responseModel;
	}

	@SuppressWarnings("deprecation")
	public InvoiceCreateResponseModel createInvoice(String authorizationHeader, String userCode,
			InvoiceCreateRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("create createInvoice invoked.." + userCode);
		}
		logger.debug(requestModel.toString());
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		InvoiceCreateResponseModel responseModel = new InvoiceCreateResponseModel();
		MachineInvoiceHDREntity hdrEntity = null;
		String invoiceNumber = null;
		boolean isSuccess = true;
		String sqlQuery = null;
		BigInteger vinId = null;
		Integer invoiceTypeId = 0;
		BigInteger dealerId = null;
		BigInteger coDealerId = null;
		BigInteger branchId = null;
		BigInteger poHdrId = null;
		BigInteger userId = null;
		BigInteger save = BigInteger.ZERO;

		try {
			if (requestModel.getMachineInvoiceDtlList() == null || requestModel.getMachineInvoiceDtlList().isEmpty()) {
				logger.error(this.getClass().getName(), "At-least One Line Machine Is Required");
				responseModel.setMsg("At-least One Line Machine Is Required");
				isSuccess = false;
			}
			if (isSuccess) {

				List<InvoiceMachDtlCreateRequestModel> machineInvoiceDtlList2 = requestModel.getMachineInvoiceDtlList();
				// now we will use only one machine but in future may be it inc.
				for (InvoiceMachDtlCreateRequestModel obj : machineInvoiceDtlList2) {
					vinId = obj.getVinId();
				}
				coDealerId = requestModel.getToDealerId();
				branchId = requestModel.getBranchId();
				dealerId = requestModel.getDealerId();
				hdrEntity = mapper.map(requestModel, MachineInvoiceHDREntity.class, "InvoiceMapId");

				session = sessionFactory.openSession();
				transaction = session.beginTransaction();
				mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					userId = (BigInteger) mapData.get("userId");
					// fetch Dealer Detail
					DealerDTLResponseModel dealerDtl = fetchDealerDTLByDealerId(authorizationHeader,
							requestModel.getDealerId());
					if (dealerDtl != null) {
						Date currDate = new Date();
						SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy");
						String strYear = simpleformat.format(currDate);
						invoiceTypeId = hdrEntity.getInvoiceTypeId();
						InvoiceTypeResponseModel invoiceTypeDetail = commonDao.fetchInvoiceTypeDtl(session, userCode,
								hdrEntity.getInvoiceTypeId());
						if (invoiceTypeDetail != null) {

							mapData = fetchLastInvoiceDTLByBranchId(session, hdrEntity.getBranchId(), strYear);
							String lastInvNumber = null;
							simpleformat = new SimpleDateFormat("yy");
							strYear = simpleformat.format(currDate);
							if (mapData != null && mapData.get("SUCCESS") != null) {
								String invNo = (String) mapData.get("invNumber");
								if (invNo != null && !invNo.equalsIgnoreCase("")) {
									lastInvNumber = (String) mapData.get("invNumber");
									int lastIndexOf = dealerDtl.getDealerCode().length() + 2;// lastInvNumber.lastIndexOf(strYear);
									if (lastIndexOf > 0) {
										String prefix = lastInvNumber.substring(0, lastIndexOf);
										invoiceNumber = lastInvNumber.substring(lastIndexOf + 2,
												lastInvNumber.length());
										Integer i = Integer.valueOf(invoiceNumber);
										invoiceNumber = prefix + strYear + String.format("%04d", i + 1);
									} else {
										invoiceNumber = "IN" + dealerDtl.getDealerCode() + strYear + "0001";
									}
								} else {
									invoiceNumber = "IN" + dealerDtl.getDealerCode() + strYear + "0001";
								}

								List<MachineInvoiceDtlEntity> machineInvoiceDtlList = hdrEntity
										.getMachineInvoiceDtlList();

								List<MachineInvoiceItemDtlEntity> machineInvoiceItemDtlList = hdrEntity
										.getMachineInvoiceItemDtlList();

								poHdrId = hdrEntity.getToPoHdrId();
								if (poHdrId != null && poHdrId.compareTo(BigInteger.ZERO) > 0) {
//									sqlQuery = "Select * from SA_PO_DTL (nolock) where po_hdr_id =:poHdrId and machine_item_id =:machineItemId";
									// calculate count of lineItem For PO Machine Detail
									Map<BigInteger, Long> machineItemWithCount = machineInvoiceDtlList.stream()
											.filter(e -> e.getIsSelected()).map(m -> m.getMachineItemId())
											.collect(Collectors.groupingByConcurrent(Function.identity(),
													Collectors.counting()));
									if (machineItemWithCount == null || machineItemWithCount.isEmpty()) {
										logger.error(this.getClass().getName(), "Machine Count is null/0.");
//										logger.debug(machineInvoiceDtlList.toString());
										responseModel.setMsg("Please Select At-least One Line Machine.");
										isSuccess = false;
									}
									if (isSuccess) {
										for (MachineInvoiceDtlEntity dtlEntity : machineInvoiceDtlList) {
											sqlQuery = "select * from SA_PO_DTL (nolock) SPD where SPD.machine_item_id =:machineItemId and SPD.po_hdr_id =:poHdrId ";
											query = session.createNativeQuery(sqlQuery)
													.addEntity(MachinePODtlEntity.class);
											
											logger.info("poHdrId --" + poHdrId + " machineItemId --" + dtlEntity.getMachineItemId());
											query.setParameter("poHdrId", poHdrId);
											query.setParameter("machineItemId", dtlEntity.getMachineItemId());
											Optional<MachinePODtlEntity> dtlEntity2 = Optional
													.of((MachinePODtlEntity) query.uniqueResult());
											
											if (dtlEntity2.isPresent()) {
												int qty = dtlEntity2.get().getQuantity();
												int invQty = dtlEntity2.get().getInvoiceQty();
												int pendingQty = dtlEntity2.get().getPendingQty();
												int count = machineItemWithCount
														.get(dtlEntity2.get().getMachineItemId()).intValue();
												logger.info("qty --" + qty + " invQty --" + invQty);
												
												logger.info("count --" + count + " pending qty --" + pendingQty );
												if (pendingQty >= 1) {
													invQty = invQty + 1;
													dtlEntity2.get().setInvoiceQty(invQty);

													pendingQty = pendingQty - 1;
													dtlEntity2.get().setPendingQty(pendingQty);

													String status = null;
													if (qty == invQty) {
														status = "FULLY INVOICED";
													} else {
														status = "PARTIALLY INVOICE";
													}
													sqlQuery = "update SA_PO_HDR set po_status =:status, last_modified_by =:modifiedBy, last_modified_date=:modifiedDate where po_hdr_id =:poHdrId";
													query = session.createNativeQuery(sqlQuery);
													query.setParameter("status", status);
													query.setParameter("modifiedBy", userId);
													query.setParameter("modifiedDate", currDate);
													query.setParameter("poHdrId", hdrEntity.getToPoHdrId());
													int k = query.executeUpdate();

												} else {
													logger.error(this.getClass().getName(),
															"Error While Updating Pending and Invoice Qty. : " + " : "
																	+ count);
													responseModel.setMsg(
															"Error While Updating Pending and Invoice Qty. Please Contact Your System Administrator.");
													isSuccess = false;
													break;
												}

												session.merge(dtlEntity2.get());

												dtlEntity.setGrnDoneFlag(false);
												// change to false when start working on service module
												dtlEntity.setPdiDoneFlag(true);
												dtlEntity.setMachineInvoiceHDR(hdrEntity);
												
												String sql = "exec UPDATE_INVENTORY_FOR_IDT :machineInventoryId, :invoiceNo, :invoiceDate";	
												query = session.createSQLQuery(sql);
												query.setParameter("machineInventoryId", dtlEntity.getMachineInventoryId());
												query.setParameter("invoiceNo", invoiceNumber);
												query.setParameter("invoiceDate", requestModel.getInvoiceDate());
												query.executeUpdate();
											}
										}
									}
								} else {
									// for Customer/DC
									for (MachineInvoiceDtlEntity dtlEntity : machineInvoiceDtlList) {

										sqlQuery = "update SA_MACHINE_DC_HDR set dc_status = 'INVOICE DONE', last_modified_by =:modifiedBy, last_modified_date=:modifiedDate where dc_id =:dcId and branch_id =:branchId";
										query = session.createNativeQuery(sqlQuery);
										query.setParameter("modifiedBy", userId);
										query.setParameter("modifiedDate", currDate);
										query.setParameter("dcId", dtlEntity.getDcId());
										query.setParameter("branchId", hdrEntity.getBranchId());
										int k = query.executeUpdate();

										sqlQuery = "update ALLOT set allotment_status = 'INVOICE', last_modified_by =:modifiedBy, last_modified_date=:modifiedDate "
												+ " from SA_MACHINE_ALLOTMENT (nolock) ALLOT "
												+ " INNER JOIN SA_MACHINE_DC_HDR (nolock) DCH ON ALLOT.machine_allotment_id = DCH.machine_allotment_id"
												+ " where DCH.dc_id =:dcId and DCH.branch_id =:branchId";
										query = session.createNativeQuery(sqlQuery);
										query.setParameter("modifiedBy", userId);
										query.setParameter("modifiedDate", currDate);
										query.setParameter("dcId", dtlEntity.getDcId());
										query.setParameter("branchId", hdrEntity.getBranchId());
										k = query.executeUpdate();

										sqlQuery = "update enq set enquiry_status = 'RETAILED', ModifiedBy =:modifiedBy, ModifiedDate=:modifiedDate "
												+ " from SA_MACHINE_ALLOTMENT (nolock) ALLOT "
												+ " INNER JOIN SA_MACHINE_DC_HDR (nolock) DCH ON ALLOT.machine_allotment_id = DCH.machine_allotment_id"
												+ " inner join SA_ENQ_HDR(nolock) enq ON enq.enquiry_id=DCH.enquiry_id "
												+ " where DCH.dc_id =:dcId and DCH.branch_id =:branchId";
										query = session.createNativeQuery(sqlQuery);
										query.setParameter("modifiedBy", userId);
										query.setParameter("modifiedDate", currDate);
										query.setParameter("dcId", dtlEntity.getDcId());
										query.setParameter("branchId", hdrEntity.getBranchId());
										k = query.executeUpdate();

										dtlEntity.setGrnDoneFlag(false);
										// change to false when start working on service module
										dtlEntity.setPdiDoneFlag(true);
										dtlEntity.setMachineInvoiceHDR(hdrEntity);
										
										
										
									}

								}

								if (isSuccess) {
									if (machineInvoiceItemDtlList != null && !machineInvoiceItemDtlList.isEmpty()) {
										for (MachineInvoiceItemDtlEntity dtlEntity : machineInvoiceItemDtlList) {
											dtlEntity.setGrnDoneFlag(false);
											// change to false when start working on service module
											dtlEntity.setPdiDoneFlag(true);
											dtlEntity.setMachineInvoiceHDR(hdrEntity);
										}
									}

									hdrEntity.setInvoiceNumber(invoiceNumber);
									hdrEntity.setInvoiceStatus("INVOICED");
									hdrEntity.setCreatedBy(userId);
									hdrEntity.setCreatedDate(currDate);
									hdrEntity.setGrnDoneFlag(false);
									// change to false when start working on service module
									hdrEntity.setPdiDoneFlag(true);
									session.save(hdrEntity);
								}

							} else {
								logger.error(this.getClass().getName(),
										"Error While Validating Last Machine Invoice Number.");
								responseModel.setMsg(
										"Error While Validating Last Machine Invoice Number. Please Contact Your System Administrator.");
								isSuccess = false;
							}
						} else {
							logger.error(this.getClass().getName(),
									"Error While Fetching Machine Invoice Type Detail.");
							responseModel.setMsg(
									"Error While Fetching Machine Invoice Type Detail. Please Contact Your System Administrator.");
							isSuccess = false;
						}
					} else {
						// Dealer Not Found.
						isSuccess = false;
						responseModel.setMsg("Dealer Not Found.");
					}
				} else {
					// User not found
					isSuccess = false;
					responseModel.setMsg("User Not Found.");
				}
				if (isSuccess) {
					if (invoiceTypeId == 4) {
						for (InvoiceMachDtlCreateRequestModel obj : machineInvoiceDtlList2) {
							vinId = obj.getVinId();
							String result = updateStockForCoDealer(vinId, userId, dealerId, branchId, coDealerId, session, poHdrId);
						}
					}
					transaction.commit();
				}
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (isSuccess) {
				mapData = fetchInvoiceNoByInvId(session, hdrEntity.getSalesInvoiceHdrId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setSalesInvoiceHdrId(hdrEntity.getSalesInvoiceHdrId());
					responseModel.setInvoiceNumber((String) mapData.get("invNumber"));
					responseModel.setMsg("Machine Invoice Number Created Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				} else {
					responseModel.setMsg("Error While Fetching Machine Invoice Number.");
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Creating Machine Invoice.");
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchLastInvoiceDTLByBranchId(Session session, BigInteger branchId, String fy) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "select top 1 invoice_number "
				+ "	   from SA_MACHINE_INVOICE_HDR (nolock) pr where DATEPART(year,invoice_date)=:fy "
				+ "	   and pr.branch_id =:branchId order by sales_invoice_id desc ";
		mapData.put("ERROR", "Machine Last Invoice Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("branchId", branchId);
			query.setParameter("fy", fy);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String invNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					invNumber = (String) row.get("invoice_number");
				}
				mapData.put("invNumber", invNumber);
			}
			mapData.put("SUCCESS", "FETCHED");
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE INVOICE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE INVOICE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchInvoiceNoByInvId(Session session, BigInteger hdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select invoice_number from SA_MACHINE_INVOICE_HDR (nolock) pr where pr.sales_invoice_id =:hdrId";
		mapData.put("ERROR", "Machine Invoice Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("hdrId", hdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String invNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					invNumber = (String) row.get("invoice_number");
				}
				mapData.put("invNumber", invNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE INVOICE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE INVOICE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	public void saveDataInERPInvoice(Session session) {
		Query query = null;
		String sqlQuery = "exec Sync_STG_ERP_CO_DEALER_INVOICE";	
		query = session.createSQLQuery(sqlQuery);
		query.executeUpdate();
	}
	
	public void syncERPInvoiceData(Session session) {
		Query query = null;
		String sqlQuery = "exec IS_Sync_MACHINE_ERP_CO_DEALER_INVOICE";	
		query = session.createSQLQuery(sqlQuery);
		query.executeUpdate();
	}
	
	public String updateStockForCoDealer(BigInteger vinId, BigInteger userId, BigInteger dealerId,
			BigInteger branchId, BigInteger coDealerId, Session session, BigInteger poHdrId) {
		logger.info(vinId + " " + userId + " " + coDealerId + " " + poHdrId);
		Query query = null;
		String result = null;
		List<?> resultList = null;
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateToString = null;
		dateToString = dateFormat1.format(new java.util.Date());

//		String sqlQuery = "exec [update_codealer_stock] :vinid, :dealerid, :usercode, :poId";

		String sqlQuery = " update SA_MACHINE_VIN_MASTER set Sale_Date = :saleDate, Dealer_id = :dealerId,"
				+ " Branch_id = :branchId, Co_dealer_id =:coDealerId,"
				+ " last_modified_by = :modifiedBy, last_modified_date = :modifiedDate where vin_id =:vinId";

		query = session.createSQLQuery(sqlQuery);
		query.setParameter("saleDate", dateToString);
		query.setParameter("dealerId", dealerId);
		query.setParameter("branchId", branchId);
		query.setParameter("coDealerId", coDealerId);
		query.setParameter("modifiedBy", userId);
		query.setParameter("modifiedDate", dateToString);
		query.setParameter("vinId", vinId);
		query.executeUpdate();
		
//		resultList = query.list();
//		if (null != resultList && !resultList.isEmpty()) {
//			result = (String) resultList.get(0);
//		}
		return result;
	}

}
