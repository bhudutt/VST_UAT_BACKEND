package com.hitech.dms.web.service.serviceclaim;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.serviceclaim.ServiceClaimDao;
import com.hitech.dms.web.entity.serviceclaim.ServiceClaimApproval;
import com.hitech.dms.web.entity.serviceclaim.ServiceClaimHdr;
import com.hitech.dms.web.model.goodwill.GoodwillApprovalResponseDto;
import com.hitech.dms.web.model.serviceclaim.ServiceCLaimApprovalRequestDto;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimJCListRequest;

import com.hitech.dms.web.model.serviceclaim.ServiceClaimJobcardDetailDto;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimSearchRequestDto;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimSearchResponseDto;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimViewDto;

import com.hitech.dms.web.model.serviceclaim.ServiceClaimJCListResponse;


/**
 * @author suraj.gaur
 */
@Service
public class ServiceClaimServiceImpl implements ServiceClaimService {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceClaimServiceImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private ServiceClaimDao claimDao;
	
	@Override
	public ApiResponse<?> saveServiceClaim(String userCode, ServiceClaimHdr requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveServiceClaim invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		
		ApiResponse<ServiceClaimHdr> response = null;
		String serviceClaimNo = null;
		boolean isSuccess = true;
		Map<String, Object> mapData = null;
		
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			BigInteger branchId = requestModel.getBranchId();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			
			if (requestModel != null) {
				serviceClaimNo = commonDao.getDocumentNumberById("SC", branchId, session);
				commonDao.updateDocumentNumber("Service Claim", branchId, serviceClaimNo, session);
				
				requestModel.setClaimDate(new Date());
				requestModel.setClaimNo(serviceClaimNo);
				requestModel.setStatus("PENDING");
				
				if(requestModel.getId() == null) {
					requestModel.setCreatedBy((BigInteger)mapData.get("userId"));
					requestModel.setCreatedDate(new Date());
		        }
				else {
					requestModel.setModifiedBy((BigInteger)mapData.get("userId"));
					requestModel.setModifiedDate(new Date());
		        }
		        session.save(requestModel);
		        
		        //Setting hierarchy in table
		        List<ServiceClaimApproval> serviceClaimApprovals = new ArrayList<>();
				List<?> data = claimDao.getApprovalHierarchy(session);
				
				if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						@SuppressWarnings("rawtypes")
						Map row = (Map) object;
						
						ServiceClaimApproval hierarchyData = new ServiceClaimApproval();
						hierarchyData.setServiceClaimId(requestModel.getId());
						hierarchyData.setApproverLevelSeq((Integer) row.get("approver_level_seq"));
						hierarchyData.setDesignationLevelId((Integer)row.get("designation_level_id"));
						hierarchyData.setGrpSeqNo((Integer) row.get("grp_seq_no"));
						hierarchyData.setApprovalStatus((String) row.get("approvalStatus"));
						hierarchyData.setIsfinalapprovalstatus((Character) row.get("isFinalApprovalStatus"));
						hierarchyData.setRejectedFlag('N');

						serviceClaimApprovals.add(hierarchyData);
					}
				}		        
		        if (!serviceClaimApprovals.isEmpty()) {
		            for (ServiceClaimApproval approvalEntity : serviceClaimApprovals) {
		                session.save(approvalEntity);
		            }
		        }
			} 
			
			//Committing the transaction if everything went well.
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
				if (requestModel != null) {
					response = new ApiResponse<>();
					response.setResult(requestModel);
					response.setMessage("Service Claim Created Successfully.");
					response.setStatus(WebConstants.STATUS_CREATED_201);
				}
			}
			if (session != null) {
				session.close();
			}
		}
		return response;
	}

	@Override
	public ApiResponse<?> getJobcardClaimList(String userCode, ServiceClaimJCListRequest requestModel) {
	    Session session = null;
	    ApiResponse<List<ServiceClaimJCListResponse>> apiResponse = null;
	    List<?> data = null;
	    List<ServiceClaimJCListResponse> responseList = null;

	    try {
	        session = sessionFactory.openSession();
	        
	        data = claimDao.getJobcardClaimList(session, userCode, requestModel);

	        if(requestModel.getClaimType().equals("3")) {
	        	responseList = new ArrayList<>();
		        if (data != null && !data.isEmpty()) {
		            for (Object object : data) {
		                Map<?, ?> row = (Map<?, ?>) object;
		                ServiceClaimJCListResponse response = new ServiceClaimJCListResponse();
		                response.setJobcardId((BigInteger)row.get("jobcardId"));
		                response.setJobcardNo((String)row.get("jobcardNo"));
		                response.setJobcardDate((Date)row.get("jobcardDate"));
		                response.setClaimId((Integer)row.get("claimId"));
		                response.setClaimType((String)row.get("claimType"));
		                response.setJobcardCategoryId((Integer)row.get("jobcardCategoryId"));
		                response.setJobcardCategory((String)row.get("jobcardCategory"));
		                response.setServiceTypeId((Integer)row.get("serviceTypeId"));
		                response.setServiceType((String)row.get("serviceType"));
		                response.setModel((String)row.get("model"));
		                response.setChassisNo((String)row.get("chassisNo"));
		                response.setVinNo((String)row.get("vinNo"));
		                response.setEngineNo((String)row.get("engineNo"));
		                response.setHours((BigInteger)row.get("hours"));
						response.setClaimValue((BigDecimal)row.get("claimValue"));
						response.setPlantCode((String)row.get("plantCode"));//added by mahesh.kumar on 20-03-2025

						responseList.add(response);
		            }
		        }
			} else if (requestModel.getClaimType().equals("8")) {
				if (data != null && !data.isEmpty()) {
					responseList = new ArrayList<>();
					for (Object object : data) {
						Map<?, ?> row = (Map<?, ?>) object;
						ServiceClaimJCListResponse response = new ServiceClaimJCListResponse();
						response.setPdiId((Integer) row.get("pdiId"));
						response.setPdiNo((String) row.get("pdiNo"));
						response.setPdiDate((Date) row.get("pdiDate"));
						response.setClaimId((Integer) row.get("claimId"));
						response.setClaimType((String) row.get("claimType"));
						response.setDcId((BigInteger) row.get("dcId"));
						response.setDcNumber((String) row.get("dcNumber"));
						response.setDcDate((Date) row.get("dcDate"));
						response.setModel((String) row.get("model"));
						response.setChassisNo((String) row.get("chassisNo"));
						response.setVinNo((String) row.get("vinNo"));
						response.setEngineNo((String) row.get("engineNo"));
						response.setClaimValue((BigDecimal) row.get("claimValue"));
						response.setPlantCode((String)row.get("plantCode"));//added by mahesh.kumar on 20-03-2025

						responseList.add(response);
					}
				}
			} else if (requestModel.getClaimType().equals("9")) {
				if (data != null && !data.isEmpty()) {
					responseList = new ArrayList<>();
					for (Object object : data) {
						Map<?, ?> row = (Map<?, ?>) object;
						ServiceClaimJCListResponse response = new ServiceClaimJCListResponse();
						response.setInstallationId((Integer) row.get("installationId"));
						response.setInstallationNo((String) row.get("installationNo"));
						response.setInstallationDate((Date) row.get("installationDate"));
						response.setClaimId((Integer) row.get("claimId"));
						response.setClaimType((String) row.get("claimType"));
						response.setDcId((BigInteger) row.get("dcId"));
						response.setDcNumber((String) row.get("dcNumber"));
						response.setDcDate((Date) row.get("dcDate"));
						response.setModel((String) row.get("model"));
						response.setChassisNo((String) row.get("chassisNo"));
						response.setVinNo((String) row.get("vinNo"));
						response.setEngineNo((String) row.get("engineNo"));
						response.setClaimValue((BigDecimal) row.get("claimValue"));
						response.setPlantCode((String)row.get("plantCode"));//added by mahesh.kumar on 20-03-2025

						responseList.add(response);
					}
				}
			}

	    } catch (SQLGrammarException exp) {
	    	String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } catch (HibernateException exp) {
	    	String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } catch (Exception exp) {
	    	 String errorMessage = "An error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } finally {
	    	if (data != null) {
	    		apiResponse = new ApiResponse<>();
	    		apiResponse.setResult(responseList);
	    		apiResponse.setMessage("Job Card Claim List Get Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	@Override
	public ApiResponse<?> viewServiceClaim(BigInteger claimId) {
	    Session session = null;
	    ApiResponse<ServiceClaimViewDto> apiResponse = new ApiResponse<>();
	    ServiceClaimViewDto response = new ServiceClaimViewDto();
        List<ServiceClaimJobcardDetailDto> jobcardDetails = new ArrayList<>();
	    List<?> data = null;
	    try {
	        session = sessionFactory.openSession();

	        data = claimDao.viewServiceClaim(session, claimId, 1);
	        
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                Map<?, ?> row = (Map<?, ?>) object;
	                response.setId((BigInteger) row.get("claimId"));
	                response.setClaimNo((String) row.get("claimNo"));
	                response.setClaimType((String)row.get("claimType"));
	                response.setClaimDate((Date)row.get("claimDate"));
	                response.setStatus((String)row.get("status"));
	                response.setInvoiceId((BigInteger)row.get("invoiceId"));
	                response.setInvoiceNo((String)row.get("invoiceNo"));
	                response.setRemark((String)row.get("remark"));
	                response.setRejectedreason((String)row.get("rejectedReason"));
	                
	                ServiceClaimJobcardDetailDto jcDetail = new ServiceClaimJobcardDetailDto();
	                jcDetail.setJobcardCatId((BigInteger) row.get("jobcardId"));;
	                jcDetail.setInstallationId((Integer)row.get("installationId"));
	                jcDetail.setJobcardNo((String)row.get("jobcardNo"));
	                jcDetail.setJobcardDate((Date)row.get("jobcardDate"));
	                jcDetail.setClaimType((String)row.get("claimType"));
	                jcDetail.setJobcardCategory((String)row.get("jobcardCategory"));
	                jcDetail.setServiceType((String)row.get("serviceType"));
	                jcDetail.setModel((String)row.get("model"));
	                jcDetail.setChassisNo((String)row.get("chassisNo"));
	                jcDetail.setVinNo((String)row.get("vinNo"));
	                jcDetail.setEngineNo((String)row.get("engineNo"));
	                jcDetail.setHour((BigInteger)row.get("hours"));
	                jcDetail.setClaimValue((BigDecimal)row.get("claimValue"));
	                jcDetail.setInstallationStatus((String)row.get("installationStatus"));
	                
	                jobcardDetails.add(jcDetail);
	                
	                
	            }
	        }
	        
	        data = null;
	        data = claimDao.viewServiceClaim(session, claimId, 2);
	        
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                Map<?, ?> row = (Map<?, ?>) object;
	                response.setId((BigInteger) row.get("claimId"));
	                response.setClaimNo((String) row.get("claimNo"));
	                response.setClaimType((String)row.get("claimType"));
	                response.setClaimDate((Date)row.get("claimDate"));
	                response.setStatus((String)row.get("status"));
	                response.setInvoiceId((BigInteger)row.get("invoiceId"));
	                response.setInvoiceNo((String)row.get("invoiceNo"));
	                response.setRemark((String)row.get("remark"));
	                response.setRejectedreason((String)row.get("rejectedReason"));
	                
	                ServiceClaimJobcardDetailDto jcDetail = new ServiceClaimJobcardDetailDto();
	                jcDetail.setInstallationId((Integer)row.get("installationId"));
	                jcDetail.setPdiId((Integer)row.get("pdiId"));
	                jcDetail.setPdiNo((String)row.get("pdiNo"));
	                jcDetail.setPdiDate((Date)row.get("pdiDate"));
	                jcDetail.setClaimId((BigInteger)row.get("claimId"));
	                jcDetail.setClaimType((String)row.get("claimType"));
	                jcDetail.setDcId((BigInteger)row.get("dcId"));
	                jcDetail.setDcNumber((String)row.get("dcNumber"));
	                jcDetail.setDcDate((Date)row.get("dcDate"));
	                jcDetail.setModel((String)row.get("model"));
	                jcDetail.setChassisNo((String)row.get("chassisNo"));
	                jcDetail.setVinNo((String)row.get("vinNo"));
	                jcDetail.setEngineNo((String)row.get("engineNo"));
	                jcDetail.setClaimValue((BigDecimal)row.get("claimValue"));
	                jcDetail.setInstallationStatus((String)row.get("installationStatus"));
	                
	                jobcardDetails.add(jcDetail);
	                
	                
	            }
	        }
	        
	        
	        data = null;
	        data = claimDao.viewServiceClaim(session, claimId, 3);
	        
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                Map<?, ?> row = (Map<?, ?>) object;
	                response.setId((BigInteger) row.get("claimId"));
	                response.setClaimNo((String) row.get("claimNo"));
	                response.setClaimType((String)row.get("claimType"));
	                response.setClaimDate((Date)row.get("claimDate"));
	                response.setStatus((String)row.get("status"));
	                response.setInvoiceId((BigInteger)row.get("invoiceId"));
	                response.setInvoiceNo((String)row.get("invoiceNo"));
	                response.setRemark((String)row.get("remark"));
	                response.setRejectedreason((String)row.get("rejectedReason"));
	                
	                ServiceClaimJobcardDetailDto jcDetail = new ServiceClaimJobcardDetailDto();
	                jcDetail.setInstallationId((Integer)row.get("installationId"));
	                jcDetail.setInstallationNo((String)row.get("installationNo"));
	                jcDetail.setInstallationDate((Date)row.get("installationDate"));
	                jcDetail.setClaimId((BigInteger)row.get("claimId"));
	                jcDetail.setClaimType((String)row.get("claimType"));
	                jcDetail.setDcId((BigInteger)row.get("dcId"));
	                jcDetail.setDcNumber((String)row.get("dcNumber"));
	                jcDetail.setDcDate((Date)row.get("dcDate"));
	                jcDetail.setModel((String)row.get("model"));
	                jcDetail.setChassisNo((String)row.get("chassisNo"));
	                jcDetail.setVinNo((String)row.get("vinNo"));
	                jcDetail.setEngineNo((String)row.get("engineNo"));
	                jcDetail.setClaimValue((BigDecimal)row.get("claimValue"));
	                jcDetail.setInstallationStatus((String)row.get("installationStatus"));
	                
	                jobcardDetails.add(jcDetail);
	                
	                
	            }
	        }
	        
	        
	        response.setJobcardDetails(jobcardDetails);
	        apiResponse.setResult(response);

	    } catch (SQLGrammarException exp) {
	    	String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (HibernateException exp) {
	    	String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	    	 String errorMessage = "An error occurred: " + exp.getMessage();
	         apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}

	@Override
	public ApiResponse<?> getClaimTypes() {
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = claimDao.getClaimTypes(session);

//	        apiResponse.setResult(data);

	    } catch (SQLGrammarException exp) {
	    	String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } catch (HibernateException exp) {
	    	String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } catch (Exception exp) {
	    	 String errorMessage = "An error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } finally {
	    	if (data != null) {
	    		apiResponse = new ApiResponse<>();
	    		apiResponse.setResult(data);
	    		apiResponse.setMessage("Get claim type Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	@Override
	public ApiResponse<List<Map<String, Object>>> autoSearchClaimNo(String claimNo) {
	    ApiResponse<List<Map<String, Object>>> apiResponse = new ApiResponse<>();

	    try (Session session = sessionFactory.openSession()) { // Use try-with-resources for session
	        List<?> data = claimDao.autoSearchClaimNo(session, claimNo);

	        if (data != null && !data.isEmpty()) {
	            List<Map<String, Object>> responseModelList = new ArrayList<>();

	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("claimId", row.get("claimId"));
	                responseModel.put("claimNo", row.get("claimNo"));
	                responseModelList.add(responseModel);
	            }

	            apiResponse.setResult(responseModelList); // Set the result in the ApiResponse
	        } else {
	            apiResponse.setMessage("No data found."); // Handle the case when no data is returned
	        }
	    } catch (HibernateException exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	        String errorMessage = "An error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    }

	    return apiResponse;
	}	
	
	@Override
	public ApiResponse<?> getClaimStatus() {
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = new ApiResponse<>();

	    try {
	        session = sessionFactory.openSession();

	        List<?> data = claimDao.getClaimStatus(session);

	        apiResponse.setResult(data);

	    } catch (SQLGrammarException exp) {
	    	String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (HibernateException exp) {
	    	String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
	        apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	    	 String errorMessage = "An error occurred: " + exp.getMessage();
	         apiResponse.setMessage(errorMessage);
	        logger.error(this.getClass().getName(), exp);
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<List<ServiceClaimSearchResponseDto>> serviceClaimSearch(String userCode, ServiceClaimSearchRequestDto requestModel) {
	    Session session = null;
	    ApiResponse<List<ServiceClaimSearchResponseDto>> apiResponse = null;
	    List<ServiceClaimSearchResponseDto> responseList = null;
	    List<?> data = null;
	    Integer dataCount = 0;

	    try {
	        session = sessionFactory.openSession();

	        data = claimDao.serviceClaimSearch(session, userCode, requestModel);

	        responseList = new ArrayList<>();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                Map<?, ?> row = (Map<?, ?>) object;
	                ServiceClaimSearchResponseDto response = new ServiceClaimSearchResponseDto();
	                response.setAction((String)row.get("Action"));
	                response.setId((BigInteger) row.get("claimId"));
	                response.setClaimType((String) row.get("claimType"));
	                response.setClaimNo((String) row.get("claimNo"));
	                response.setClaimDate((Date) row.get("claimDate"));
	                response.setDealerCode((String) row.get("dealerCode"));
	                response.setDealerName((String) row.get("dealerName"));
	                response.setDealerLocation((String)row.get("dealerLocation"));
	                response.setDealerBranch((String) row.get("dealerBranch"));
					response.setStatus((String) row.get("status"));	
					response.setTotalClaimValue((BigDecimal) row.get("totalClaimValue"));
					dataCount = (Integer) row.get("recordCount");

					responseList.add(response);
	            }
	        }

//	        apiResponse.setCount(dataCount);
//	        apiResponse.setResult(responseList);
	        // You can also set other properties of the ApiResponse here, like success status, messages, etc.

	    } catch (SQLGrammarException exp) {
	    	String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } catch (HibernateException exp) {
	    	String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } catch (Exception exp) {
	    	 String errorMessage = "An error occurred: " + exp.getMessage();
	        logger.error(errorMessage + this.getClass().getName(), exp);
	    } finally {
	    	if (data != null) {
	    		apiResponse = new ApiResponse<>();
	    		apiResponse.setCount(dataCount);
	    		apiResponse.setResult(responseList);
	    		apiResponse.setMessage("Service Claim Searched Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}

	@Override
	public ApiResponse<?> approveRejectSVClaim(String userCode, ServiceCLaimApprovalRequestDto requestModel) {
	    Session session = null;
	    Transaction transaction = null;
	    boolean isSuccess = true;
	    
	    ApiResponse<GoodwillApprovalResponseDto> apiResponse = null;
	    GoodwillApprovalResponseDto responseModel = new GoodwillApprovalResponseDto();
	    Map<String, Object> mapData = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();
	        transaction = session.beginTransaction();
	        
	        mapData = commonDao.fetchHOUserDTLByUserCode(session, userCode);
	        BigInteger hoUserId = null;
	        if (mapData != null && mapData.get("SUCCESS") != null) {
	        	hoUserId = (BigInteger) mapData.get("hoUserId");
	        }
	        
	        data = claimDao.approveRejectSVClaim(session, userCode, hoUserId, requestModel);
	        
	        if (data != null && !data.isEmpty()) {
	        	if(requestModel.getInstaillationStatus()!=null && !requestModel.getInstaillationStatus().isEmpty()) {
	        		claimDao.approveRejectStatusClaim(session, userCode, hoUserId, requestModel.getInstaillationStatus());
	        	}
	        	if(requestModel.getPdiStatus()!=null && !requestModel.getPdiStatus().isEmpty()) {
	        		claimDao.approveRejectPDIStatusClaim(session, userCode, hoUserId, requestModel.getPdiStatus());
	        	}
	        	if(requestModel.getJobCardStatus()!=null && !requestModel.getJobCardStatus().isEmpty()) {
	        		claimDao.approveRejectJCStatusClaim(session, userCode, hoUserId, requestModel.getJobCardStatus());
	        	}
	            @SuppressWarnings("rawtypes")
				Map row = (Map) data.get(0);
	            String msg = (String) row.get("msg");
	            String approvalStatus = (String) row.get("approvalStatus");
	            
	            responseModel.setMsg(msg);
	            responseModel.setApprovalStatus(approvalStatus);
	            responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
	        } else {
	            responseModel.setMsg("Error While Validating Service Claim Approval.");
	            responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	        }
	        
	        if (isSuccess) {
				transaction.commit();
			}

	    } catch (HibernateException ex) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        isSuccess = false;
	        responseModel.setMsg(ex.getMessage());
	        responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	        logger.error(this.getClass().getName(), ex);
	    } catch (Exception ex) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        isSuccess = false;
	        responseModel.setMsg(ex.getMessage());
	        responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	        logger.error(this.getClass().getName(), ex);
	    } finally {
	    	if (data != null) {
	    		apiResponse = new ApiResponse<>();
	    		apiResponse.setResult(responseModel);
	    		apiResponse.setMessage("Operation Completed Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}

	@Override
	public ApiResponse<?> installationAppAndRej(String userCode, Integer installationId, String statusApproveReject) {
		  Session session = null;
		    Transaction transaction = null;
		    boolean isSuccess = true;
		    
		    ApiResponse<GoodwillApprovalResponseDto> apiResponse = null;
		    GoodwillApprovalResponseDto responseModel = new GoodwillApprovalResponseDto();
		    Map<String, Object> mapData = null;
		    Integer data = null;

		    try {
		        session = sessionFactory.openSession();
		        transaction = session.beginTransaction();
		        
		        mapData = commonDao.fetchHOUserDTLByUserCode(session, userCode);
		        BigInteger hoUserId = null;
		        if (mapData != null && mapData.get("SUCCESS") != null) {
		        	hoUserId = (BigInteger) mapData.get("hoUserId");
		        }
		        
		        data = claimDao.installationAppAndRej(session, userCode, hoUserId, installationId,statusApproveReject);
		        
		        
		        if (isSuccess) {
					transaction.commit();
				}

		    } catch (HibernateException ex) {
		        if (transaction != null) {
		            transaction.rollback();
		        }
		        isSuccess = false;
		        responseModel.setMsg(ex.getMessage());
		        responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		        logger.error(this.getClass().getName(), ex);
		    } catch (Exception ex) {
		        if (transaction != null) {
		            transaction.rollback();
		        }
		        isSuccess = false;
		        responseModel.setMsg(ex.getMessage());
		        responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		        logger.error(this.getClass().getName(), ex);
		    } finally {
		    	if (data != null) {
		    		apiResponse = new ApiResponse<>();
		    		apiResponse.setResult(responseModel);
		    		apiResponse.setMessage("Operation Completed Successfully.");
		    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
				}
		        if (session != null) {
		            session.close();
		        }
		    }

		    return apiResponse;
		}

}
