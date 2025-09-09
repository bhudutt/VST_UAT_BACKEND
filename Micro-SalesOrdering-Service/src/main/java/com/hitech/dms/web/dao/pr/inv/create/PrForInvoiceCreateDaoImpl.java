/**
 * 
 */
package com.hitech.dms.web.dao.pr.inv.create;

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
import com.hitech.dms.web.dao.machinepo.common.MachinePOCommonDao;
import com.hitech.dms.web.entity.sales.grn.SalesMachineInventoryLedgerEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineItemInvEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineItemInvLedgerEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineItemInvLedgerPEntity;
import com.hitech.dms.web.entity.sales.purchase.ret.SalesMachinePurchaseReturnDtlEntity;
import com.hitech.dms.web.entity.sales.purchase.ret.SalesMachinePurchaseReturnImplDtlEntity;
import com.hitech.dms.web.entity.sales.purchase.ret.inv.SalesMachinePurchaseReturnInvDtlEntity;
import com.hitech.dms.web.entity.sales.purchase.ret.inv.SalesMachinePurchaseReturnInvEntity;
import com.hitech.dms.web.entity.sales.purchase.ret.inv.SalesMachinePurchaseReturnInvImplDtlEntity;
import com.hitech.dms.web.model.branchdtl.request.BranchDTLRequestModel;
import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.pr.inv.create.request.PrForInvoiceCreateRequestModel;
import com.hitech.dms.web.model.pr.inv.create.response.PrFornvoiceCreateResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class PrForInvoiceCreateDaoImpl implements PrForInvoiceCreateDao {
	private static final Logger logger = LoggerFactory.getLogger(PrForInvoiceCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private MachinePOCommonDao machinePOCommonDao;
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

	public BranchDTLResponseModel fetchMainBranchDtlByDealerId(String authorizationHeader, BigInteger dealerId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchMainBranchDtlByDealerId invoked..");
		}
		BranchDTLResponseModel responseModel = null;
		try {
			BranchDTLRequestModel requestModel = new BranchDTLRequestModel();
			requestModel.setDealerId(dealerId);
			requestModel.setIsFor("MACHINEPRINV");
			HeaderResponse headerResponse = commonServiceClient.fetchMainBranchDtlByDealerId(authorizationHeader,
					requestModel);
			Object object = headerResponse.getResponseData();
			if (object != null) {
				try {
					com.google.gson.Gson gson = new com.google.gson.Gson();
					String jsonString = gson.toJson(object);
					responseModel = gson.fromJson(jsonString, BranchDTLResponseModel.class);

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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public PrFornvoiceCreateResponseModel createMachinePurchaseReturnInv(String authorizationHeader, String userCode,
			PrForInvoiceCreateRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("createMachinePurchaseReturnInv invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		PrFornvoiceCreateResponseModel responseModel = new PrFornvoiceCreateResponseModel();
		SalesMachinePurchaseReturnInvEntity purchaseReturnInvEntity = null;
		BigInteger id=null;
		boolean isSuccess = true;
		String prInvNumber = null;
		String sqlQuery = null;

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			purchaseReturnInvEntity = mapper.map(requestModel, SalesMachinePurchaseReturnInvEntity.class,
					"SalesPRINVMapId");

			List<SalesMachinePurchaseReturnInvDtlEntity> salesMachinePurchaseReturnDtlList = purchaseReturnInvEntity
					.getSalesMachineGrnDtlList();
			List<SalesMachinePurchaseReturnInvImplDtlEntity> salesMachinePurchaseReturnImplList = purchaseReturnInvEntity
					.getSalesMachineGrnImplDtlList();

			BigInteger userId = null;
			Date currDate = new Date();
			mapData = machinePOCommonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				if ((salesMachinePurchaseReturnDtlList == null || salesMachinePurchaseReturnDtlList.isEmpty())
						&& (salesMachinePurchaseReturnImplList == null
								|| salesMachinePurchaseReturnImplList.isEmpty())) {
					System.out.println("inside null");
					logger.error(this.getClass().getName(), "At-least One Line Item Is Required");
					responseModel.setMsg("At-least One Line Item Is Required");
					isSuccess = false;
				}
				System.out.println("isSuccess "+isSuccess);
				if (isSuccess) {
					// fetch Dealer Detail
					DealerDTLResponseModel dealerDtl = fetchDealerDTLByDealerId(authorizationHeader,
							purchaseReturnInvEntity.getDealerId());
					if (dealerDtl != null) {
						// fetch Main Branch Detail
						BranchDTLResponseModel branchDTLResponseModel = fetchMainBranchDtlByDealerId(
								authorizationHeader, purchaseReturnInvEntity.getDealerId());
						if (branchDTLResponseModel != null) {
							SimpleDateFormat simpleformat = new SimpleDateFormat("yy");
							String strYear = simpleformat.format(currDate);

							// fetch grnid, GrnType code
							BigInteger coDealerId = null;
							String grnTypeCode = null;
							BigInteger grnId = null;
							sqlQuery = "select DB.parent_dealer_id, SMG.grn_id, SMG.grn_type_id, MGT.grn_type_code from SA_MACHINE_GRN (nolock) SMG "
									+ " inner join SA_MST_MACHINE_GRN_TYPE (nolock) MGT ON SMG.grn_type_id=MGT.GRN_type_id "
									+ " inner join SA_MACHINE_PURCH_RET (nolock) MPR ON SMG.grn_id = MPR.grn_id "
									+ " left join SA_MACHINE_INVOICE_HDR (nolock) MIH ON SMG.co_dealer_invoice_id = MIH.sales_invoice_id "
									+ " left join ADM_BP_DEALER_BRANCH (nolock) DB ON MIH.branch_id = DB.branch_id"
									+ " where IsFromSAPAndMainBranch = 'Y' and DB.parent_dealer_id =:dealerId"
									+ " and MPR.purchase_return_id =:prId";
							query = session.createNativeQuery(sqlQuery);
							query.setParameter("dealerId", purchaseReturnInvEntity.getDealerId());
							query.setParameter("prId", purchaseReturnInvEntity.getPurchaseReturnId());
							
							query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
							List data = query.list();
							if (data != null && !data.isEmpty()) {
								for (Object object : data) {
									Map row = (Map) object;
									coDealerId = (BigInteger) row.get("parent_dealer_id");
									grnId = (BigInteger) row.get("grn_id");
									grnTypeCode = (String) row.get("grn_type_id");
								}
							}
							if (grnTypeCode != null && grnTypeCode.equals(WebConstants.GRNTYPE_PO)) {
								purchaseReturnInvEntity.setToDealerId(coDealerId);
							}

							mapData = fetchLastMachinePRINVDTLByDlrId(session, dealerDtl.getDealerId(), strYear);
							String lastInvNumber = null;
							if (mapData != null && mapData.get("SUCCESS") != null) {
								if (mapData.get("prInvNumber") != null) {
									lastInvNumber = (String) mapData.get("prInvNumber");
									int lastIndexOf = lastInvNumber.lastIndexOf(strYear);
									String prefix = lastInvNumber.substring(0, lastIndexOf);
									prInvNumber = lastInvNumber.substring(lastIndexOf + 2, lastInvNumber.length());
									Integer i = Integer.valueOf(prInvNumber);
									prInvNumber = prefix + String.format("%04d", i + 1);
								} else {
									prInvNumber = "PRI" + dealerDtl.getDealerCode() + strYear + "0001";
								}
							}else {
								prInvNumber = "PRI" + dealerDtl.getDealerCode() + strYear + "0001";
							}
							
							
							if (true) {//mapData != null && mapData.get("SUCCESS") != null
								
								if (salesMachinePurchaseReturnDtlList != null
										&& !salesMachinePurchaseReturnDtlList.isEmpty()) {
									sqlQuery = "Select TOP 1 * from SA_MACHINE_INVENTORY_LEDGER (nolock) where grn_id =:grnId and vin_id =:vinId and branch_id =:branchId "
											+ " and OUT_DOC_TYPE IS NULL and OUT_DOC_NUMBR IS NULL and OUT_DATE IS NULL and allot_flag = 0 order by machine_inventory_id desc ";

									for (SalesMachinePurchaseReturnInvDtlEntity dtlEntity : salesMachinePurchaseReturnDtlList) {
										if (dtlEntity.isDeleted()) {
											dtlEntity.setGrnDoneFlag(false);
											// set pdi false when service module get started, and pdi done will be by pdi
											// type service
											dtlEntity.setPdiDoneFlag(true);
											dtlEntity.setSalesMachinePurchaseReturnInv(purchaseReturnInvEntity);
											query = session.createNativeQuery(sqlQuery)
													.addEntity(SalesMachineInventoryLedgerEntity.class);
											query.setParameter("grnId", grnId);
											query.setParameter("vinId", dtlEntity.getVinId());
											query.setParameter("branchId", branchDTLResponseModel.getBranchId());
											SalesMachineInventoryLedgerEntity inventoryLedgerEntity = (SalesMachineInventoryLedgerEntity) query
													.uniqueResult();
											if (inventoryLedgerEntity == null) {
												logger.error(this.getClass().getName(),
														"inventoryLedgerEntity is null/Empty for " + grnId + " : "
																+ dtlEntity.getVinId() + " : "
																+ branchDTLResponseModel.getBranchId());
												isSuccess = false;
												break;
											} else {
												// out from dealer stock
												inventoryLedgerEntity.setOutDocNo(prInvNumber);
												inventoryLedgerEntity.setOutDocType(WebConstants.PRI);
												inventoryLedgerEntity
														.setOutDate(purchaseReturnInvEntity.getPurchaseReturnInvDate());
												inventoryLedgerEntity.setPurchaseReturnFlag(false);
												inventoryLedgerEntity.setModifiedBy(userId);
												inventoryLedgerEntity.setModifiedDate(currDate);

												session.merge(inventoryLedgerEntity);
											}
										}
										
									}
								}
								if (salesMachinePurchaseReturnImplList != null
										&& !salesMachinePurchaseReturnImplList.isEmpty()) {
									for (SalesMachinePurchaseReturnInvImplDtlEntity implEntity : salesMachinePurchaseReturnImplList) {
										
										if (implEntity.isDeleted())
										{
											sqlQuery = "Select * from SA_MACHINE_ITEM_INV (nolock) where branch_id =:branchId and machine_item_id =:machineItemId";
											query = session.createNativeQuery(sqlQuery)
													.addEntity(SalesMachineItemInvEntity.class);
											query.setParameter("branchId", branchDTLResponseModel.getBranchId());
											query.setParameter("machineItemId", implEntity.getMachineItemId());
											SalesMachineItemInvEntity itemInvEntity = (SalesMachineItemInvEntity) query
													.uniqueResult();
											if (itemInvEntity == null) {
												logger.error(this.getClass().getName(),
														"implEntity is null/Empty for "
																+ branchDTLResponseModel.getBranchId() + " : "
																+ implEntity.getMachineItemId());
												isSuccess = false;
												break;
											} else {
												Integer stockQty = itemInvEntity.getStockQty();
												Integer blockQty = itemInvEntity.getBlockedQty();

												// subtract from block qty to return qty
												blockQty = blockQty - implEntity.getReturnQuantity();
												// subtract from current
												stockQty = stockQty - implEntity.getReturnQuantity();
												
												itemInvEntity.setBlockedQty(blockQty);
												itemInvEntity.setStockQty(stockQty);

												session.merge(itemInvEntity);
												
												// Also insert into SA_MACHINE_ITEM_INV_LEDGER Table
												SalesMachineItemInvLedgerEntity itemInvLedgerEntity = new SalesMachineItemInvLedgerEntity();
												//SalesMachineItemInvLedgerPEntity salesMachineItemInvLedgerP = new SalesMachineItemInvLedgerPEntity();

												/*
												 * salesMachineItemInvLedgerP.setBranchId(branchDTLResponseModel.
												 * getBranchId());
												 * salesMachineItemInvLedgerP.setMachineItemId(implEntity.
												 * getMachineItemId());
												 * salesMachineItemInvLedgerP.setTransactionNo(prInvNumber);
												 * salesMachineItemInvLedgerP.setTransactionDate(currDate);
												 */
												
												
												itemInvLedgerEntity.setBranchId(branchDTLResponseModel.getBranchId());
												itemInvLedgerEntity.setMachineItemId(implEntity.getMachineItemId());
												itemInvLedgerEntity.setTransactionNo(prInvNumber);
												itemInvLedgerEntity.setTransactionDate(currDate);

												//itemInvLedgerEntity.setSalesMachineItemInvLedgerP(salesMachineItemInvLedgerP);
												itemInvLedgerEntity.setInward(0);
												itemInvLedgerEntity.setOutward(implEntity.getReturnQuantity());
												itemInvLedgerEntity.setTransactionDesc("THROUGH PR INVOICE");
												itemInvLedgerEntity.setModifiedBy(userId);
												itemInvLedgerEntity.setModifiedDate(currDate);

												session.save(itemInvLedgerEntity);
											}
											implEntity.setGrnDoneFlag(false);
											// set pdi false when service module get started, and pdi done will be by pdi
											// type service
											implEntity.setPdiDoneFlag(true);
											implEntity.setSalesMachinePurchaseReturnInv(purchaseReturnInvEntity);
										}
										

									}
								}
								purchaseReturnInvEntity.setPurchaseReturnInvNumber(prInvNumber);
								purchaseReturnInvEntity.setPurchaseReturnInvDate(currDate);								purchaseReturnInvEntity.setGrnDoneFlag(false);
								// set pdi true when all dtl pdi will be done
								purchaseReturnInvEntity.setPdiDoneFlag(true);
								purchaseReturnInvEntity.setPurchaseReturnInvStatus(WebConstants.PENDING);
								purchaseReturnInvEntity.setCreatedBy(userId);
								purchaseReturnInvEntity.setCreatedDate(currDate);

								id=(BigInteger)session.save(purchaseReturnInvEntity);
								
								if(salesMachinePurchaseReturnDtlList !=null && salesMachinePurchaseReturnDtlList.size()>0) {
									for (SalesMachinePurchaseReturnInvDtlEntity dtlEntity : salesMachinePurchaseReturnDtlList) {
										dtlEntity.setPurchaseRetInvId(id);
										session.save(dtlEntity);
									}
								}
								
								  if(salesMachinePurchaseReturnImplList !=null && salesMachinePurchaseReturnImplList.size()>0) 
								  { for(SalesMachinePurchaseReturnInvImplDtlEntity dtlEntity :salesMachinePurchaseReturnImplList) 
								  	{ dtlEntity.setPurchaseRetInvId(id);
								  session.save(dtlEntity); 
								  } 
								 }
								 
								
							} else {
								// Erro WHile Fetching Last Invoice Detail Not Found
								isSuccess = false;
								responseModel.setMsg("Please Contact Your System Administrator.");
							}

						}
					} else {
						// Main Branch Not Found
						isSuccess = false;
						responseModel.setMsg("Main Branch Not Found.");
					}
				} else {
					// Dealer Not Found
					isSuccess = false;
					responseModel.setMsg("Dealer Not Found.");
				}
			} else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
//				mapData = saveIntoApproval(session, userId, purchaseReturnInvEntity);
//				if (mapData != null && mapData.get("SUCCESS") != null) {
//				} else {
//					isSuccess = false;
//					if(responseModel.getMsg() == null) {
//						responseModel.setMsg("Error While Updating Machine Purchase Return Approval Hier.");
//					}
//				}
				transaction.commit();
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
				mapData = fetchMachinePRINVNoByPRINVId(session, purchaseReturnInvEntity.getPurchaseReturnInvId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setPurchaseReturnInvId(purchaseReturnInvEntity.getPurchaseReturnInvId());
					responseModel.setPurchaseReturnInvNumber((String) mapData.get("prInvNumber"));
					responseModel.setMsg("Machine Purchase Return Invoice Number Created Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				} else {
					responseModel.setMsg("Error While Fetching Machine Purchase Return Invoice Number.");
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
			} else {
				if(responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Creating Machine Purchase Return Invoice.");
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				if (transaction != null) {
					transaction.rollback();
				}
			}
			if (session != null) {
				session.close();
			}
		}

		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchLastMachinePRINVDTLByDlrId(Session session, BigInteger dlrId, String invoiceFY) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "select top 1 purchase_ret_inv_number"
				+ "	   from SA_MACHINE_PURCH_RET_INV (nolock) pr where DATEPART(year,purchase_ret_inv_date)=:invoiceFY "
				+ "	   and pr.dealer_id =:dlrId order by purchase_ret_inv_id desc ";
		mapData.put("ERROR", "Machine Last Purchase Return Invoice Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("dlrId", dlrId);
			query.setParameter("invoiceFY", invoiceFY);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String purchase_ret_inv_number = null;
				for (Object object : data) {
					Map row = (Map) object;
					purchase_ret_inv_number = (String) row.get("purchase_ret_inv_number");
				}
				mapData.put("prInvNumber", purchase_ret_inv_number);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE PURCHASE RETURN INVOICE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING Last MACHINE PURCHASE RETURN INVOICE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchMachinePRINVNoByPRINVId(Session session, BigInteger prInvHdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select purchase_ret_inv_number from SA_MACHINE_PURCH_RET_INV (nolock) pr where pr.purchase_ret_inv_id =:prInvHdrId";
		mapData.put("ERROR", "Machine Purchase Return Invoice Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("prInvHdrId", prInvHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String purchase_ret_inv_number = null;
				for (Object object : data) {
					Map row = (Map) object;
					purchase_ret_inv_number = (String) row.get("purchase_ret_inv_number");
				}
				mapData.put("prInvNumber", purchase_ret_inv_number);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE PURCHASE RETURN INVOICE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE PURCHASE RETURN INVOICE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
}
