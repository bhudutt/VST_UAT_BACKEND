/**
 * 
 */
package com.hitech.dms.web.dao.dc.create;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.hitech.dms.web.entity.delivery.DeliveryChallanCheckListEntity;
import com.hitech.dms.web.entity.delivery.DeliveryChallanDtlEntity;
import com.hitech.dms.web.entity.delivery.DeliveryChallanHdrEntity;
import com.hitech.dms.web.entity.delivery.DeliveryChallanItemEntity;
import com.hitech.dms.web.entity.ledger.SalesMachineInventoryLedgerEntity;
import com.hitech.dms.web.entity.ledger.SalesMachineItemInvEntity;
import com.hitech.dms.web.entity.ledger.SalesMachineItemInvLedgerEntity;
import com.hitech.dms.web.entity.ledger.SalesMachineItemInvLedgerPEntity;
import com.hitech.dms.web.model.dc.create.request.AllotMachDtlForDCCreateRequestModel;
import com.hitech.dms.web.model.dc.create.request.DcCreateRequestModel;
import com.hitech.dms.web.model.dc.create.response.DcCreateResponseModel;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class DcCreateDaoImpl implements DcCreateDao {
	private static final Logger logger = LoggerFactory.getLogger(DcCreateDaoImpl.class);

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
			requestModel.setIsFor("MACHINEPO");
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

	@SuppressWarnings({ "deprecation", "rawtypes", "resource" })
	public DcCreateResponseModel createDc(String authorizationHeader, String userCode,
			DcCreateRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("create createDc invoked.." + userCode);
		}
		logger.debug(requestModel.toString());
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		DcCreateResponseModel responseModel = new DcCreateResponseModel();
		DeliveryChallanHdrEntity hdrEntity = null;
		List<AllotMachDtlForDCCreateRequestModel> dcMechList=null;
		boolean isSuccess = true;
		String dcNumber = null;
		String sqlQuery = null;
		try {
			if (requestModel.getDcDtlList() == null || requestModel.getDcDtlList().isEmpty()) {
				logger.error(this.getClass().getName(), "At-least One Line Item Is Required");
				responseModel.setMsg("At-least One Line Item Is Required");
				isSuccess = false;
			}
			if (isSuccess) {
				hdrEntity = mapper.map(requestModel, DeliveryChallanHdrEntity.class, "DCMapId");
				hdrEntity.setCustomerId(requestModel.getCustomerDetail().getCustomerId());
				dcMechList=requestModel.getDcDtlList();
				System.out.println("dcMachineList "+dcMechList);
				session = sessionFactory.openSession();
				transaction = session.beginTransaction();
				BigInteger userId = null;
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

						mapData = fetchLastMachineDCDTLByBranchId(session, hdrEntity.getBranchId(), strYear);
						String lastDCNumber = null;
						String gatePassNo = null;
						simpleformat = new SimpleDateFormat("yy");
						strYear = simpleformat.format(currDate);
						if (mapData != null && mapData.get("SUCCESS") != null) {
							/*
							 * if (mapData.get("dcNumber") != null) {
							 * 
							 * lastDCNumber = (String) mapData.get("dcNumber"); int lastIndexOf =
							 * dealerDtl.getDealerCode().length()+2;//lastDCNumber.lastIndexOf(strYear);
							 * String prefix = lastDCNumber.substring(0, lastIndexOf); dcNumber =
							 * lastDCNumber.substring(lastIndexOf + 2, lastDCNumber.length()); Integer i =
							 * Integer.valueOf(dcNumber); dcNumber = prefix + strYear +
							 * String.format("%04d", i + 1); gatePassNo = "G" + prefix + dcNumber; //
							 * dcNumber = prefix + dcNumber; } else { dcNumber = "DC" +
							 * dealerDtl.getDealerCode() + strYear + "0001"; gatePassNo = "G" +
							 * dealerDtl.getDealerCode() + strYear + "0001"; }
							 */
							
							dcNumber = getDocumentNumber("DC",hdrEntity.getBranchId().intValue(), session);
							gatePassNo = getDocumentNumber("G",hdrEntity.getBranchId().intValue(), session);

							List<DeliveryChallanDtlEntity> dcDtlList = hdrEntity.getDcDtlList();
							List<DeliveryChallanItemEntity> dcItemList = hdrEntity.getDcItemList();
							List<DeliveryChallanCheckListEntity> dcCheckList = hdrEntity.getDcCheckList();
							SalesMachineItemInvLedgerEntity inventoryLedgerEntity = null;
							SalesMachineInventoryLedgerEntity saleMachineinventoryLedger =null;
							for (DeliveryChallanDtlEntity dtlEntity : dcDtlList) {
								
								if(dtlEntity.getProductType().equalsIgnoreCase("Accessory") || dtlEntity.getProductType().equalsIgnoreCase("Implement")) {
									sqlQuery = "Select TOP 1 * from SA_MACHINE_ITEM_INV_LEDGER (nolock) where machine_inventory_id =:machineInventory and branch_id =:branchId "
											+ "  order by machine_item_id desc ";
									query = session.createNativeQuery(sqlQuery)
											.addEntity(SalesMachineItemInvLedgerEntity.class);
									query.setParameter("machineInventory", dtlEntity.getMachineInventoryId());
									query.setParameter("branchId", hdrEntity.getBranchId());
									 inventoryLedgerEntity = (SalesMachineItemInvLedgerEntity) query
											.uniqueResult();
									if (inventoryLedgerEntity == null) {
										logger.error(this.getClass().getName(),
												"While Allotting InventoryLedgerEntity is null/Empty for "
														+ dtlEntity.getMachineInventoryId() + " : "
														+ hdrEntity.getBranchId());
										responseModel
												.setMsg("Stock Not Found. Kindly Contact Your Admnistrator.");
										isSuccess = false;
										break;
									}else {
										sqlQuery = "Select * from SA_MACHINE_ITEM_INV (nolock) "
												+ " where branch_id =:branchId and machine_item_id =:machineItemId";
										query = session.createNativeQuery(sqlQuery)
												.addEntity(SalesMachineItemInvEntity.class);
										query.setParameter("branchId", hdrEntity.getBranchId());
										query.setParameter("machineItemId", dtlEntity.getItemId());
										SalesMachineItemInvEntity itemInvEntity = (SalesMachineItemInvEntity) query
												.uniqueResult();
										
										if (itemInvEntity == null) {
											logger.error(this.getClass().getName(),
													"DC implDtlEntity is null/Empty for " + hdrEntity.getBranchId() + " : "
															+ dtlEntity.getItemId());
											responseModel
													.setMsg("Item Inventory is Empty. Kindly Contact Your Administrator.");
											isSuccess = false;
											break;
										} else {
											Integer stockQty = itemInvEntity.getStockQty();
											Integer netStockQty = itemInvEntity.getNetStockQty();
											
											Integer avail=inventoryLedgerEntity.getAllotQuantity().intValue()-inventoryLedgerEntity.getOutward();
											
											if (stockQty != null) {
												if (stockQty.compareTo(avail) >= 0) {
													stockQty = stockQty - inventoryLedgerEntity.getAllotQuantity().intValue();
													netStockQty = netStockQty - inventoryLedgerEntity.getAllotQuantity().intValue();
													itemInvEntity.setStockQty(stockQty);
													itemInvEntity.setNetStockQty(netStockQty);
													itemInvEntity.setModifiedBy(userId);
													itemInvEntity.setModifiedDate(currDate);
													session.merge(itemInvEntity);
												} else {
													logger.error(this.getClass().getName(),
															"Qty Must Not Be Greater Than Stock Qty. for "
																	+ hdrEntity.getBranchId() + " : "
																	+ dtlEntity.getItemId());
													responseModel.setMsg(
															"Qty Must Not Be Greater Than Stock Qty. Kindly Contact Your Administrator.");
													isSuccess = false;
													break;
												}
											} else {
												logger.error(this.getClass().getName(),
														"Stock Qty. Not Found for " + hdrEntity.getBranchId() + " : "
																+ dtlEntity.getItemId());
												responseModel
														.setMsg("Stock Qty. Not Found. Kindly Contact Your Administrator.");
												isSuccess = false;
												break;
											}

											// Also insert into SA_MACHINE_ITEM_INV_LEDGER Table
											//SalesMachineItemInvLedgerEntity itemInvLedgerEntity = new SalesMachineItemInvLedgerEntity();
											//SalesMachineItemInvLedgerPEntity salesMachineItemInvLedgerP = new SalesMachineItemInvLedgerPEntity();

											//salesMachineItemInvLedgerP.setBranchId(hdrEntity.getBranchId());
										//	salesMachineItemInvLedgerP.setMachineItemId(dtlEntity.getItemId());
											//inventoryLedgerEntity.setTransactionNo(dcNumber);
											//inventoryLedgerEntity.setTransactionDate(currDate);

										//	itemInvLedgerEntity.setSalesMachineItemInvLedgerP(salesMachineItemInvLedgerP);
											inventoryLedgerEntity.setOutward(inventoryLedgerEntity.getAllotQuantity().intValue());
											inventoryLedgerEntity.setTransactionDesc("THROUGH DC");
											inventoryLedgerEntity.setModifiedBy(userId);
											inventoryLedgerEntity.setModifiedDate(currDate);

											session.merge(inventoryLedgerEntity);

										}
											
										
										
										
									}
									
									
								}else {
									sqlQuery = "Select TOP 1 * from SA_MACHINE_INVENTORY_LEDGER (nolock) where machine_inventory_id =:machineInventory and branch_id =:branchId "
											+ " and OUT_DOC_TYPE IS NULL and OUT_DOC_NUMBR IS NULL and OUT_DATE IS NULL and allot_flag = 1 order by machine_inventory_id desc ";
									query = session.createNativeQuery(sqlQuery)
											.addEntity(SalesMachineInventoryLedgerEntity.class);
									query.setParameter("machineInventory", dtlEntity.getMachineInventoryId());
									query.setParameter("branchId", hdrEntity.getBranchId());
									saleMachineinventoryLedger = (SalesMachineInventoryLedgerEntity) query
											.uniqueResult();
									if (saleMachineinventoryLedger == null) {
										logger.error(this.getClass().getName(),
												"While Creating DC InventoryLedgerEntity is null/Empty for "
														+ dtlEntity.getMachineInventoryId() + " : "
														+ hdrEntity.getBranchId());
										responseModel.setMsg(
												"Stock Not Found While Creating DC. Kindly Contact Your Admnistrator.");
										isSuccess = false;
										break;
									} else {
										saleMachineinventoryLedger.setOutDocNo(dcNumber);
										saleMachineinventoryLedger.setOutDocType(WebConstants.DC);
										saleMachineinventoryLedger.setOutDate(currDate);
										saleMachineinventoryLedger.setModifiedBy(userId);
										saleMachineinventoryLedger.setModifiedDate(currDate);

										session.merge(saleMachineinventoryLedger);
									}
								}
								
								if(inventoryLedgerEntity!=null || saleMachineinventoryLedger!=null) {
									//  session = sessionFactory.openSession();
									  String hql = "UPDATE SA_MACHINE_VIN_MASTER SET delivery_date = :deliveryDate WHERE vin_id = :vinId";
									 	query = session.createNativeQuery(hql);
									    query.setParameter("deliveryDate", new Date());
									    query.setParameter("vinId", inventoryLedgerEntity!=null?inventoryLedgerEntity.getVinId():saleMachineinventoryLedger!=null?saleMachineinventoryLedger.getVinId():null);
									    query.executeUpdate();
								}
							
								dtlEntity.setDcHdr(hdrEntity);
							}
							if (isSuccess) {
								for (DeliveryChallanItemEntity challanItemEntity : dcItemList) {
									sqlQuery = "Select * from SA_MACHINE_ITEM_INV (nolock) "
											+ " where branch_id =:branchId and machine_item_id =:machineItemId";
									query = session.createNativeQuery(sqlQuery)
											.addEntity(SalesMachineItemInvEntity.class);
									query.setParameter("branchId", hdrEntity.getBranchId());
									query.setParameter("machineItemId", challanItemEntity.getMachineItemId());
									SalesMachineItemInvEntity itemInvEntity = (SalesMachineItemInvEntity) query
											.uniqueResult();
									if (itemInvEntity == null) {
										logger.error(this.getClass().getName(),
												"DC implDtlEntity is null/Empty for " + hdrEntity.getBranchId() + " : "
														+ challanItemEntity.getMachineItemId());
										responseModel
												.setMsg("Item Inventory is Empty. Kindly Contact Your Administrator.");
										isSuccess = false;
										break;
									} else {
										Integer stockQty = itemInvEntity.getStockQty();
										Integer netStockQty = itemInvEntity.getNetStockQty();
										if (stockQty != null) {
											if (stockQty.compareTo(challanItemEntity.getQty()) >= 0) {
												stockQty = stockQty - challanItemEntity.getQty();
												netStockQty = netStockQty - challanItemEntity.getQty();
												itemInvEntity.setStockQty(stockQty);
												itemInvEntity.setNetStockQty(netStockQty);
												itemInvEntity.setModifiedBy(userId);
												itemInvEntity.setModifiedDate(currDate);
												session.merge(itemInvEntity);
											} else {
												logger.error(this.getClass().getName(),
														"Qty Must Not Be Greater Than Stock Qty. for "
																+ hdrEntity.getBranchId() + " : "
																+ challanItemEntity.getMachineItemId());
												responseModel.setMsg(
														"Qty Must Not Be Greater Than Stock Qty. Kindly Contact Your Administrator.");
												isSuccess = false;
												break;
											}
										} else {
											logger.error(this.getClass().getName(),
													"Stock Qty. Not Found for " + hdrEntity.getBranchId() + " : "
															+ challanItemEntity.getMachineItemId());
											responseModel
													.setMsg("Stock Qty. Not Found. Kindly Contact Your Administrator.");
											isSuccess = false;
											break;
										}

										// Also insert into SA_MACHINE_ITEM_INV_LEDGER Table
										SalesMachineItemInvLedgerEntity itemInvLedgerEntity = new SalesMachineItemInvLedgerEntity();
										SalesMachineItemInvLedgerPEntity salesMachineItemInvLedgerP = new SalesMachineItemInvLedgerPEntity();

										salesMachineItemInvLedgerP.setBranchId(hdrEntity.getBranchId());
										salesMachineItemInvLedgerP
												.setMachineItemId(challanItemEntity.getMachineItemId());
										salesMachineItemInvLedgerP.setTransactionNo(dcNumber);
										salesMachineItemInvLedgerP.setTransactionDate(currDate);

										itemInvLedgerEntity.setSalesMachineItemInvLedgerP(salesMachineItemInvLedgerP);
										itemInvLedgerEntity.setInward(challanItemEntity.getQty());
										itemInvLedgerEntity.setOutward(challanItemEntity.getQty());
										itemInvLedgerEntity.setTransactionDesc("THROUGH DC");
										itemInvLedgerEntity.setModifiedBy(userId);
										itemInvLedgerEntity.setModifiedDate(currDate);

										session.save(itemInvLedgerEntity);

									}
									challanItemEntity.setDcHdr(hdrEntity);
								}
								if (isSuccess) {
									for (DeliveryChallanCheckListEntity checkEntity : dcCheckList) {
										checkEntity.setDcHdr(hdrEntity);
									}
									hdrEntity.setDcNumber(dcNumber);
									hdrEntity.setDcStatus(WebConstants.INVOICE_NOT_DONE);
									hdrEntity.setGatePassNumber(gatePassNo);
									hdrEntity.setCreatedBy(userId);
									hdrEntity.setCreatedDate(currDate);
									Serializable save = session.save(hdrEntity);

									mapData = commonDao.updateAllotmentStatus(session, userCode, userId, currDate,
											hdrEntity.getMachineAllotmentId(), WebConstants.DELIVERY_CHALLAN);
									
									updateDocumentNumber(dcNumber.substring(dcNumber.length() - 7), "DC",hdrEntity.getBranchId() + "", session,"Delliery Challan");
									updateDocumentNumber(gatePassNo.substring(gatePassNo.length() - 7), "G",hdrEntity.getBranchId() + "", session,"Sales Gate Pass");
									
									if (mapData != null && mapData.get("SUCCESS") != null) {
										logger.info("Allotment Status has been updated to Allotted.");
									} else {
										logger.error(this.getClass().getName(),
												"Error While Updating Allotment Status.");
										responseModel.setMsg(
												"Error While Updating Allotment Status. Please Contact Your System Administrator.");
										isSuccess = false;
									}
									
									
									if (hdrEntity.getEnquiryId() != null) {
										mapData = commonDao.updateEnquiryStatus(session, userCode, userId,
												currDate, hdrEntity.getEnquiryId(),
												WebConstants.DC_GENERATED);
										if (mapData != null && mapData.get("SUCCESS") != null) {
											logger.info("Enquiry Status has been updated to Allotted.");
										} else {
											logger.error(this.getClass().getName(),
													"Error While Updating Enquiry Status.");
											responseModel.setMsg(
													"Error While Updating Enquiry Status. Please Contact Your System Administrator.");
											isSuccess = false;
										}
									}
								}
							}
						} else {
							logger.error(this.getClass().getName(), "Error While Validating Last Machine DC Number.");
							responseModel.setMsg(
									"Error While Validating Last Machine DC Number. Please Contact Your System Administrator.");
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
				
				mapData = fetchMachineDCNoByDCId(session, hdrEntity.getDcId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					
					// update SA_VIN_LIST 
					Integer status=updateSAVinMaster(requestModel,dcMechList);
					if(status==1)
					{
						responseModel.setDcId(hdrEntity.getDcId());
						responseModel.setDcNumber((String) mapData.get("dcNumber"));
						responseModel.setMsg("Machine DC Number Created Successfully.And Vin Master updated");
						responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					}
					else
					{
						responseModel.setDcId(hdrEntity.getDcId());
						responseModel.setDcNumber((String) mapData.get("dcNumber"));
						responseModel.setMsg("Machine DC Number Created Successfully.");
						responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					}
					
				} else {
					responseModel.setMsg("Error While Fetching Machine DC Number.");
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Creating Machine Allotment.");
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}

		return responseModel;
	}

	
	public String getDocumentNumber(String documentPrefix, Integer branchId, Session session) {
		Query query = null;
		String documentNumber = null;
		List<?> documentNoList = null;
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		String dateToString = null;
		dateToString = dateFormat1.format(new java.util.Date());
		String sqlQuery = "exec [Get_Doc_No_25AUG] :DocumentType, :BranchID, '" + dateToString + "'";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("DocumentType", documentPrefix);
		query.setParameter("BranchID", branchId);
		
		documentNoList = query.list();
		if (null != documentNoList && !documentNoList.isEmpty()) {
			documentNumber = (String) documentNoList.get(0);
		}
		return documentNumber;
	}
	
	public void updateDocumentNumber(String lastDocumentNumber, String documentPrefix, String branchId,
			Session session,String docDesc) {

		if (null != lastDocumentNumber) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat1.format(new java.util.Date());
			String updateQuery = "EXEC Update_INV_Doc_No :lastDocumentNo,:DocumentTypeDesc,:currentDate,:branchId,:documentPrefix";
			Query query = session.createSQLQuery(updateQuery);
			query.setParameter("lastDocumentNo", lastDocumentNumber);
			query.setParameter("DocumentTypeDesc", docDesc);
			query.setParameter("documentPrefix", documentPrefix);
			query.setParameter("branchId", Integer.parseInt(branchId));
			query.setParameter("currentDate", currentDate);
			query.executeUpdate();
		}
	}
	
	private Integer updateSAVinMaster(DcCreateRequestModel requestModel,
			List<AllotMachDtlForDCCreateRequestModel> dcMechList) {
		 boolean isSuccess=false;
		 Integer   updateCount =0;
		Integer updateStatus=0;
		Session session= null;
		Query query=null;
		try
		{
			session=sessionFactory.openSession();
			
			if(dcMechList!=null) {
				
				for(AllotMachDtlForDCCreateRequestModel model:dcMechList)
				{
					
					 session.beginTransaction();

			            String sqlQuery = "exec InsertCustomerId_IN_VIN_MASTER :enquiryId, :chassisNo";
			             query = session.createSQLQuery(sqlQuery)
			                    .setParameter("enquiryId", requestModel.getEnquiryId())
			                    .setParameter("chassisNo", model.getChassisNo());

			             updateCount = query.executeUpdate();
			            if(updateCount==1)
			            {
			            	isSuccess=true;
			            }

			            session.getTransaction().commit();


			           

				}
			}
			
			
			
		}catch (SQLGrammarException ex) {
			
			isSuccess = false;
			updateCount=-1;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			
			isSuccess = false;
			updateCount=-1;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			
			isSuccess = false;
			updateCount=-1;
			logger.error(this.getClass().getName(), ex);
		} finally {
			
				
			
	}
		
       // System.out.println("before send response "+updateCount);

		  return updateCount;

	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchLastMachineDCDTLByBranchId(Session session, BigInteger branchId, String fy) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "select top 1 dc_number"
				+ "	   from SA_MACHINE_DC_HDR (nolock) pr where DATEPART(year,dc_date)=:fy "
				+ "	   and pr.branch_id =:branchId order by dc_id desc ";
		mapData.put("ERROR", "Machine Last DC Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("branchId", branchId);
			query.setParameter("fy", fy);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String dcNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					dcNumber = (String) row.get("dc_number");
				}
				mapData.put("dcNumber", dcNumber);
			}
			mapData.put("SUCCESS", "FETCHED");
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE DC DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE DC DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchMachineDCNoByDCId(Session session, BigInteger dcHdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select dc_number from SA_MACHINE_DC_HDR (nolock) pr where pr.dc_id =:dcHdrId";
		mapData.put("ERROR", "Machine DC Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("dcHdrId", dcHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String dcNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					dcNumber = (String) row.get("dc_number");
				}
				mapData.put("dcNumber", dcNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE DC DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE DC DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
}
