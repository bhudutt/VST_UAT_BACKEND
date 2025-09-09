/**
 * 
 */
package com.hitech.dms.web.dao.machinepo.update;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.machinepo.common.MachinePOCommonDao;
import com.hitech.dms.web.entity.machinepo.MachinePOApprovalEntity;
import com.hitech.dms.web.entity.machinepo.MachinePODtlEntity;
import com.hitech.dms.web.entity.machinepo.MachinePOHdrEntity;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.machinepo.orderTo.response.MachinePOOrderToResponseModel;
import com.hitech.dms.web.model.machinepo.update.request.MachinePOUpdateRequestModel;
import com.hitech.dms.web.model.machinepo.update.response.MachinePOUpdateResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MachinePOUpdateDaoImpl implements MachinePOUpdateDao {
	private static final Logger logger = LoggerFactory.getLogger(MachinePOUpdateDaoImpl.class);

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

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public MachinePOUpdateResponseModel updateMachinePO(String authorizationHeader, String userCode,
			MachinePOUpdateRequestModel requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateMachinePO invked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		MachinePOHdrEntity poHdrEntity = null;
		MachinePOUpdateResponseModel responseModel = new MachinePOUpdateResponseModel();
		responseModel.setPoHdrId(requestModel.getPoHdrId());
		responseModel.setPoNumber(requestModel.getPoNumber());
		String action = requestModel.getAction();
		boolean isSubmit = false;
		boolean isSuccess = true;
		try {
			if (action == null || !action.equals(WebConstants.SAVE)) {
				isSubmit = true;
			}
			logger.debug("PO DRAFT MODE : ", isSubmit);
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			poHdrEntity = mapper.map(requestModel, MachinePOHdrEntity.class, "MachinePOUpdateMapId");
			logger.debug(poHdrEntity.toString());
			BigInteger userId = null;
			BigInteger dlrEmpId = null;
			BigInteger hoUserId = null;
			String dealerType = null;
			MachinePOOrderToResponseModel poToTypeResponseModel = null;
			MachinePOHdrEntity poHdrDBEntity = null;
			mapData = machinePOCommonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				dlrEmpId = (BigInteger) mapData.get("dlrEmpId");
				hoUserId = (BigInteger) mapData.get("hoUserId");
				String sqlQuery = "Select * from SA_PO_HDR (nolock) SPH where po_hdr_id =:poHdrId";
				query = session.createNativeQuery(sqlQuery).addEntity(MachinePOHdrEntity.class);
				query.setParameter("poHdrId", poHdrEntity.getPoHdrId());
				poHdrDBEntity = (MachinePOHdrEntity) query.uniqueResult();
				if (poHdrDBEntity != null) {
					if (!poHdrDBEntity.getPoStatus().equals(WebConstants.DRAFT) && dlrEmpId != null) {
						// dealer can not edit the Machine Po
						isSuccess = false;
						responseModel.setMsg("Machine PO Can not be Edited.");
					}
					if (isSuccess) {
						// fetch Dealer Detail
						DealerDTLResponseModel dealerDtl = fetchDealerDTLByDealerId(authorizationHeader,
								poHdrDBEntity.getDealerId());
						if (dealerDtl != null) {
							String dealerCode = null;
							dealerCode = dealerDtl.getDealerCode();
							dealerType = dealerDtl.getDealerType();
							// current date
							Date currDate = new Date();
							if (!isSubmit) {
								// set draft mode true
								poHdrDBEntity.setDraftMode(true);
								// If PO is SAVE then PO Status will be DRAFT.
								poHdrDBEntity.setPoStatus(WebConstants.DRAFT);
							} else {
								poHdrDBEntity.setDraftMode(false);
								// If PO is SUBMIT then PO status will be RELEASE. In case of
								// Co-Dealer/CO-Distributor its status will be Waiting for Approval
								poHdrDBEntity.setPoStatus(WebConstants.RELEASE);
								poHdrDBEntity.setPoReleasedDate(currDate);
								
								
								
							}
							// fetch PO Status
							// fetch PO TO Type Detail
							poToTypeResponseModel = machinePOCommonDao.fetchPOTOTypeDTL(session, userCode,
									poHdrDBEntity.getPoTypeId());
							if (poToTypeResponseModel != null) {
								if (isSubmit) {
									// In case of Co-Dealer/CO-Distributor its status will be Waiting for Approval
									if (poToTypeResponseModel.getTypeCode().equals(WebConstants.CO_DEALER)
											|| poToTypeResponseModel.getTypeCode()
													.equals(WebConstants.CO_DISTRIBUTOR)) {
										poHdrDBEntity.setPoStatus(WebConstants.WAITING_FOR_APPROVAL);
									}
								}
								if (dlrEmpId != null) {
									// If Logged in user is Dealer then in PO ON values will be VST, Co-Dealer and
									// Distributor.
									if (poToTypeResponseModel.getTypeCode().equals(WebConstants.VST)
											|| poToTypeResponseModel.getTypeCode().equals(WebConstants.CO_DEALER)
											|| poToTypeResponseModel.getTypeCode()
													.equals(WebConstants.CO_DISTRIBUTOR)) {

									} else {
										// PO TO Type ID not Matched.
										isSuccess = false;
									}
									// If Logged in user is Distributor then in PO ON values will be VST and
									// Co-Distributor.
									if (dealerType != null && dealerType.equalsIgnoreCase(WebConstants.DISTRIBUTOR)) {
										if (poToTypeResponseModel.getTypeCode().equals(WebConstants.VST)
												|| poToTypeResponseModel.getTypeCode()
														.equals(WebConstants.CO_DISTRIBUTOR)) {

										} else {
											// PO To Type ID not Matched.
											isSuccess = false;
											responseModel.setMsg("PO To Type ID not Matched.");
										}
									}
								}
								if (isSuccess) {
									// If PO ON is selected as VST then in Party Name automatically VST will Display
									// and RSO will be mandatory, user need to selected RSO from dropdown list
									if (poToTypeResponseModel.getTypeCode().equals(WebConstants.VST)) {
										if (poHdrEntity.getPoPlantId() == null) {
											// PO Plant Required
											isSuccess = false;
											responseModel.setMsg("PO Plant Required.");
										} else {
											poHdrDBEntity.setPoPlantId(poHdrEntity.getPoPlantId());
										}
									}
									if (isSuccess) {
										// data inserting/updating into PO Tables
										int totalQty = 0;
										poHdrDBEntity.setMachinePODtlList(poHdrEntity.getMachinePODtlList());
										for (MachinePODtlEntity machinePODtlEntity : poHdrDBEntity
												.getMachinePODtlList()) {
											totalQty = totalQty + machinePODtlEntity.getQuantity();
											machinePODtlEntity.setPendingQty(machinePODtlEntity.getQuantity());
											if (machinePODtlEntity.getInvoiceQty() > 0) {
												isSuccess = false;
												responseModel.setMsg(
														"Qty. Is Already Invoiced. Kindly Contact Your Administrator.");
												logger.info(this.getClass().getName(), machinePODtlEntity.toString());
												break;
											}
											machinePODtlEntity.setInvoiceQty(0);
											machinePODtlEntity.setMachinePOHdr(poHdrDBEntity);
										}

										if (isSuccess) {
											poHdrDBEntity.setTotalQty(totalQty);
											poHdrDBEntity.setModifiedBy(userId);
											poHdrDBEntity.setModifiedDate(currDate);
											
											poHdrDBEntity.setBasicAmount(requestModel.getBasicAmount());
											poHdrDBEntity.setTcsPercent(requestModel.getTcsPercent());
											poHdrDBEntity.setTcsValue(requestModel.getTcsValue());
											poHdrDBEntity.setTotalAmount(requestModel.getTotalAmount());
											poHdrDBEntity.setTotalGstAmount(requestModel.getTotalGstAmount());

											session.merge(poHdrDBEntity);
										}
									}
								}

							} else {
								// Machine PO To Type Not Found
								isSuccess = false;
								responseModel.setMsg("Machine PO To Type Not Found.");
							}
						} else {
							// Dealer Not Found
							isSuccess = false;
							responseModel.setMsg("Dealer Not Found.");
						}
					}
				} else {
					//
					isSuccess = false;
					responseModel.setMsg("Machine PO Number Not Found.");
				}
			} else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
				if (isSubmit) {
					if (poToTypeResponseModel.getTypeCode().equals(WebConstants.CO_DEALER)
							|| poToTypeResponseModel.getTypeCode().equals(WebConstants.CO_DISTRIBUTOR)) {
						mapData = saveIntoApproval(session, userId, null, poHdrDBEntity);
						if (mapData != null && mapData.get("SUCCESS") != null) {
						} else {
							isSuccess = false;
							responseModel.setMsg("Error While Updating Machine PO Approval Hier.");
						}
					}
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
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setMsg("Machine PO -" + requestModel.getPoNumber() + " Updated Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "rawtypes" })
	private Map<String, Object> saveIntoApproval(Session session, BigInteger userId, BigInteger hoUserId,
			MachinePOHdrEntity poHdrEntity) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("ERROR", "ERROR WHILE INSERTING INTO MACHINE PO TABLE.");
		try {
			if (poHdrEntity.getMachinePOApprovalList() == null || poHdrEntity.getMachinePOApprovalList().isEmpty()) {
				List data = machinePOCommonDao.fetchApprovalData(session, "SA_MACHINE_PO_APPROVAL");
				if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						Map row = (Map) object;
						MachinePOApprovalEntity approvalEntity = new MachinePOApprovalEntity();
						approvalEntity.setMachinePOHdr(poHdrEntity);
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
			} else {
				// no need to insert again in Approval table.
				logger.debug("No need to insert again in Approval Table.");
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

}
