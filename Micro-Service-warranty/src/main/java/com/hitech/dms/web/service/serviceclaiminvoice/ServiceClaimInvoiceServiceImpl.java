package com.hitech.dms.web.service.serviceclaiminvoice;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Query;

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
import com.hitech.dms.web.dao.serviceclaiminvoice.ServiceClaimInvoiceDao;
import com.hitech.dms.web.entity.serviceclaiminvoice.ServiceClaimInvoiceDtl;
import com.hitech.dms.web.entity.serviceclaiminvoice.ServiceClaimInvoiceHdr;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimJobcardDetailDto;
import com.hitech.dms.web.model.serviceclaim.ServiceClaimViewDto;
import com.hitech.dms.web.model.serviceclaiminvoice.ServiceClaimInvoiceSearchRequestDto;
import com.hitech.dms.web.model.serviceclaiminvoice.ServiceClaimInvoiceSearchResponseDto;
import com.hitech.dms.web.model.serviceclaiminvoice.ServiceClaimInvoiceViewForSaveResponseDto;
import com.hitech.dms.web.model.serviceclaiminvoice.SvClaimInvoiceoViewForSaveDtlResponseDto;

/**
 * @author suraj.gaur
 */
@Service
public class ServiceClaimInvoiceServiceImpl implements ServiceClaimInvoiceService {

