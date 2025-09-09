/**
 * 
 */
package com.hitech.dms.web.dao.grn.create;

import java.math.BigInteger;
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
import com.hitech.dms.web.entity.sales.grn.SalesMachineGrnDtlEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineGrnHDREntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineGrnImplDtlEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineInventoryLedgerEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineItemInvEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineItemInvLedgerEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineItemInvLedgerPEntity;
import com.hitech.dms.web.entity.sales.grn.SalesMachineItemInvPEntity;
import com.hitech.dms.web.entity.sales.purchase.ret.SalesMachineVinMstEntity;
import com.hitech.dms.web.model.branchdtl.request.BranchDTLRequestModel;
import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.grn.create.request.SalesGrnCreateRequestModel;
import com.hitech.dms.web.model.grn.create.response.SalesGrnCreateResponseModel;
import com.hitech.dms.web.model.grn.type.response.GrnTypeResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class SalesGrnCreateDaoImpl implements SalesGrnCreateDao {
	private static final Logger logger = LoggerFactory.getLogger(SalesGrnCreateDaoImpl.class);

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
			requestModel.setIsFor("MACHINEPO");
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

	@SuppressWarnings("deprecation")
	public SalesGrnCreateResponseModel createGrn(String authorizationHeader, String userCode,
			SalesGrnCreateRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("createMachinePO invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		SalesGrnCreateResponseModel responseModel = new SalesGrnCreateResponseModel();
		SalesMachineGrnHDREntity grnHDREntity = null;
		GrnTypeResponseModel grnTypeModel = null;
		boolean isSuccess = true;
		String grnNumber = null;
		String sqlQuery = "Select * from SA_MACHINE_VIN_MASTER (nolock) where chassis_no =:chassisNo";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			grnHDREntity = mapper.map(requestModel, SalesMachineGrnHDREntity.class, "SalesGrnMapId");

			logger.debug(grnHDREntity.toString());

			List<SalesMachineGrnDtlEntity> salesMachineGrnDtlList = grnHDREntity.getSalesMachineGrnDtlList();
			List<SalesMachineGrnImplDtlEntity> salesMachineGrnImplDtlList = grnHDREntity
					.getSalesMachineGrnImplDtlList();

			BigInteger userId = null;
			Date currDate = new Date();
			mapData = machinePOCommonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				// fetch Dealer Detail
				DealerDTLResponseModel dealerDtl = fetchDealerDTLByDealerId(authorizationHeader,
						grnHDREntity.getDealerId());
				if (dealerDtl != null) {
					// fetch Main Branch Detail
					BranchDTLResponseModel branchDTLResponseModel = fetchMainBranchDtlByDealerId(authorizationHeader,
							grnHDREntity.getDealerId());
					if (branchDTLResponseModel != null) {
						// validate / fetch Grn Type
						grnTypeModel = machinePOCommonDao.fetchGrnTypeDtl(grnHDREntity.getGrnTypeId());
						if (grnTypeModel != null) {
							if (grnTypeModel.getGrnTypeCode().equals(WebConstants.GRNTYPE_OEM)
									&& requestModel.getErpInvoiceHdrId() == null) {
								grnHDREntity.setErpInvoiceHdrId(requestModel.getInvoiceId());
								;
							} else if (grnTypeModel.getGrnTypeCode().equals(WebConstants.GRNTYPE_PO)
									&& requestModel.getCoDealerInvoiceId() == null) {
								grnHDREntity.setCoDealerInvoiceId(requestModel.getInvoiceId());
							} else if (grnTypeModel.getGrnTypeCode().equals(WebConstants.GRNTYPE_PR)
									&& requestModel.getPurchaseReturnInvId() == null) {
								grnHDREntity.setPurchaseReturnInvId(requestModel.getInvoiceId());
							}
							// validate and insert chassis Nos.
							if(salesMachineGrnDtlList !=null && salesMachineGrnDtlList.size()>0) {
								Integer bToCFlag = machinePOCommonDao.fetchB2CFlag(grnHDREntity.getInvoiceNumber());
								for (SalesMachineGrnDtlEntity machineGrnDtlEntity : salesMachineGrnDtlList) {
									query = session.createNativeQuery(sqlQuery).addEntity(SalesMachineVinMstEntity.class);
									query.setParameter("chassisNo", machineGrnDtlEntity.getChassisNo());
									SalesMachineVinMstEntity vinMstEntity = (SalesMachineVinMstEntity) query.uniqueResult();
									if (vinMstEntity == null) {
										// insert into vin table
										vinMstEntity = new SalesMachineVinMstEntity();
										vinMstEntity.setChassisNo(machineGrnDtlEntity.getChassisNo());
										vinMstEntity.setEngineNo(machineGrnDtlEntity.getEngineNo());
										vinMstEntity.setMachineItemId(machineGrnDtlEntity.getMachineItemId());
										vinMstEntity.setMfgInvoiceDate(grnHDREntity.getInvoiceDate());
										vinMstEntity.setMfgInvoiceNo(grnHDREntity.getInvoiceNumber());
										vinMstEntity.setSellingDealerCode(dealerDtl.getDealerCode());
										vinMstEntity.setUnitPrice(machineGrnDtlEntity.getUnitPrice());
										vinMstEntity.setVinNo(machineGrnDtlEntity.getVinNo());
										vinMstEntity.setPlantCode(machineGrnDtlEntity.getPlantCode());
										vinMstEntity.setCreatedBy(userId);
										vinMstEntity.setCreatedDate(currDate);
										vinMstEntity.setBToCFlag(bToCFlag);;

										session.save(vinMstEntity);
									}
									machineGrnDtlEntity.setVinId(vinMstEntity.getVinId());
									machineGrnDtlEntity.setSalesMachineGrnHDR(grnHDREntity);
								}
							}
							
							
							
							if(salesMachineGrnImplDtlList !=null && salesMachineGrnImplDtlList.size()>0) {
								for (SalesMachineGrnImplDtlEntity grnItemDtlEntity : salesMachineGrnImplDtlList) {
									grnItemDtlEntity.setSalesMachineGrnHDR(grnHDREntity);
								}
							}
							
							
							// insert into GRN Table
							grnNumber = "GRN" + dealerDtl.getDealerCode() + currDate.getTime();
							grnHDREntity.setGrnNumber(grnNumber);
							if (grnHDREntity.getGrnDate() == null) {
								grnHDREntity.setGrnDate(currDate);
							}
							grnHDREntity.setGrnStatus("SUBMITTED");
							grnHDREntity.setCreatedBy(userId);
							grnHDREntity.setCreatedDate(currDate);

							session.save(grnHDREntity);

							// insert into Inventory Ledger
							
							if(salesMachineGrnDtlList !=null && salesMachineGrnDtlList.size()>0) {
								for (SalesMachineGrnDtlEntity machineGrnDtlEntity : salesMachineGrnDtlList) {
									SalesMachineInventoryLedgerEntity ledgerEntity = new SalesMachineInventoryLedgerEntity();
									ledgerEntity.setAllotFlag(false);
									ledgerEntity.setBranchId(branchDTLResponseModel.getBranchId());
									ledgerEntity.setGrnId(grnHDREntity.getGrnId());
									ledgerEntity.setInDate(currDate);
									ledgerEntity.setInDocNo(grnHDREntity.getGrnNumber());
									ledgerEntity.setInDocType(WebConstants.GRN);
									ledgerEntity.setStockType(WebConstants.FRESH);
									ledgerEntity.setUnitPrice(machineGrnDtlEntity.getUnitPrice());
									ledgerEntity.setVinId(machineGrnDtlEntity.getVinId());
									ledgerEntity.setPurchaseReturnFlag(false);

									ledgerEntity.setCreatedBy(userId);
									ledgerEntity.setCreatedDate(currDate);

									session.save(ledgerEntity);
								}
							}
							
							

							// insert into item ledger
							
							if(salesMachineGrnImplDtlList !=null && salesMachineGrnImplDtlList.size()>0) {
								for (SalesMachineGrnImplDtlEntity grnItemDtlEntity : salesMachineGrnImplDtlList) {
									
									
									// check Item is there in SA_MACHINE_ITEM_INV table or not
									sqlQuery = "select * from SA_MACHINE_ITEM_INV (nolock) where machine_item_id =:itemId and branch_id =:branchId";
									query = session.createNativeQuery(sqlQuery).addEntity(SalesMachineItemInvEntity.class);
									query.setParameter("itemId", grnItemDtlEntity.getMachineItemId());
									query.setParameter("branchId", branchDTLResponseModel.getBranchId());
									SalesMachineItemInvEntity itemInvEntity = (SalesMachineItemInvEntity) query
											.uniqueResult();
									if (itemInvEntity != null) {

										Integer stockQty = itemInvEntity.getStockQty();
										if (stockQty == null) {
											stockQty = 0;
										}
										stockQty = stockQty + grnItemDtlEntity.getReceiptQty();
										Integer netStockQty = itemInvEntity.getNetStockQty();
										if (netStockQty == null) {
											netStockQty = 0;
										}
										netStockQty = netStockQty + grnItemDtlEntity.getReceiptQty();
										itemInvEntity.setStockQty(stockQty);
										itemInvEntity.setNetStockQty(netStockQty);

										itemInvEntity.setModifiedBy(userId);
										itemInvEntity.setModifiedDate(currDate);

										session.merge(itemInvEntity);
									} else {
										itemInvEntity = new SalesMachineItemInvEntity();
										SalesMachineItemInvPEntity salesMachineItemInvP = new SalesMachineItemInvPEntity();
										salesMachineItemInvP.setBranchId(branchDTLResponseModel.getBranchId());
										salesMachineItemInvP.setMachineItemId(grnItemDtlEntity.getMachineItemId());

										itemInvEntity.setSalesMachineItemInvP(salesMachineItemInvP);
										itemInvEntity.setBlockedQty(0);
										itemInvEntity.setStockQty(grnItemDtlEntity.getReceiptQty());
										itemInvEntity.setNetStockQty(grnItemDtlEntity.getReceiptQty());
										itemInvEntity.setCreatedBy(userId);
										itemInvEntity.setCreatedDate(currDate);

										session.save(itemInvEntity);
									}
									//session.flush();

									// Also insert into SA_MACHINE_ITEM_INV_LEDGER Table
									SalesMachineItemInvLedgerEntity itemInvLedgerEntity = new SalesMachineItemInvLedgerEntity();
									//SalesMachineItemInvLedgerPEntity salesMachineItemInvLedgerP = new SalesMachineItemInvLedgerPEntity();

									/*
									 * salesMachineItemInvLedgerP.setBranchId(branchDTLResponseModel.getBranchId());
									 * salesMachineItemInvLedgerP.setMachineItemId(grnItemDtlEntity.getMachineItemId
									 * ());
									 * salesMachineItemInvLedgerP.setTransactionNo(grnHDREntity.getGrnNumber());
									 * salesMachineItemInvLedgerP.setTransactionDate(currDate);
									 */
									if(branchDTLResponseModel.getBranchId() !=null) {
										itemInvLedgerEntity.setBranchId(branchDTLResponseModel.getBranchId());
									}
									
									if(grnItemDtlEntity.getMachineItemId() !=null) {
										itemInvLedgerEntity.setMachineItemId(grnItemDtlEntity.getMachineItemId());
									}
									
									
									if(grnHDREntity.getGrnNumber() !=null) {
										itemInvLedgerEntity.setTransactionNo(grnHDREntity.getGrnNumber());
									}
									
									
									
									itemInvLedgerEntity.setTransactionDate(currDate);
									
									if(grnHDREntity.getGrnId() !=null) {
										itemInvLedgerEntity.setGrnId(grnHDREntity.getGrnId());
									}
									if(grnItemDtlEntity.getVinId() !=null) {
										itemInvLedgerEntity.setVinId(grnItemDtlEntity.getVinId());
									}
									
									//itemInvLedgerEntity.setSalesMachineItemInvLedgerP(salesMachineItemInvLedgerP);
									itemInvLedgerEntity.setInward(grnItemDtlEntity.getReceiptQty());
									itemInvLedgerEntity.setOutward(0);
									itemInvLedgerEntity.setTransactionDesc("THROUGH GRN");
									itemInvLedgerEntity.setModifiedBy(userId);
									itemInvLedgerEntity.setModifiedDate(currDate);
									itemInvLedgerEntity.setAllotFlag(0);
									itemInvLedgerEntity.setAllotQnty(0);
									if(itemInvLedgerEntity !=null) {
										session.save(itemInvLedgerEntity);
									}
									else {
										logger.info("object is empty "+itemInvLedgerEntity.toString());
									}
									
								}
							}
							
							

						} else {
							// Grn Type Not Found
							isSuccess = false;
							responseModel.setMsg("Grn Type Not Found.");
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
				transaction.commit();;
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
				// insert / update in detail tables based on grn Type
				mapData = fetchMachineGRNNoByGRNId(session, grnHDREntity.getGrnId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setGrnId(grnHDREntity.getGrnId());
					responseModel.setGrnNumber((String) mapData.get("grnNumber"));
					responseModel.setMsg("Machine GRN Number Created Successfully.");
					mapData = updateInvoiceDtlByGRNId(session, userCode, grnHDREntity.getGrnId(), grnTypeModel.getGrnTypeCode());
					if (mapData != null && mapData.get("status") != null) {
						logger.info(mapData.toString());
					}
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				}
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> updateInvoiceDtlByGRNId(Session session, String userCode, BigInteger grnHdrId, String grnTypeCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "exec [SP_SA_GRN_Update_INV_DTL] :userCode, :grnHdrId, :grnTypeCode";
		mapData.put("ERROR", "Machine INVOICE Details Not Found  ON GRN.");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("grnHdrId", grnHdrId);
			query.setParameter("grnTypeCode", grnTypeCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String msg = null;
				String status = null;
				for (Object object : data) {
					Map row = (Map) object;
					msg = (String) row.get("msg");
					status = (String) row.get("status");
				}
				mapData.put("msg", msg);
				mapData.put("status", status);
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE UPDATING MACHINE INVOICE DETAILS ON GRN.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE INVOICE DETAILS ON GRN.");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchMachineGRNNoByGRNId(Session session, BigInteger grnHdrId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select grn_number from SA_MACHINE_GRN (nolock) grn where grn.grn_id =:grnHdrId";
		mapData.put("ERROR", "Machine GRN Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("grnHdrId", grnHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String grn_number = null;
				for (Object object : data) {
					Map row = (Map) object;
					grn_number = (String) row.get("grn_number");
				}
				mapData.put("grnNumber", grn_number);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE GRN DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE GRN DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
}
