/**
 * 
 */
package com.hitech.dms.web.dao.machinepo.create;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.config.publisher.PublishModel;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.machinepo.common.MachinePOCommonDao;
import com.hitech.dms.web.entity.machinepo.MachinePOApprovalEntity;
import com.hitech.dms.web.entity.machinepo.MachinePODtlEntity;
import com.hitech.dms.web.entity.machinepo.MachinePOHdrEntity;
import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;
import com.hitech.dms.web.model.machinepo.create.request.MachinePOCreateRequestModel;
import com.hitech.dms.web.model.machinepo.create.response.MachinePOCreateResponseModel;
import com.hitech.dms.web.model.machinepo.orderTo.response.MachinePOOrderToResponseModel;
import com.hitech.dms.web.service.client.CommonServiceClient;

import reactor.core.publisher.Mono;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MachinePOCreateDaoImpl implements MachinePOCreateDao {
	private static final Logger logger = LoggerFactory.getLogger(MachinePOCreateDaoImpl.class);

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
	public MachinePOCreateResponseModel createMachinePO(String authorizationHeader, String userCode,
			MachinePOCreateRequestModel requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("createMachinePO invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		MachinePOHdrEntity poHdrEntity = null;
		MachinePOCreateResponseModel responseModel = new MachinePOCreateResponseModel();
		String action = requestModel.getAction();
		boolean isSubmit = false;
		boolean isSuccess = true;
		String poTypeval="";
		try {
			if (action == null || !action.equals(WebConstants.SAVE)) {
				isSubmit = true;
			}
			logger.debug("PO DRAFT MODE : ", isSubmit);
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			poHdrEntity = mapper.map(requestModel, MachinePOHdrEntity.class, "MachinePOMapId");
			List<MachinePODtlEntity> machinePoDtlList = poHdrEntity.getMachinePODtlList().stream()
					.distinct().collect(Collectors.toList());
			if (machinePoDtlList != null) {
				poHdrEntity.setMachinePODtlList(machinePoDtlList);
			}
			logger.debug(poHdrEntity.toString());
			BigInteger userId = null;
			BigInteger dlrEmpId = null;
			BigInteger hoUserId = null;
			String dealerType = null;
			MachinePOOrderToResponseModel poToTypeResponseModel = null;
			mapData = machinePOCommonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				dlrEmpId = (BigInteger) mapData.get("dlrEmpId");
				hoUserId = (BigInteger) mapData.get("hoUserId");

				// fetch Dealer Detail
				DealerDTLResponseModel dealerDtl = fetchDealerDTLByDealerId(authorizationHeader,
						poHdrEntity.getDealerId());
				if (dealerDtl != null) {
					String dealerCode = null;
					dealerCode = dealerDtl.getDealerCode();
					dealerType = dealerDtl.getDealerType();
					// current date
					Date currDate = new Date();
					if (!isSubmit) {
						// set draft mode true
						poHdrEntity.setDraftMode(true);
						// If PO is SAVE then PO Status will be DRAFT.
						poHdrEntity.setPoStatus(WebConstants.DRAFT);
					} else {
						poHdrEntity.setDraftMode(false);
						// If PO is SUBMIT then PO status will be RELEASE. In case of
						// Co-Dealer/CO-Distributor its status will be Waiting for Approval
						poHdrEntity.setPoStatus(WebConstants.RELEASE);
						poHdrEntity.setPoReleasedDate(currDate);
					}
					// set Temp PO No.
					poHdrEntity.setPoNumber("DRA" + dealerCode + currDate.getTime());
					// fetch PO Status
					// fetch PO TO Type Detail
					poToTypeResponseModel = machinePOCommonDao.fetchPOTOTypeDTL(session, userCode,
							poHdrEntity.getPoTypeId());
					if (poToTypeResponseModel != null) {
						poTypeval=poToTypeResponseModel.getTypeCode();
						if (isSubmit) {
							// In case of Co-Dealer/CO-Distributor its status will be Waiting for Approval
							if (poToTypeResponseModel.getTypeCode().equalsIgnoreCase(WebConstants.CO_DEALER)
									|| poToTypeResponseModel.getTypeCode()
											.equalsIgnoreCase(WebConstants.CO_DISTRIBUTOR)) {
								poHdrEntity.setPoStatus(WebConstants.WAITING_FOR_APPROVAL);
							}
						}
						if (dlrEmpId != null) {
							// If Logged in user is Dealer then in PO ON values will be VST, Co-Dealer and
							// Distributor.
							if (poToTypeResponseModel.getTypeCode().equalsIgnoreCase(WebConstants.VST)
									|| poToTypeResponseModel.getTypeCode().equalsIgnoreCase(WebConstants.CO_DEALER)
									|| poToTypeResponseModel.getTypeCode().equalsIgnoreCase(WebConstants.DISTRIBUTOR)) {

							} else {
								// PO TO Type ID not Matched.
								isSuccess = false;
							}
							// If Logged in user is Distributor then in PO ON values will be VST and
							// Co-Distributor.
							if (dealerType != null && dealerType.equalsIgnoreCase(WebConstants.DISTRIBUTOR)) {
								if (poToTypeResponseModel.getTypeCode().equalsIgnoreCase(WebConstants.VST)
										|| poToTypeResponseModel.getTypeCode()
												.equalsIgnoreCase(WebConstants.CO_DISTRIBUTOR)) {

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
							if (poToTypeResponseModel.getTypeCode().equalsIgnoreCase(WebConstants.VST)) {
								if (poHdrEntity.getPoPlantId() == null) {
									// PO Plant Required
									isSuccess = false;
									responseModel.setMsg("PO Plant Required.");
								}
							}
							if (isSuccess) {
								int totalQty = 0;
								// data inserting into PO Tables
								for (MachinePODtlEntity machinePODtlEntity : poHdrEntity.getMachinePODtlList()) {
									totalQty = totalQty + machinePODtlEntity.getQuantity();
									machinePODtlEntity.setPendingQty(machinePODtlEntity.getQuantity());
									machinePODtlEntity.setInvoiceQty(0);
									machinePODtlEntity.setMachinePOHdr(poHdrEntity);
								}

								poHdrEntity.setTotalQty(totalQty);
								poHdrEntity.setCreatedBy(userId);
								poHdrEntity.setCreatedDate(currDate);
								poHdrEntity.setProductDivisionId(1);//need remove this

								session.save(poHdrEntity);
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
			} else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
				if (isSubmit) {
					// In case of Co-Dealer/CO-Distributor
					if (poToTypeResponseModel.getTypeCode().equalsIgnoreCase(WebConstants.CO_DEALER)
							|| poToTypeResponseModel.getTypeCode().equalsIgnoreCase(WebConstants.CO_DISTRIBUTOR)) {
						mapData = saveIntoApproval(session, userId, null, poHdrEntity);
						if (mapData != null && mapData.get("SUCCESS") != null) {
						} else {
							isSuccess = false;
							responseModel.setMsg("Error While Updating Machine PO Approval Hier.");
						}
					}
				}
				if (isSuccess) {
					//System.out.println("poHdrEntitypoHdrEntitypoHdrEntity"+poHdrEntity);
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
				mapData = fetchMachinePONoByPOId(poHdrEntity.getPoHdrId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setPoHdrId(poHdrEntity.getPoHdrId());
					responseModel.setPoNumber((String) mapData.get("poNumber"));
					responseModel.setMsg("PO Number Created Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
					
					try {
						
						if(poTypeval.equalsIgnoreCase(WebConstants.CO_DEALER ) || poTypeval.equalsIgnoreCase(WebConstants.CO_DISTRIBUTOR )) {
							poCreationMail(userCode, "PO CREATION IDT",poTypeval, poHdrEntity.getPoHdrId()).subscribe(e -> {
								logger.info(e.toString());
							});
						}else {
							poCreationMail(userCode, "PO CREATION",poTypeval, poHdrEntity.getPoHdrId()).subscribe(e -> {
								logger.info(e.toString());
							});
						}
						
						
					} catch (Exception exp) {
						logger.error(this.getClass().getName(), exp);
					}
					
				}
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	public <T> List<T> jsonArrayToObjectList(String json, Class<T> tClass) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
		List<T> ts = mapper.readValue(json, listType);
		logger.debug("class name: {}", ts.get(0).getClass().getName());
		return ts;
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchMachinePONoByPOId(BigInteger poHdrId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select po_number from SA_PO_HDR (nolock) po where po.po_hdr_id =:poHdrId";
		mapData.put("ERROR", "Machine PO Details Not Found");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("poHdrId", poHdrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String poNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					poNumber = (String) row.get("po_number");
				}
				mapData.put("poNumber", poNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE PO DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING MACHINE PO DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mapData;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private Mono<Map<String, Object>> poCreationMail(String userCode, String eventName,String flag, BigInteger hDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		BigInteger mailItemId = null;
		String sqlQuery ="";
		if(flag !=null && (flag.equalsIgnoreCase(WebConstants.CO_DEALER ) || flag.equalsIgnoreCase(WebConstants.CO_DISTRIBUTOR))) {
			sqlQuery = "exec [SP_MAIL_SA_PO_CREATION_IDT_MAIL] :userCode, :eventName, :refId, :isIncludeActive";
		}else {
			sqlQuery = "exec [SP_MAIL_SA_PO_CREATION_MAIL] :userCode, :eventName, :refId, :isIncludeActive";
		}
		
		//mapData.put("ERROR", "ERROR WHILE INSERTING VALIDATED ENQUIRY MAIL TRIGGERS..");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("eventName", eventName);
			query.setParameter("refId", hDRId);
			query.setParameter("isIncludeActive", "N");
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
			mapData.put("ERROR", "ERROR WHILE INSERTING PO CREATION MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING PO CREATION MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (mailItemId != null && mailItemId.compareTo(BigInteger.ZERO) > 0) {
				PublishModel publishModel = new PublishModel();
				publishModel.setId(mailItemId);
				//publishModel.setTopic(senderValidatedEnqTopicExchange.getName());
				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					// Simulate a long-running Job
					try {
//						rabbitTemplate.convertAndSend(senderValidatedEnqTopicExchange.getName(), routingKey,
//								commonUtils.objToJson(publishModel).toString());
//						logger.info("Published message for validated enquiry '{}'", publishModel.toString());

					} catch (Exception exp) {
						logger.error(this.getClass().getName(), exp);
					}
					System.out.println("I'll run in a separate thread than the main thread.");
				});
			}
		}
		return Mono.just(mapData);
	}	
}
