/**
 * 
 */
package com.hitech.dms.web.dao.pr.grn.create;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.config.publisher.PublishModel;
import com.hitech.dms.app.exceptions.RecordNotFoundException;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.machinepo.common.MachinePOCommonDao;
import com.hitech.dms.web.entity.sales.grn.SalesMachineInventoryLedgerEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineItemInvEntity;
import com.hitech.dms.web.entity.sales.purchase.ret.SalesMachinePurchaseReturnAppEntity;
import com.hitech.dms.web.entity.sales.purchase.ret.SalesMachinePurchaseReturnDtlEntity;
import com.hitech.dms.web.entity.sales.purchase.ret.SalesMachinePurchaseReturnEntity;
import com.hitech.dms.web.entity.sales.purchase.ret.SalesMachinePurchaseReturnImplDtlEntity;
import com.hitech.dms.web.model.branchdtl.request.BranchDTLRequestModel;
import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.pr.create.request.PurchaseReturnCreateRequestModel;
import com.hitech.dms.web.model.pr.create.response.PurchaseReturnCreateResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

import reactor.core.publisher.Mono;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class PurchaseReturnCreateDaoImpl implements PurchaseReturnCreateDao {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseReturnCreateDaoImpl.class);

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
			requestModel.setIsFor("MACHINEPR");
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
	public PurchaseReturnCreateResponseModel createMachinePurchaseReturn(String authorizationHeader, String userCode,
			PurchaseReturnCreateRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("createMachinePurchaseReturn invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		PurchaseReturnCreateResponseModel responseModel = new PurchaseReturnCreateResponseModel();
		SalesMachinePurchaseReturnEntity purchaseReturnEntity = null;
		BigInteger id=null;
		boolean isSuccess = true;
		String prNumber = null;
		String sqlQuery = "Select TOP 1 * from SA_MACHINE_INVENTORY_LEDGER (nolock) where grn_id =:grnId and vin_id =:vinId and branch_id =:branchId "
				+ " and OUT_DOC_TYPE IS NULL and OUT_DOC_NUMBR IS NULL and OUT_DATE IS NULL order by machine_inventory_id desc ";

		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			purchaseReturnEntity = mapper.map(requestModel, SalesMachinePurchaseReturnEntity.class, "SalesPRMapId");

			List<SalesMachinePurchaseReturnDtlEntity> salesMachinePurchaseReturnDtlList = purchaseReturnEntity
					.getSalesMachineGrnDtlList();
			List<SalesMachinePurchaseReturnImplDtlEntity> salesMachinePurchaseReturnImplList = purchaseReturnEntity
					.getSalesMachineGrnImplDtlList();
			
			List<SalesMachinePurchaseReturnAppEntity> salesMachinePurchaseReturnAppList = purchaseReturnEntity
					.getSalesMachinePurchaseReturnAppList();

			BigInteger userId = null;
			Date currDate = new Date();
			mapData = machinePOCommonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				// fetch Dealer Detail
				DealerDTLResponseModel dealerDtl = fetchDealerDTLByDealerId(authorizationHeader,
						purchaseReturnEntity.getDealerId());
				if (dealerDtl != null) {
					// fetch Main Branch Detail
					BranchDTLResponseModel branchDTLResponseModel = fetchMainBranchDtlByDealerId(authorizationHeader,
							purchaseReturnEntity.getDealerId());
					if (branchDTLResponseModel != null) {

						if ((salesMachinePurchaseReturnDtlList == null || salesMachinePurchaseReturnDtlList.isEmpty())
								&& (salesMachinePurchaseReturnImplList == null
										|| salesMachinePurchaseReturnImplList.isEmpty())) {
							logger.error(this.getClass().getName(), "At-least One Line Item Is Required");
							responseModel.setMsg("At-least One Line Item Is Required");
							isSuccess = false;
						}
						if (isSuccess) {
							if (salesMachinePurchaseReturnDtlList != null
									&& !salesMachinePurchaseReturnDtlList.isEmpty()) {
								for (SalesMachinePurchaseReturnDtlEntity dtlEntity : salesMachinePurchaseReturnDtlList) {
									if (!dtlEntity.isDeleted()) {
										query = session.createNativeQuery(sqlQuery)
												.addEntity(SalesMachineInventoryLedgerEntity.class);
										query.setParameter("grnId", purchaseReturnEntity.getGrnId());
										query.setParameter("vinId", dtlEntity.getVinId());
										query.setParameter("branchId", branchDTLResponseModel.getBranchId());
										SalesMachineInventoryLedgerEntity inventoryLedgerEntity = (SalesMachineInventoryLedgerEntity) query
												.uniqueResult();
										if (inventoryLedgerEntity == null) {
											logger.error(this.getClass().getName(),
													"inventoryLedgerEntity is null/Empty for "
															+ purchaseReturnEntity.getGrnId() + " : "
															+ dtlEntity.getVinId() + " : "
															+ branchDTLResponseModel.getBranchId());
											isSuccess = false;
											break;
										} else {
											if (inventoryLedgerEntity.isPurchaseReturnFlag()) {
												logger.error(this.getClass().getName(),
														"isPurchaseReturnFlag is already true for "
																+ purchaseReturnEntity.getGrnId() + " : "
																+ dtlEntity.getVinId() + " : "
																+ branchDTLResponseModel.getBranchId());
												isSuccess = false;
												break;
											}
											inventoryLedgerEntity.setPurchaseReturnFlag(true);
											inventoryLedgerEntity.setModifiedBy(userId);
											inventoryLedgerEntity.setModifiedDate(currDate);
											session.merge(inventoryLedgerEntity);
										}

										dtlEntity.setSalesMachinePurchaseReturn(purchaseReturnEntity);
									}
								}
							}
						}
						if (isSuccess) {
							if (salesMachinePurchaseReturnImplList != null
									&& !salesMachinePurchaseReturnImplList.isEmpty()) {
								for (SalesMachinePurchaseReturnImplDtlEntity implDtlEntity : salesMachinePurchaseReturnImplList) {
									if (!implDtlEntity.isDeleted()) {
										sqlQuery = "Select * from SA_MACHINE_ITEM_INV (nolock) where branch_id =:branchId and machine_item_id =:machineItemId";
										query = session.createNativeQuery(sqlQuery)
												.addEntity(SalesMachineItemInvEntity.class);
										query.setParameter("branchId", branchDTLResponseModel.getBranchId());
										query.setParameter("machineItemId", implDtlEntity.getMachineItemId());
										SalesMachineItemInvEntity itemInvEntity = (SalesMachineItemInvEntity) query
												.uniqueResult();
										if (itemInvEntity == null) {
											logger.error(this.getClass().getName(),
													"implDtlEntity is null/Empty for "
															+ branchDTLResponseModel.getBranchId() + " : "
															+ implDtlEntity.getMachineItemId());
											isSuccess = false;
											break;
										} else {
											Integer blockQty = itemInvEntity.getBlockedQty();
											Integer returnQty = implDtlEntity.getReceiptQty();
											if (blockQty == null) {
												blockQty = 0;
											}
											blockQty = blockQty + returnQty;
											itemInvEntity.setBlockedQty(blockQty);

											Integer stockQty = itemInvEntity.getStockQty();
											if (stockQty == null) {
												logger.error(this.getClass().getName(),
														"Sock Qty is 0/null for " + branchDTLResponseModel.getBranchId()
																+ " : " + implDtlEntity.getMachineItemId());
												isSuccess = false;
												break;
											}
											Integer netStockQty = stockQty - returnQty;
											itemInvEntity.setNetStockQty(netStockQty);
											itemInvEntity.setModifiedBy(userId);
											itemInvEntity.setModifiedDate(currDate);
											session.merge(itemInvEntity);

											// update return qty in Grn Detail Table
											sqlQuery = "Update SA_MACHINE_GRN_ITEM_DTL Set RETURN_QTY =:returnQty "
													+ " where grn_item_id =:grnItemId and grn_id =:grnId and machine_item_id =:machineItemId";
											query = session.createNativeQuery(sqlQuery);
											query.setParameter("returnQty", blockQty);
											query.setParameter("grnItemId", implDtlEntity.getGrnItemDtlId());
											query.setParameter("grnId", purchaseReturnEntity.getGrnId());
											query.setParameter("machineItemId", implDtlEntity.getMachineItemId());
											int k = query.executeUpdate();
										}

										implDtlEntity.setSalesMachinePurchaseReturn(purchaseReturnEntity);
									}
								}
							}

							if (isSuccess) {
								prNumber = "DRA" + branchDTLResponseModel.getBranchCode() + currDate.getTime();
								purchaseReturnEntity.setPurchaseReturnNumber(prNumber);
								purchaseReturnEntity.setPurchaseReturnStatus(WebConstants.PENDING);
								purchaseReturnEntity.setGrossTotalReturnValue(requestModel.getGrossTotalValue());
								purchaseReturnEntity.setCreatedBy(userId);
								purchaseReturnEntity.setCreatedDate(currDate);
								
								 id=(BigInteger) session.save(purchaseReturnEntity);
								
								if(salesMachinePurchaseReturnDtlList !=null && salesMachinePurchaseReturnDtlList.size()>0) {
									for (SalesMachinePurchaseReturnDtlEntity dtlEntity : salesMachinePurchaseReturnDtlList) {
										dtlEntity.setPurchase_return_id(id);
										session.save(dtlEntity);
									}
								}
								if(salesMachinePurchaseReturnImplList !=null && salesMachinePurchaseReturnImplList.size()>0) {
									for (SalesMachinePurchaseReturnImplDtlEntity dtlEntity : salesMachinePurchaseReturnImplList) {
										dtlEntity.setPurchase_return_id(id);
										session.save(dtlEntity);
									}
								}
								if(salesMachinePurchaseReturnAppList !=null && salesMachinePurchaseReturnAppList.size()>0) {
									for (SalesMachinePurchaseReturnAppEntity dtlEntity : salesMachinePurchaseReturnAppList) {
										dtlEntity.setPurchase_return_id(id);
										session.save(dtlEntity);
									}
								}
								
								
								
								
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
				mapData = saveIntoApproval(session, userId, purchaseReturnEntity,id);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					transaction.commit();
				} else {
					isSuccess = false;
					if (responseModel.getMsg() == null) {
						responseModel.setMsg("Error While Updating Machine Purchase Return Approval Hier.");
					}
				}
			}
		} catch (SQLGrammarException ex) {
			ex.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
			
		} catch (HibernateException ex) {
			ex.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (isSuccess) {
				mapData = fetchMachinePRNoByPRId(session, purchaseReturnEntity.getPurchaseReturnId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setPurchaseReturnId(purchaseReturnEntity.getPurchaseReturnId());
					responseModel.setPurchaseReturnNumber((String) mapData.get("prNumber"));
					responseModel.setMsg("Machine Purchase Return Number Created Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					try {
						
						updatePurchaseReturnMail(userCode, WebConstants.PURCHASE_RETURN,
								purchaseReturnEntity.getPurchaseReturnId()).subscribe(e -> {
									logger.info(e.toString());
								});
					} catch (Exception exp) {
						logger.error(this.getClass().getName(), exp);
					}
				} else {
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				}
			} else {
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
		private Mono<Map<String, Object>> updatePurchaseReturnMail(String userCode, String eventName, BigInteger enqHDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		BigInteger mailItemId = null;
		String sqlQuery = "exec [SP_MAIL_SA_PUR_RET_MAIL] :usercode , :event_name, :enqHDRId, :includeInactive";
		mapData.put("ERROR", "ERROR WHILE INSERTING PURCHASE RETURN MAIL TRIGGERS..");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("usercode ", userCode);
			query.setParameter("event_name", eventName);
			query.setParameter("refId", enqHDRId);
			query.setParameter("includeInactive", "N");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String msg = null;
				for (Object object : data) {
					Map row = (Map) object;
					msg = (String) row.get("msg");
					mailItemId = (BigInteger) row.get("mailItemId");
				}
				mapData.put("msg", msg);
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING PURCHASE RETURN MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING PURCHASE RETURN MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (mailItemId != null && mailItemId.compareTo(BigInteger.ZERO) > 0) {
				PublishModel publishModel = new PublishModel();
				publishModel.setId(mailItemId);
				//publishModel.setTopic(senderfollowupEnqTopicExchange.getName());
				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					// Simulate a long-running Job
					try {
		//				rabbitTemplate.convertAndSend(senderfollowupEnqTopicExchange.getName(), routingKey,
		//						commonUtils.objToJson(publishModel).toString());
		//				logger.info("Published message for followup enquiry '{}'", publishModel.toString());
		
					} catch (Exception exp) {
						logger.error(this.getClass().getName(), exp);
					}
					System.out.println("I'll run in a separate thread than the main thread.");
				});
			}
		}
		return Mono.just(mapData);
		}

	
	
	
	@SuppressWarnings({ "rawtypes" })
	private Map<String, Object> saveIntoApproval(Session session, BigInteger userId,
			SalesMachinePurchaseReturnEntity purchaseReturnEntity,BigInteger id) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("ERROR", "ERROR WHILE INSERTING INTO MACHINE PURCHASE RETURN TABLE.");
		try {
			List data = machinePOCommonDao.fetchApprovalData(session, "SA_MACHINE_PURCHASE_RET_APPROVAL");
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					SalesMachinePurchaseReturnAppEntity approvalEntity = new SalesMachinePurchaseReturnAppEntity();
					approvalEntity.setPurchase_return_id(id);
					approvalEntity.setSalesMachinePurchaseReturn(purchaseReturnEntity);
					approvalEntity.setApproverLevelSeq((Integer) row.get("approver_level_seq"));
					approvalEntity.setApprovalStatus((String) row.get("approvalStatus"));
					approvalEntity.setDesignationLevelId((Integer) row.get("designation_level_id"));
					approvalEntity.setGrpSeqNo((Integer) row.get("grp_seq_no"));
					Character isFinalApprovalStatus = (Character) row.get("isFinalApprovalStatus");
					if (isFinalApprovalStatus != null && isFinalApprovalStatus.toString().equals("Y")) {
						approvalEntity.setIsFinalApprovalStatus('Y');
					} else {
						approvalEntity.setIsFinalApprovalStatus('N');
					}
					approvalEntity.setRejectedFlag('N');
					approvalEntity.setHoUserId(null);

					session.save(approvalEntity);
				}
			}
			mapData.put("SUCCESS", "Inserted Into Machine PO Approval Table.");
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchMachinePRNoByPRId(Session session, BigInteger prHdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select purchase_return_number from SA_MACHINE_PURCH_RET (nolock) pr where pr.purchase_return_id =:prHdrId";
		mapData.put("ERROR", "Machine Purchase return Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("prHdrId", prHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String purchase_return_number = null;
				for (Object object : data) {
					Map row = (Map) object;
					purchase_return_number = (String) row.get("purchase_return_number");
				}
				mapData.put("prNumber", purchase_return_number);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE PURCHASE RETURN DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE PURCHASE RETURN DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
	
	
	public Resource loadFileAsResource(String fileName, String docPath, Long id,String downloadPath) throws MalformedURLException {
		
		
		if(id != null) {
			downloadPath = downloadPath  + id ;
		}
	
	Path path = Paths.get(downloadPath).toAbsolutePath().normalize();
//	System.out.println(this.fileStorageLocation);
	Path filePath = path.resolve(fileName).normalize();
	Resource resource = null;
	try {
		resource = new UrlResource(filePath.toUri());
		if (resource.exists()) {
			return resource;
		}else {
			throw new RecordNotFoundException("File not found " + fileName);
		}
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
		throw new MalformedURLException("File not found " + fileName);
	}
	//return resource;
	

	}	
}
