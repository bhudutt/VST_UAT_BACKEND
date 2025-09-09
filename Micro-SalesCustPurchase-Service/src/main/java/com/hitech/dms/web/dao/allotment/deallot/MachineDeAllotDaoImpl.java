/**
 * 
 */
package com.hitech.dms.web.dao.allotment.deallot;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.hitech.dms.web.entity.allotment.MachineAllotmentDtlEntity;
import com.hitech.dms.web.entity.allotment.MachineAllotmentEntity;
import com.hitech.dms.web.entity.ledger.SalesMachineInventoryLedgerEntity;
import com.hitech.dms.web.entity.ledger.SalesMachineItemInvLedgerEntity;
import com.hitech.dms.web.model.allot.deallot.request.MachineDeAllotRequestModel;
import com.hitech.dms.web.model.allot.deallot.response.MachineDeAllotResponseModel;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MachineDeAllotDaoImpl implements MachineDeAllotDao {
	private static final Logger logger = LoggerFactory.getLogger(MachineDeAllotDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
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

	@SuppressWarnings("deprecation")
	public MachineDeAllotResponseModel deAllotMachine(String authorizationHeader, String userCode,
			MachineDeAllotRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("create Allotment invoked.." + userCode);
		}
		logger.debug(requestModel.toString());
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		MachineDeAllotResponseModel responseModel = new MachineDeAllotResponseModel();
		boolean isSuccess = true;
		MachineAllotmentEntity allotmentEntity = null;
		String sqlQuery = "Select * from SA_MACHINE_ALLOTMENT (nolock) pr where pr.machine_allotment_id =:allotHdrId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			Date currDate = new Date();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				query = session.createNativeQuery(sqlQuery).addEntity(MachineAllotmentEntity.class);
				query.setParameter("allotHdrId", requestModel.getMachineAllotmentId());
				allotmentEntity = (MachineAllotmentEntity) query.uniqueResult();
				if (allotmentEntity != null) {
					List<MachineAllotmentDtlEntity> machineAllotDtlList = allotmentEntity.getEnqMachineDtlList();

					allotmentEntity.setAllotStatus(WebConstants.DEALLOTTED);
					allotmentEntity.setDeAllotDate(requestModel.getDeAllotDate());
					allotmentEntity.setDeAllotedById(userId);
					allotmentEntity.setDeAllotFlag(true);
					allotmentEntity.setDeAllotReason(requestModel.getDeAllotReason());
					allotmentEntity.setModifiedBy(userId);
					allotmentEntity.setModifiedDate(currDate);

					session.merge(allotmentEntity);

					for (MachineAllotmentDtlEntity dtlEntity : machineAllotDtlList) {
						
						
						System.out.println("prodcut "+dtlEntity.getProductGroup());
						
						if(dtlEntity.getProductGroup().equalsIgnoreCase("Implement") || dtlEntity.getProductGroup().equalsIgnoreCase("Accessory")) {
							
							sqlQuery = "Select TOP 1 * from SA_MACHINE_ITEM_INV_LEDGER (nolock) where machine_inventory_id =:machineInventory and branch_id =:branchId "
									+ "   order by machine_item_id desc ";
							query = session.createNativeQuery(sqlQuery).addEntity(SalesMachineItemInvLedgerEntity.class);
							query.setParameter("machineInventory", dtlEntity.getMachineInventoryId());
							query.setParameter("branchId", allotmentEntity.getBranchId());
							SalesMachineItemInvLedgerEntity inventoryLedgerEntity = (SalesMachineItemInvLedgerEntity) query
									.uniqueResult();
							if (inventoryLedgerEntity == null) {
								logger.error(this.getClass().getName(),
										"While De-Allotting InventoryLedgerEntity is null/Empty for "
												+ dtlEntity.getMachineInventoryId() + " : "
												+ allotmentEntity.getBranchId());
								responseModel.setMsg("Stock Not Found. Kindly Contact Your Admnistrator.");
								isSuccess = false;
								break;
							} else {
								
								BigInteger allotQnty=dtlEntity.getAllotQnty();
								Integer inward = inventoryLedgerEntity.getInward();
								Integer outward = inventoryLedgerEntity.getOutward();
								BigInteger alreadeyAllotedQuantity = inventoryLedgerEntity.getAllotQuantity();
								
								//Integer available=inward-outward;
								
								BigInteger toalAvail=alreadeyAllotedQuantity.subtract(allotQnty);
								
								
								inventoryLedgerEntity.setAllotQuantity(toalAvail);
								inventoryLedgerEntity.setAllotFlag(false);
								inventoryLedgerEntity.setModifiedBy(userId);
								inventoryLedgerEntity.setModifiedDate(currDate);
								session.merge(inventoryLedgerEntity);
							}
						}else {
							sqlQuery = "Select TOP 1 * from SA_MACHINE_INVENTORY_LEDGER (nolock) where machine_inventory_id =:machineInventory and branch_id =:branchId "
									+ " and OUT_DOC_TYPE IS NULL and OUT_DOC_NUMBR IS NULL and OUT_DATE IS NULL and allot_flag = 1 order by machine_inventory_id desc ";
							query = session.createNativeQuery(sqlQuery).addEntity(SalesMachineInventoryLedgerEntity.class);
							query.setParameter("machineInventory", dtlEntity.getMachineInventoryId());
							query.setParameter("branchId", allotmentEntity.getBranchId());
							SalesMachineInventoryLedgerEntity inventoryLedgerEntity = (SalesMachineInventoryLedgerEntity) query
									.uniqueResult();
							if (inventoryLedgerEntity == null) {
								logger.error(this.getClass().getName(),
										"While De-Allotting InventoryLedgerEntity is null/Empty for "
												+ dtlEntity.getMachineInventoryId() + " : "
												+ allotmentEntity.getBranchId());
								responseModel.setMsg("Stock Not Found. Kindly Contact Your Admnistrator.");
								isSuccess = false;
								break;
							} else {
								inventoryLedgerEntity.setAllotFlag(false);
								inventoryLedgerEntity.setModifiedBy(userId);
								inventoryLedgerEntity.setModifiedDate(currDate);
								session.merge(inventoryLedgerEntity);
							}
							
							
								if(true) {
									mapData = commonDao.updateEnquiryStatus(session, userCode, userId, currDate,
											allotmentEntity.getEnquiryId(), WebConstants.OPEN);
									if (mapData != null && mapData.get("SUCCESS") != null) {
										logger.info("Enquiry Status has been updated to De-Allotted.");
									} else {
										logger.error(this.getClass().getName(), "Error While Updating Enquiry Status.");
										responseModel.setMsg(
												"Error While Updating Enquiry Status. Please Contact Your System Administrator.");
										isSuccess = false;
									}
								}
						}
						
						
						
					}
					
				} else {
					logger.error(this.getClass().getName(), "Allotment Number Not Found.");
					responseModel.setMsg("Allotment Number Not Found.");
					isSuccess = false;
				}
			} else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
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
				responseModel.setMachineAllotmentId(allotmentEntity.getMachineAllotmentId());
				responseModel.setAllotNumber(allotmentEntity.getAllotNumber());
				responseModel.setMsg("Machine Allotment Number De-Allotted Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While De-Allotting Machine Allotment.");
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}
}