	private static final Logger logger = LoggerFactory.getLogger(ServiceClaimInvoiceServiceImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private ServiceClaimInvoiceDao claimInvoiceDao;
	
	@Override
	public ApiResponse<?> saveServiceClaimInvoice(String userCode, ServiceClaimInvoiceHdr requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("saveServiceClaimInvoice invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		
		ApiResponse<ServiceClaimInvoiceHdr> response = null;
		String svClaimInvoiceNo = null;
		boolean isSuccess = true;
		Map<String, Object> mapData = null;
		int size=0;
		
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			BigInteger branchId = requestModel.getBranchId();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			
			if (requestModel != null) {
				
				Query query = null;
				
				if(requestModel.getFinalSubmitFlag() !=null && requestModel.getFinalSubmitFlag().equalsIgnoreCase("Y")) {
					String sqlQuery = "update SV_SERVICE_CLAIM_INV_HDR set customer_invoice_no=:custInvNo,customer_invoice_date=:custInvDate,final_submit_flag=:finalSub where id =:idClaim";
		            try {
		            	query = session.createNativeQuery(sqlQuery);
		            	query.setParameter("custInvNo", requestModel.getCustomerInvoiceNo());
		    			query.setParameter("custInvDate", requestModel.getCustomerInvoiceDate());
		    			query.setParameter("finalSub", requestModel.getFinalSubmitFlag());
		    			query.setParameter("idClaim", requestModel.getId());
		    			
		    			int k = query.executeUpdate();
		            }
		            catch (SQLGrammarException ex) {
		    			mapData.put("ERROR", "ERROR WHILE UPDATING CUSTOMER INVOICE");
		    			logger.error(this.getClass().getName(), ex);
		    		} catch (Exception ex) {
		    			mapData.put("ERROR", "ERROR WHILE UPDATING CUSTOMER INVOICE");
		    			logger.error(this.getClass().getName(), ex);
		    		}
				}else {
					List<ServiceClaimInvoiceDtl> claimInvoiceDtls = requestModel.getClaimInvoiceDtls();
					if(claimInvoiceDtls !=null && claimInvoiceDtls.size()>0) {
						
						// Map to group by plantCode
				        Map<String, List<ServiceClaimInvoiceDtl>> groupedByPlantCode = new HashMap<>();
					
				        // Loop through the list and group by plantCode
				        for (ServiceClaimInvoiceDtl test : claimInvoiceDtls) {
				            if (!groupedByPlantCode.containsKey(test.getPlantCode())) {
				                groupedByPlantCode.put(test.getPlantCode(), new ArrayList<>());
				            }
				            groupedByPlantCode.get(test.getPlantCode()).add(test);
				        }
				        System.out.println("Plant Code: " + groupedByPlantCode);
				        size=groupedByPlantCode.size();
				        // Loop through each distinct plantCode and print list of Test objects
				        for (Map.Entry<String, List<ServiceClaimInvoiceDtl>> entry : groupedByPlantCode.entrySet()) {
				            System.out.println("Plant Code: " + entry.getKey());
				            
				            svClaimInvoiceNo = commonDao.getDocumentNumberById("SCI", branchId, session);
							commonDao.updateDocumentNumber("Service Claim Invoice", branchId, svClaimInvoiceNo, session);
							
							ServiceClaimInvoiceHdr obj=new ServiceClaimInvoiceHdr();
							obj.setPlantCode(entry.getKey());
							obj.setBranchId(requestModel.getBranchId());
							obj.setClaimInvoiceNo(svClaimInvoiceNo);
							obj.setClaimInvoiceDate(new Date());
							obj.setCreatedBy((BigInteger)mapData.get("userId"));
							obj.setCreatedDate(new Date());
							obj.setBasePrice(requestModel.getBasePrice());
							obj.setGstAmount(requestModel.getGstAmount());
							obj.setInvoiceAmount(requestModel.getInvoiceAmount());
							obj.setCreditNo(requestModel.getCreditNo());
							obj.setCreditAmount(requestModel.getCreditAmount());
							obj.setCreditDate(requestModel.getCreditDate());
							obj.setClaimHdrId(requestModel.getClaimHdrId());
							obj.setCustomerInvoiceNo(requestModel.getCustomerInvoiceNo());
							obj.setCustomerInvoiceDate(requestModel.getCustomerInvoiceDate());;
							BigInteger save = (BigInteger)session.save(obj);
							
							
							BigDecimal sum = new BigDecimal("0.00");
							BigDecimal gstsum = new BigDecimal("0.00");
							
							BigDecimal totalSum = new BigDecimal("0.00");
							
							BigDecimal gstPer = new BigDecimal("0.18");
							
							
				            for (ServiceClaimInvoiceDtl test : entry.getValue()) {
				                System.out.println("Chassis No: " + test.getChassisNo());
				                
				                sum = sum.add(test.getClaimValue());
				               // gstsum= gstsum.add(test.get));
				                
				                test.setInvHdrId(save);
				                test.setPlantCode(entry.getKey());
				                
				                session.save(test);
				            }
				            
				           
				            
				            BigDecimal result = sum.multiply(gstPer);
				            
				            totalSum=sum.add(result);
				           
				            
				            String sqlQuery = "update SV_SERVICE_CLAIM_INV_HDR set GST_AMOUNT=:gstAmt,BASE_PRICE=:sumClaimValue,INV_AMOUNT=:invAmt where id =:idClaim";
				            try {
				            	query = session.createNativeQuery(sqlQuery);
				            	query.setParameter("gstAmt", result);
				    			query.setParameter("sumClaimValue", sum);
				    			query.setParameter("invAmt", totalSum);
				    			query.setParameter("idClaim", save);
				    			
				    			int k = query.executeUpdate();
				            }
				            catch (SQLGrammarException ex) {
				    			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
				    			logger.error(this.getClass().getName(), ex);
				    		} catch (Exception ex) {
				    			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
				    			logger.error(this.getClass().getName(), ex);
				    		}
				        }
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
					response.setMessage(size+" Service Claim Invoice Created Successfully.");
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
	public ApiResponse<?> autoGetClaimInvoiceNo(String claimInvNoStr) {
		if (logger.isDebugEnabled()) {
			logger.debug("autoGetClaimInvoiceNo invoked..");
		}
	    Session session = null;
	    ApiResponse<List<?>> apiResponse = null;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        data = claimInvoiceDao.autoGetClaimInvoiceNo(session, claimInvNoStr);

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
	    		apiResponse.setMessage("Service Claim Invoice Created Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<List<ServiceClaimInvoiceSearchResponseDto>> serviceClaimInvoiceSearch(String userCode, ServiceClaimInvoiceSearchRequestDto requestModel) {
		
	    Session session = null;
	    
	    ApiResponse<List<ServiceClaimInvoiceSearchResponseDto>> apiResponse = new ApiResponse<>();

	    try {
	        session = sessionFactory.openSession();

	        List<?> data = claimInvoiceDao.serviceClaimInvoiceSearch(session, userCode, requestModel);

	        List<ServiceClaimInvoiceSearchResponseDto> responseList = new ArrayList<>();
	        Integer dataCount = 0;
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                Map<?, ?> row = (Map<?, ?>) object;
	                ServiceClaimInvoiceSearchResponseDto response = new ServiceClaimInvoiceSearchResponseDto();
//	                response.setAction((String)row.get("Action"));
	                response.setId((BigInteger) row.get("invoiceId"));
	                response.setInvoiceNo((String)row.get("invoiceNo"));
	                response.setInvoiceDate((Date) row.get("invoiceDate"));
	                response.setClaimType((String) row.get("claimType"));
	                response.setClaimNo((String) row.get("claimNo"));
	                response.setClaimDate((Date) row.get("claimDate"));
	                response.setDealerCode((String) row.get("dealerCode"));
	                response.setDealerName((String) row.get("dealerName"));
	                response.setDealerLocation((String)row.get("dealerLocation"));
	                response.setDealerBranch((String) row.get("dealerBranch"));
					response.setClaimAmount((BigDecimal) row.get("claimAmount"));
					response.setCreditNoteNo((String)row.get("creditNoteNo"));
					response.setCreditNoteDate((Date)row.get("creditNoteDate"));
					response.setCreditNoteAmount((BigDecimal)row.get("creditNoteAmount"));
					response.setCustomerInvoiceNo((String)row.get("customer_invoice_no"));
					response.setCustomerInvoiceDate((String)row.get("customer_invoice_date"));
					response.setFinalSubmit("Y".equals((String)row.get("final_submit_flag")) ? "Submitted" : "Pending");
					
					
					dataCount = (Integer) row.get("recordCount");

					responseList.add(response);
	            }
	        }

	        apiResponse.setCount(dataCount);
	        apiResponse.setResult(responseList);
	        // You can also set other properties of the ApiResponse here, like success status, messages, etc.

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
	
	
//	@Override
//	public ApiResponse<?> viewServiceClaimInvoice(BigInteger invoiceId) {
//	    Session session = null;
//	    ApiResponse<ServiceClaimViewDto> apiResponse = new ApiResponse<>();
//	    try {
//	        session = sessionFactory.openSession();
//
//	        List<?> data = claimInvoiceDao.viewServiceClaimInvoice(session, invoiceId);
//
//	        ServiceClaimViewDto response = new ServiceClaimViewDto();
//	        List<ServiceClaimJobcardDetailDto> jobcardDetails = new ArrayList<>();
//	        
//	        if (data != null && !data.isEmpty()) {
//	            for (Object object : data) {
//	                Map<?, ?> row = (Map<?, ?>) object;
//	                response.setId((BigInteger) row.get("claimId"));
//	                response.setInvoiceId((BigInteger)row.get("invoiceId"));
//	                response.setInvoiceNo((String)row.get("invoiceNo"));
//	                response.setInvoiceDate((Date)row.get("invoiceDate"));
//	                response.setClaimNo((String) row.get("claimNo"));
//	                response.setClaimDate((Date)row.get("claimDate"));
//	                response.setBasePrice((BigDecimal)row.get("basePrice"));
//	                response.setGstAmount((BigDecimal)row.get("gstAmount"));
//	                response.setInvoiceAmount((BigDecimal)row.get("invoiceAmount"));
//
//	                
//	                ServiceClaimJobcardDetailDto jcDetail = new ServiceClaimJobcardDetailDto();
//	                jcDetail.setJobcardNo((String)row.get("jobcardNo"));
//	                jcDetail.setJobcardDate((Date)row.get("jobcardDate"));
//	                jcDetail.setClaimType((String)row.get("claimType"));
//	                jcDetail.setJobcardCategory((String)row.get("jobcardCategory"));
//	                jcDetail.setServiceType((String)row.get("serviceType"));
//	                jcDetail.setModel((String)row.get("model"));
//	                jcDetail.setChassisNo((String)row.get("chassisNo"));
//	                jcDetail.setVinNo((String)row.get("vinNo"));
//	                jcDetail.setEngineNo((String)row.get("engineNo"));
//	                jcDetail.setHour((BigInteger)row.get("hours"));
//	                jcDetail.setClaimValue((BigDecimal)row.get("claimValue"));
//	                
//	                jobcardDetails.add(jcDetail);
//	                
//	                
//	            }
//	        }
//	        response.setJobcardDetails(jobcardDetails);
//	        apiResponse.setResult(response);
//
//	    } catch (SQLGrammarException exp) {
//	    	String errorMessage = "An SQL grammar error occurred: " + exp.getMessage();
//	        apiResponse.setMessage(errorMessage);
//	        logger.error(this.getClass().getName(), exp);
//	    } catch (HibernateException exp) {
//	    	String errorMessage = "A Hibernate error occurred: " + exp.getMessage();
//	        apiResponse.setMessage(errorMessage);
//	        logger.error(this.getClass().getName(), exp);
//	    } catch (Exception exp) {
//	    	 String errorMessage = "An error occurred: " + exp.getMessage();
//	         apiResponse.setMessage(errorMessage);
//	        logger.error(this.getClass().getName(), exp);
//	    } finally {
//	        if (session != null) {
//	            session.close();
//	        }
//	    }
//
//	    return apiResponse;
//	}
	
	
	@Override
	public ApiResponse<?> viewServiceClaimInvoice(BigInteger invoiceId) {
		Session session = null;
	    ApiResponse<ServiceClaimViewDto> apiResponse = null;
	    ServiceClaimViewDto responseDto = new ServiceClaimViewDto();
	    List<ServiceClaimJobcardDetailDto> dtlResponseDtos;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        //for getting Header Data
	        data = claimInvoiceDao.viewServiceClaimInvoice(session, invoiceId, 1);
	        if (data != null && !data.isEmpty()) {
	        	@SuppressWarnings("rawtypes")
	        	Map row = (Map) data.get(0);
	        	responseDto.setInvoiceId((BigInteger)row.get("invoiceId"));
	        	responseDto.setInvoiceNo((String)row.get("invoiceNo"));
	        	responseDto.setInvoiceDate((Date)row.get("invoiceDate"));
				responseDto.setClaimNo((String)row.get("claimNo"));
				responseDto.setClaimDate((Date)row.get("claimDate"));
				responseDto.setBasePrice((BigDecimal)row.get("basePrice"));
				responseDto.setGstAmount((BigDecimal)row.get("gstAmount"));
				responseDto.setInvoiceAmount((BigDecimal)row.get("invoiceAmount"));
				responseDto.setCustomerInvoiceNo((String)row.get("customer_invoice_no"));
				responseDto.setCustomerInvoiceDate((String)row.get("customer_invoice_date"));
				responseDto.setFinalSubmit((String)row.get("final_submit_flag"));				
				
				}
	        
	        //for getting Detail Data
	        data = null;
	        data = claimInvoiceDao.viewServiceClaimInvoice(session, invoiceId, 2);
	        if (data != null && !data.isEmpty()) {
	        	dtlResponseDtos = new ArrayList<>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					ServiceClaimJobcardDetailDto dtlResponseDto = new ServiceClaimJobcardDetailDto();
					dtlResponseDto.setRoId((BigInteger)row.get("roId"));
					dtlResponseDto.setJobcardNo((String)row.get("jobcardNo"));
					dtlResponseDto.setJobcardDate((Date)row.get("jobcardDate"));
					dtlResponseDto.setClaimType((String)row.get("claimType"));
					dtlResponseDto.setJobcardCatId((BigInteger)row.get("jobcardCatId"));
					dtlResponseDto.setJobcardCategory((String)row.get("jobcardCategory"));
					dtlResponseDto.setServiceTypeId((BigInteger)row.get("serviceTypeId"));
					dtlResponseDto.setServiceType((String)row.get("serviceType"));
					dtlResponseDto.setModel((String)row.get("model"));
					dtlResponseDto.setChassisNo((String)row.get("chassisNo"));
					dtlResponseDto.setVinNo((String)row.get("vinNo"));
					dtlResponseDto.setEngineNo((String)row.get("engineNo"));
					dtlResponseDto.setHour((BigInteger)row.get("hours"));
					dtlResponseDto.setClaimValue((BigDecimal)row.get("claimValue"));
					
					dtlResponseDtos.add(dtlResponseDto);
				}
				responseDto.setJobcardDetails(dtlResponseDtos);
	        }
	        
//	        apiResponse.setResult(responseDto);

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
	    		apiResponse.setResult(responseDto);
	    		apiResponse.setMessage("Service Claim Invoice Created Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}

	@Override
	public ApiResponse<?> viewServiceClaimInvoiceForSave(BigInteger claimId) {
		Session session = null;
	    ApiResponse<ServiceClaimInvoiceViewForSaveResponseDto> apiResponse = null;
	    ServiceClaimInvoiceViewForSaveResponseDto responseDto = new ServiceClaimInvoiceViewForSaveResponseDto();
	    List<SvClaimInvoiceoViewForSaveDtlResponseDto> dtlResponseDtos;
	    List<?> data = null;

	    try {
	        session = sessionFactory.openSession();

	        //for getting Header Data
	        data = claimInvoiceDao.viewServiceClaimInvoiceForSave(session, claimId, 1);
	        if (data != null && !data.isEmpty()) {
	        	@SuppressWarnings("rawtypes")
	        	Map row = (Map) data.get(0);
	        	responseDto.setClaimId((BigInteger)row.get("claimId"));
				responseDto.setClaimNo((String)row.get("claimNo"));
				responseDto.setClaimDate((Date)row.get("claimDate"));
				responseDto.setBasePrice((BigDecimal)row.get("basePrice"));
				responseDto.setGstAmount((BigDecimal)row.get("gstAmount"));
				responseDto.setInvoiceAmount((BigDecimal)row.get("invoiceAmount"));
	        }
	        
	        //for getting Detail Data
	        data = null;
	        data = claimInvoiceDao.viewServiceClaimInvoiceForSave(session, claimId, 2);
	        if (data != null && !data.isEmpty()) {
	        	dtlResponseDtos = new ArrayList<>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					SvClaimInvoiceoViewForSaveDtlResponseDto dtlResponseDto = new SvClaimInvoiceoViewForSaveDtlResponseDto();
					dtlResponseDto.setRoId((BigInteger)row.get("roId"));
					dtlResponseDto.setJobcardNo((String)row.get("jobcardNo"));
					dtlResponseDto.setJobcardDate((Date)row.get("jobcardDate"));
					dtlResponseDto.setClaimType((String)row.get("claimType"));
					dtlResponseDto.setJobcardCatId((BigInteger)row.get("jobcardCatId"));
					dtlResponseDto.setJobcardCategory((String)row.get("jobcardCategory"));
					dtlResponseDto.setServiceTypeId((BigInteger)row.get("serviceTypeId"));
					dtlResponseDto.setServiceType((String)row.get("serviceType"));
					dtlResponseDto.setModel((String)row.get("model"));
					dtlResponseDto.setChassisNo((String)row.get("chassisNo"));
					dtlResponseDto.setPlantCode((String)row.get("plantCode"));
					dtlResponseDto.setVinNo((String)row.get("vinNo"));
					dtlResponseDto.setEngineNo((String)row.get("engineNo"));
					dtlResponseDto.setHours((BigInteger)row.get("hours"));
					dtlResponseDto.setClaimValue((BigDecimal)row.get("claimValue"));
					dtlResponseDto.setClaimDtlId((BigInteger)row.get("claimDtlId"));
					dtlResponseDto.setCgstPercent((Integer)row.get("cgst_percent"));	
					dtlResponseDto.setCgstValue((BigDecimal)row.get("cgst_value"));
					dtlResponseDto.setSgstPercent((Integer)row.get("sgst_percent"));	
					dtlResponseDto.setSgstValue((BigDecimal)row.get("sgst_value"));
					dtlResponseDto.setIgstPercent((Integer)row.get("igst_percent"));	
					dtlResponseDto.setIgstValue((BigDecimal)row.get("igst_value"));
					dtlResponseDto.setPdiId((BigInteger)row.get("pdi_id"));	
					dtlResponseDto.setInsId((BigInteger)row.get("ins_id"));
					
					dtlResponseDtos.add(dtlResponseDto);
				}
				responseDto.setDtlResponseDtos(dtlResponseDtos);
	        }
	        
//	        apiResponse.setResult(responseDto);

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
	    		apiResponse.setResult(responseDto);
	    		apiResponse.setMessage("Service Claim Invoice Created Successfully.");
	    		apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
			}
	        if (session != null) {
	            session.close();
	        }
	    }

	    return apiResponse;
	}
	
	
	@Override
	public ApiResponse<List<ServiceClaimInvoiceSearchResponseDto>> serviceClaimCreditNoteSearch(String userCode, ServiceClaimInvoiceSearchRequestDto requestModel) {
		
	    Session session = null;
	    
	    ApiResponse<List<ServiceClaimInvoiceSearchResponseDto>> apiResponse = new ApiResponse<>();

	    try {
	        session = sessionFactory.openSession();

	        List<?> data = claimInvoiceDao.serviceClaimCreditNoteSearch(session, userCode, requestModel);

	        List<ServiceClaimInvoiceSearchResponseDto> responseList = new ArrayList<>();
	        Integer dataCount = 0;
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                Map<?, ?> row = (Map<?, ?>) object;
	                ServiceClaimInvoiceSearchResponseDto response = new ServiceClaimInvoiceSearchResponseDto();
//	                response.setAction((String)row.get("Action"));
	                response.setId((BigInteger) row.get("invoiceId"));
	                response.setInvoiceNo((String)row.get("invoiceNo"));
	                response.setInvoiceDate((Date) row.get("invoiceDate"));
	                response.setClaimType((String) row.get("claimType"));
	                response.setClaimNo((String) row.get("claimNo"));
	                response.setClaimDate((Date) row.get("claimDate"));
	                response.setDealerCode((String) row.get("dealerCode"));
	                response.setDealerName((String) row.get("dealerName"));
	                response.setDealerLocation((String)row.get("dealerLocation"));
	                response.setDealerBranch((String) row.get("dealerBranch"));
					response.setClaimAmount((BigDecimal) row.get("claimAmount"));
					response.setCreditNoteNo((String)row.get("creditNoteNo"));
					response.setCreditNoteDate((Date)row.get("creditNoteDate"));
					response.setCreditNoteAmount((BigDecimal)row.get("creditNoteAmount"));
					response.setAction("pdf download");
					dataCount = (Integer) row.get("recordCount");

					responseList.add(response);
	            }
	        }

	        apiResponse.setCount(dataCount);
	        apiResponse.setResult(responseList);
	        // You can also set other properties of the ApiResponse here, like success status, messages, etc.

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

}
