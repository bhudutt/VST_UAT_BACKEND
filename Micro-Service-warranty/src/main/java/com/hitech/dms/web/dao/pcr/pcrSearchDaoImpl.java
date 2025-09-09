package com.hitech.dms.web.dao.pcr;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.pcr.ServiceWarrantyPcr;
import com.hitech.dms.web.entity.pcr.ServiceWarrantyPcrDtlEntity;
import com.hitech.dms.web.entity.pcr.WarrantyPcrApprovalEntity;
import com.hitech.dms.web.entity.pcr.WarrantyPcrPhotos;
import com.hitech.dms.web.model.pcr.ComplaintAggregateDto;
import com.hitech.dms.web.model.pcr.CustomerVoiceDto;
import com.hitech.dms.web.model.pcr.JobCardPcrDto;
import com.hitech.dms.web.model.pcr.JobCardPcrPartDto;
import com.hitech.dms.web.model.pcr.JobCardPcrViewDto;
import com.hitech.dms.web.model.pcr.LabourChargeDTO;
import com.hitech.dms.web.model.pcr.PCRApprovalRequestDto;
import com.hitech.dms.web.model.pcr.PcrApprovalResponseDto;
import com.hitech.dms.web.model.pcr.PcrCreateResponseDto;
import com.hitech.dms.web.model.pcr.PcrSearchRequestDto;
import com.hitech.dms.web.model.pcr.PcrSearchResponseDto;
import com.hitech.dms.web.model.pcr.ServiceHistoryDto;
import com.hitech.dms.web.model.serviceclaiminvoice.ServiceClaimInvoiceSearchResponseDto;
import com.hitech.dms.web.service.common.Utils;


/**
 * @author mahesh.kumar
 * @apiNote this is the DAO Layer which is used for database interaction
 */

@SuppressWarnings("deprecation")
@Repository
public class pcrSearchDaoImpl implements pcrSearchDao{
	private static final Logger logger = LoggerFactory.getLogger(pcrSearchDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private Utils utils;
	
//	@Autowired
//	private Session session;

	@SuppressWarnings({"rawtypes"})
	@Override
	public ApiResponse<List<PcrSearchResponseDto>> pcrSearchList(String userCode, PcrSearchRequestDto requestModel) {
	    if (logger.isDebugEnabled()) {
	        logger.debug("pcrSearchList invoked.. " + userCode + " " + requestModel.toString());
	    }
	    
	    
	    Session session = null;
	    
	    Map<String, Object> mapData = null;
	    boolean isSuccess = true;
	    
	    
//	    private Session session;
	    ApiResponse<List<PcrSearchResponseDto>> apiResponse = new ApiResponse<>();
	    
	    List<PcrSearchResponseDto> pcrSearchList = new ArrayList<>();
	    @SuppressWarnings("unused")
		Integer dataCount = 0;
	    
	    String sqlQuery = "exec [SV_Get_PCR_Search_Details] :PCRNO, :userCode, :hoUserId, :RONumber, :PCRStatus, :fromDate, :toDate, :page, :size";
	    
	    try{
	    	session = sessionFactory.openSession();
	    	mapData = commonDao.fetchHOUserDTLByUserCode(session, userCode);
//	    	if (mapData != null && mapData.get("SUCCESS") != null) {
	    		BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
		        Query query = session.createSQLQuery(sqlQuery);
		        query.setParameter("PCRNO", requestModel.getPcrNo());
		        query.setParameter("userCode", userCode);
		        query.setParameter("hoUserId", hoUserId);
		        query.setParameter("RONumber", requestModel.getJobCardNo());
		        query.setParameter("PCRStatus", requestModel.getStatus());
		        query.setParameter("fromDate", requestModel.getFromDate());
		        query.setParameter("toDate", requestModel.getToDate());
		        query.setParameter("page", requestModel.getPage());
		        query.setParameter("size", requestModel.getSize());
		        
		        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		        List data = query.list();
		        
		        if (data != null && !data.isEmpty()) {
		            for (Object object : data) {
		                Map row = (Map) object;
		                PcrSearchResponseDto response = new PcrSearchResponseDto();
		                response.setId((BigInteger)row.get("id"));
		                response.setAction((String)row.get("Action")); // Set appropriate value based on your logic or leave it blank
		                response.setDealerCode((String) row.get("DealerCode"));
		                response.setDealerName((String) row.get("DealerName"));
		                response.setBranch((String) row.get("BranchName"));
		                response.setPcrNo((String) row.get("PCRNO"));
		                response.setPcrCreatedDate((Date) row.get("PCRDATE"));
		                response.setPcrSubmittedDate((Date) row.get("PCR_SUBMITED_DATE"));
		                response.setStatus((String) row.get("status"));
		                response.setJobCardNo((String) row.get("JobCard_No"));
		                response.setJobCardDate((Date) row.get("JobCard_Date"));
		                response.setChassisNo((String) row.get("chassis_no"));
	//	                response.setTotalCount((Integer)row.get("recordCount"));
		                dataCount = (Integer) row.get("recordCount");
		                
		                pcrSearchList.add(response);
		            }
			        apiResponse.setCount(dataCount);
			        apiResponse.setResult(pcrSearchList);
		           
		        }else {
		        	apiResponse.setCount(dataCount);
		        	apiResponse.setResult(pcrSearchList);
//					isSuccess = false;
					apiResponse.setMessage("Data Not Found.");
				}
		        
		        

	            // Extract the record count from the last row of the result set
//	            Map lastRow = (Map) data.get(data.size() - 1);
//	            dataCount = (Integer) lastRow.get("recordCount");
	            
//	        }
	    } catch (SQLGrammarException exp) {
	        logger.error(this.getClass().getName(), exp);
	    } catch (HibernateException exp) {
	        logger.error(this.getClass().getName(), exp);
	    } catch (Exception exp) {
	        logger.error(this.getClass().getName(), exp);
	    }

	    return apiResponse;
	}
	    
	 
	
	@Override
	public List<Map<String, Object>> fetchAllFailureType(String userCode, BigInteger roId) {
	    Session session = null;
	    List<Map<String, Object>> responseModelList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [wa_mt_dropdown_failure_type] :roId";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("roId", roId);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("id", row.get("id")); // Assuming the column name is "id" in the database table
	                responseModel.put("failureType", row.get("failureType")); // Assuming the column name is "failure_type" in the database table
	                responseModelList.add(responseModel);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session.isOpen())
	            session.close();
	    }

	    return responseModelList;
	}
	
	@Override
	public List<ComplaintAggregateDto> getComplaintAggregate(String authorizationHeader, String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("getComplaintAggregate invoked.." + userCode);
		}
		Query query = null;
		Session session = null;
		ComplaintAggregateDto responseListModel = null;
		List<ComplaintAggregateDto> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "select ID, COMPLAINTAGGREGATEDESC from SV_COMPLAINT_AGGREGATE";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new ComplaintAggregateDto();
				responseModelList = new ArrayList<ComplaintAggregateDto>();
				ComplaintAggregateDto responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new ComplaintAggregateDto();
					responseModel.setID((Integer) row.get("ID"));
//					responseModel.setCOMPLAINTAGGREGATECODE((String) row.get("COMPLAINTAGGREGATECODE"));
					responseModel.setCOMPLAINTAGGREGATEDESC((String) row.get("COMPLAINTAGGREGATEDESC"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}
	
	
	@Override
	public List<Map<String, Object>> getComplaintCode(Integer ID, String userCode) {
	    Session session = null;
	    List<Map<String, Object>> responseModelList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sp_SV_warranty_complaint_code_data_for_pcr] :ID";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("ID", ID);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("id", row.get("id")); // Assuming the column name is "id" in the database table
	                responseModel.put("ComplaintAggregateCode", row.get("ComplaintAggregateCode")); // Assuming the column name is "failure_type" in the database table
	                responseModelList.add(responseModel);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session.isOpen())
	            session.close();
	    }

	    return responseModelList;
	}


	@SuppressWarnings({ "unused"})
	@Override
	public JobCardPcrViewDto fetchJobCardPcrView(String userCode, BigInteger roId, Integer flag) {
	    Session session = null;
	    Map<String, Object> responseModel = null;
	    JobCardPcrViewDto jobCardPcrViewDto = null;
	    List<JobCardPcrDto> responseModelList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sp_sv_warranty_pcr_job_card_data_for_pcr] :roId, :flag";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("roId", roId);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                jobCardPcrViewDto = new JobCardPcrViewDto();
	                jobCardPcrViewDto.setTotalHour((BigInteger) row.get("Hours"));
	                jobCardPcrViewDto.setJobCardNo((String)row.get("jobCardNo"));
	                jobCardPcrViewDto.setJobCardDate((String)row.get("jobCardDate"));
	                jobCardPcrViewDto.setDateOfInstallation((Date)row.get("dateOfInstallation"));
	                jobCardPcrViewDto.setCustomerName((String)row.get("customerName"));
	                jobCardPcrViewDto.setModel((String)row.get("model"));
	                jobCardPcrViewDto.setAddress((String)row.get("address"));
	                jobCardPcrViewDto.setMobileNo((String)row.get("mobileNo"));
	                jobCardPcrViewDto.setDateOfFailure((String)row.get("dateOfFailure"));
	                jobCardPcrViewDto.setRegistrationNumber((String)row.get("registrationNumber"));
	                jobCardPcrViewDto.setChassisNo((String)row.get("chassisNo"));
	                jobCardPcrViewDto.setEngineNo((String)row.get("engineNo"));
	                jobCardPcrViewDto.setVinNo((String)row.get("vinNo"));
	                jobCardPcrViewDto.setSoldToDealer((String)row.get("soldToDealer"));
	                jobCardPcrViewDto.setServiceDealer((String)row.get("serviceDealer"));
	                jobCardPcrViewDto.setServiceDealerAddress((String)row.get("serviceDealerAddress"));
	                jobCardPcrViewDto.setComplaintCode((String)row.get("complaintCode"));
	                jobCardPcrViewDto.setComplaintAggregate((String)row.get("complaintAggregate"));
	                jobCardPcrViewDto.setServiceCategory((String)row.get("category"));
	                jobCardPcrViewDto.setServiceType((String)row.get("serviceType"));
	                jobCardPcrViewDto.setRepairOrderType((String)row.get("repairOrder"));
//	                jobCardPcrViewDto.setMachineInventoryId((Long) row.get("machineInventoryId"));
//	                jobCardPcrViewDto.setCustomerConcern(row.get("customerConcern").toString());
//	                jobCardPcrViewDto.setMechanicObservation(row.get("mechanicObservation").toString());       
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session.isOpen())
	            session.close();
	    }

	    return jobCardPcrViewDto;
	}
	
	@Override
	public List<CustomerVoiceDto> fetchCustomerVoiceDto(String userCode, BigInteger roId, Integer flag) {
	    Session session = null;
	    List<CustomerVoiceDto> customerVoiceDtoList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sp_SV_warranty_pcr_job_card_data_for_pcr] :roId, :flag";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("roId", roId);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
//	            responseModelList = new ArrayList<>();
	            for (Object object : data) {
	                @SuppressWarnings({ "rawtypes" })
	                Map row = (Map) object;
	                CustomerVoiceDto customerVoiceDto = new CustomerVoiceDto();

//	                responseModel.setSrNo((Long) row.get("SrNo"));
	                customerVoiceDto.setCustomerConcern((String)row.get("CUSTOMER_CONCERN"));
	                customerVoiceDto.setServiceRepresentative((String)row.get("ServiceRepresentative"));
	                customerVoiceDto.setActivityToDone((String)row.get("Activity_To_Done"));

	                customerVoiceDtoList.add(customerVoiceDto);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session != null && session.isOpen()) {
	            session.close();
	        }
	    }

	    return customerVoiceDtoList;
	}
	
	
	@SuppressWarnings("null")
	@Override
	public List<JobCardPcrPartDto> jobCardPcrPartDto(String userCode, BigInteger roId, Integer flag) {
	    Session session = null;
	    List<JobCardPcrPartDto> jobCardPcrPartDtoList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sp_SV_warranty_pcr_job_card_data_for_pcr] :roId, :flag";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("roId", roId);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                JobCardPcrPartDto jobCardPcrPartDto = new JobCardPcrPartDto();

	                jobCardPcrPartDto.setPartId((Integer)row.get("part_id"));
	                jobCardPcrPartDto.setPartNumber((String)row.get("PartNumber"));
	                jobCardPcrPartDto.setPartDesc((String)row.get("PartDesc"));
	                jobCardPcrPartDto.setClaimQty((BigDecimal) row.get("ClaimQty"));
	                jobCardPcrPartDto.setApprovedQty((BigDecimal) row.get("ApprovedQty"));
	                jobCardPcrPartDto.setFailureCode((String)row.get("FailureCode"));
	                jobCardPcrPartDto.setFailureDescription((String)row.get("FailureDesc"));

	                jobCardPcrPartDtoList.add(jobCardPcrPartDto);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session != null && session.isOpen()) {
	            session.close();
	        }
	    }

	    return jobCardPcrPartDtoList;
	}
	
	
	@Override
	public List<LabourChargeDTO> fetchLabourCharges(String userCode, BigInteger roId, Integer flag) {
	    Session session = null;
	    List<LabourChargeDTO> labourChargeDTOList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sp_SV_warranty_pcr_job_card_data_for_pcr] :roId, :flag";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("roId", roId);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
//	            responseModelList = new ArrayList<>();
	            for (Object object : data) {
	                @SuppressWarnings({ "rawtypes" })
	                Map row = (Map) object;
	                LabourChargeDTO labourChargeDTO = new LabourChargeDTO();

//	                responseModel.setSrNo((Long) row.get("SrNo"));
	                labourChargeDTO.setLabourCode((String)row.get("LabourCode"));
	                labourChargeDTO.setLabourDescription((String)row.get("LabourDesc"));
	                labourChargeDTO.setHours((BigInteger) row.get("hours"));
	                labourChargeDTO.setClaimQty((Integer) row.get("ClaimQty"));
	                labourChargeDTO.setRate((BigDecimal) row.get("rate"));
	                labourChargeDTO.setTotalAmt((BigDecimal) row.get("totalAmt"));

	                labourChargeDTOList.add(labourChargeDTO);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session != null && session.isOpen()) {
	            session.close();
	        }
	    }

	    return labourChargeDTOList;
	}
	
	
	@Override
	public List<LabourChargeDTO> fetchOutsideLabourCharge(String userCode, BigInteger roId, Integer flag) {
	    Session session = null;
	    List<LabourChargeDTO> outsideLabourChargeDTOList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sp_sv_warranty_pcr_job_card_data_for_pcr] :roId, :flag";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("roId", roId);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
//	            responseModelList = new ArrayList<>();
	            for (Object object : data) {
	                @SuppressWarnings({ "rawtypes" })
	                Map row = (Map) object;
	                LabourChargeDTO outsideLabourChargeDTO = new LabourChargeDTO();

	                outsideLabourChargeDTO.setSrNo((Long) row.get("SrNo"));
	                outsideLabourChargeDTO.setLabourCode((String)row.get("LabourCode"));
	                outsideLabourChargeDTO.setLabourDescription((String)row.get("LabourDesc"));
	                outsideLabourChargeDTO.setHours((BigInteger) row.get("hours"));
	                outsideLabourChargeDTO.setClaimQty((Integer) row.get("ClaimQty"));
	                outsideLabourChargeDTO.setRate((BigDecimal) row.get("rate"));
	                outsideLabourChargeDTO.setTotalAmt((BigDecimal) row.get("totalAmt"));

	                outsideLabourChargeDTOList.add(outsideLabourChargeDTO);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session != null && session.isOpen()) {
	            session.close();
	        }
	    }

	    return outsideLabourChargeDTOList;
	}
	
	
	@Override
	public List<ServiceHistoryDto> fetchServiceHistoryDto(String userCode, BigInteger roId, Integer flag) {
	    Session session = null;
	    List<ServiceHistoryDto> serviceHistoryDtoList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sp_sv_warranty_pcr_job_card_data_for_pcr] :roId, :flag";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("roId", roId);
	        query.setParameter("flag", flag);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
//	            responseModelList = new ArrayList<>();
	            for (Object object : data) {
	                @SuppressWarnings({ "rawtypes" })
	                Map row = (Map) object;
	                ServiceHistoryDto serviceHistoryDto = new ServiceHistoryDto();

	                serviceHistoryDto.setTypeOfService((String)row.get("TypeOfService"));
	                serviceHistoryDto.setHour((BigInteger)row.get("hour"));
	                serviceHistoryDto.setJobcardNo((String)row.get("JobcardNumber"));
	                serviceHistoryDto.setJobcardDate((Date)row.get("JobcardDate"));;
	                serviceHistoryDto.setPcrNo((String)row.get("PcrNo"));
	                serviceHistoryDto.setPcrDate((Date)row.get("PcrDate"));

	                serviceHistoryDtoList.add(serviceHistoryDto);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session != null && session.isOpen()) {
	            session.close();
	        }
	    }

	    return serviceHistoryDtoList;
	}
	
	
	@Override
	public PcrCreateResponseDto createPCR(String authorizationHeader, String userCode, ServiceWarrantyPcr requestModel,
			Device device, List<MultipartFile> files) {	
		if (logger.isDebugEnabled()) {
			logger.debug("createPCR invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
//		PcrCreateResponseDto responseModelList = new PcrCreateResponseDto();
		PcrCreateResponseDto responseModel = new PcrCreateResponseDto();
		String pcrNo = null;
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			BigInteger branchId = requestModel.getBranchId();
			if (requestModel != null) {
				pcrNo = commonDao.getDocumentNumberById("PCR", branchId, session);
				System.out.println("pcrNo1"+pcrNo);
				commonDao.updateDocumentNumber("Product  Concern Report",branchId, pcrNo, session);
				System.out.println("pcrNo2"+pcrNo);
			
				requestModel.setPcrNo(pcrNo);	
		        session.save(requestModel);
		        
		        BigInteger pcrId = requestModel.getId();
		        
		        
		        //Saving photo
		        if (files.size() <= 5 && !files.isEmpty()) {
		        	for(MultipartFile file: files){
		                WarrantyPcrPhotos warrantyPcrPhotos = new WarrantyPcrPhotos();
		                String pcrPhoto = file.getOriginalFilename();
		                String photoName = "PCR" + System.currentTimeMillis() + "_" + pcrPhoto;
		                utils.store(file, "pcr",pcrId, photoName); //added pcrId by Mahesh.Kumar on 01-03-2025
		                warrantyPcrPhotos.setFileContentType(file.getContentType());
		                warrantyPcrPhotos.setFileName(photoName);
		                warrantyPcrPhotos.setServiceWarrantyPcr(requestModel);
		                
		                session.save(warrantyPcrPhotos);
		            }
		        }
		        
		        
		        //Setting hierarchy in table
		        List<WarrantyPcrApprovalEntity> warrantyPcrApprovals = fetchApprovalEntities(session, requestModel);
		        
		        if (!warrantyPcrApprovals.isEmpty()) {
		            for (WarrantyPcrApprovalEntity approvalEntity : warrantyPcrApprovals) {
		                session.save(approvalEntity);
		            }
		        }
		        
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

				if (requestModel != null) {
					responseModel.setRoId(requestModel.getServiceJobCardId());
					responseModel.setPcrNo(requestModel.getPcrNo());
					responseModel.setMsg("PCR Number Created Successfully.");
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
//					responseModelList.add(responseModel);
				}
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
//				responseModelList.add(responseModel);
			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
		
	}
	
	private List<WarrantyPcrApprovalEntity> fetchApprovalEntities(Session session, ServiceWarrantyPcr requestModel) {
	    List<WarrantyPcrApprovalEntity> warrantyPcrApprovals = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sp_wa_pcr_get_approval_hierarchy_level]";
	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                WarrantyPcrApprovalEntity hierarchyData = new WarrantyPcrApprovalEntity();
	                hierarchyData.setWarrantyPcrId(requestModel.getId());
	                hierarchyData.setApproverLevelSeq((Integer)row.get("approver_level_seq"));
	                hierarchyData.setDesignationLevelId((Integer)row.get("designation_level_id"));
	                hierarchyData.setGrpSeqNo((Integer)row.get("grp_seq_no"));
	                hierarchyData.setApprovalStatus((String)row.get("approvalStatus"));
	                hierarchyData.setIsfinalapprovalstatus((Character)row.get("isFinalApprovalStatus"));
	                hierarchyData.setRejectedFlag('N');
	                warrantyPcrApprovals.add(hierarchyData);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    }
	    return warrantyPcrApprovals;
	}
	
	@Override
	public List<Map<String, Object>> autoSearchJcNo(String roNo) {
	    Session session = null;
	    List<Map<String, Object>> responseModelList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sv_wa_pcr_autosearch_jobcard_No_for_pcr] :roNo";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("roNo", roNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("Id", row.get("Id"));
	                responseModel.put("RONumber", row.get("RONumber"));
	                responseModelList.add(responseModel);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session.isOpen())
	            session.close();
	    }

	    return responseModelList;
	}
	
	@Override
	public List<Map<String, Object>> autoSearchPcrNo(String pcrNo) {
	    Session session = null;
	    List<Map<String, Object>> responseModelList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sv_wa_pcr_autosearch_pcr_No] :pcrNo";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("pcrNo", pcrNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("id", row.get("id"));
	                responseModel.put("pcrNo", row.get("pcrNo"));
	                responseModelList.add(responseModel);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session.isOpen())
	            session.close();
	    }

	    return responseModelList;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public PcrApprovalResponseDto approveRejectPCR(String userCode, PCRApprovalRequestDto requestModel) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		PcrApprovalResponseDto responseModel = new PcrApprovalResponseDto();
		boolean isSuccess = true;
		String sqlQuery = "exec [SP_SV_WA_PCR_APPROVAL] :hoUserId, :pcrId, :approvalStatus, :remark, :rejectReason";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			String msg = null;
			String approvalStatus = null;
//			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			mapData = commonDao.fetchHOUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
//				userId = (BigInteger) mapData.get("userId");
				
//				if (mapData != null && mapData.get("SUCCESS") != null) {
					BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
					query = session.createNativeQuery(sqlQuery);
					query.setParameter("hoUserId", hoUserId);
					query.setParameter("pcrId", requestModel.getPcrHdrId());
					query.setParameter("approvalStatus", requestModel.getApprovalStatus());
					query.setParameter("remark", requestModel.getRemarks());
					query.setParameter("rejectReason", requestModel.getRejectReason());
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					List data = query.list();
					if (data != null && !data.isEmpty()) {
						for (Object object : data) {
							Map row = (Map) object;
							msg = (String) row.get("msg");
							responseModel.setMsg(msg);
							approvalStatus = (String) row.get("approvalStatus");
							responseModel.setApprovalStatus(approvalStatus);
						}
					} else {
						isSuccess = false;
						responseModel.setMsg("Error While Validating PCR Approval.");
					}
//				} else {
//					isSuccess = false;
//					responseModel.setMsg("HO User Not Found.");
//				}
			} else {
				isSuccess = false;
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
				transaction.commit();
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				responseModel.setApprovalStatus(approvalStatus);
				responseModel.setMsg(msg);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}
	
	@Override
	public JobCardPcrViewDto viewPCR(String userCode, BigInteger pcrId) {
		
		Session session = null;
		JobCardPcrViewDto response = new JobCardPcrViewDto();
	    List<CustomerVoiceDto> customerVoiceDtoList;
	    List<ServiceHistoryDto> serviceHistoryDtoList;
	    List<LabourChargeDTO> outSideLabourChargeList;
	    List<LabourChargeDTO> labourChargeList;
	    List<JobCardPcrPartDto> JobCardPcrPartDtoList;		//failure part
	    List<WarrantyPcrPhotos> warrantyPcrPhotosList;
		
		String sqlQuery = "exec [sp_SV_warranty_pcr_job_card_data_for_pcr_detail] :pcrId, :flag";
		
		try {
			session = sessionFactory.openSession();
			
			List<?> data = null;
			NativeQuery<?> query = null;
			
			//For flag 1
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pcrId", pcrId);
			query.setParameter("flag", 1);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			
			data = query.list();
			
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					response.setPcrNo((String)row.get("pcrNo"));
					response.setPcrDate((Date)row.get("pcrDate"));
					response.setWcrId((BigInteger)row.get("wcrId"));
					response.setWcrNo((String)row.get("wcrNo"));
					response.setWcrStatus((String)row.get("wcrStatus"));
					response.setFinalStatus((String)row.get("finalStatus"));
					response.setGoodwillId((BigInteger)row.get("goodwillId"));
					response.setGoodwillNo((String)row.get("goodwillNo"));
					response.setJobCardNo((String)row.get("jobCardNo"));
					response.setJobCardDate((String)row.get("jobCardDate"));
					response.setDateOfInstallation((Date)row.get("dateOfInstallation"));
					response.setCustomerName((String)row.get("CustomerName"));
					response.setTotalHour((BigInteger)row.get("Hours"));
					response.setStatus((String)row.get("status"));
					response.setModel((String)row.get("model"));
					response.setAddress((String)row.get("address"));
					response.setMobileNo((String)row.get("mobileNo"));
					response.setDateOfFailure((String)row.get("dateOfFailure"));
					response.setFailureType((String)row.get("failureType"));
					response.setProductType((String)row.get("productType"));
//					response.setNatureOfFailure((String)row.get("natureOfFailure"));
					response.setChassisNo((String)row.get("chassisNo"));
					response.setEngineNo((String)row.get("engineNo"));
					response.setVinNo((String)row.get("vinNo"));
					response.setRegistrationNumber((String)row.get("registrationNumber"));
					response.setSoldToDealer((String)row.get("soldToDealer"));
					response.setServiceDealer((String)row.get("serviceDealer"));
					response.setServiceDealerAddress((String)row.get("serviceDealerAddress"));
					response.setComplaintCodeId((BigInteger)row.get("complaintCodeId"));
					response.setComplaintCode((String)row.get("complaintCode"));
					response.setComplaintAggregateId((Integer)row.get("complaintAggregateId"));
					response.setComplaintAggregate((String)row.get("complaintAggregate"));
					response.setServiceCategory((String)row.get("category"));
					response.setServiceType((String)row.get("serviceType"));
					response.setRepairOrderType((String)row.get("repairOrder"));
					response.setRemark((String)row.get("remark"));
					response.setRejectedReason((String)row.get("rejectedReason"));
				}
			}
			
			//For flag 2
			query = null;
			data = null;
			
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pcrId", pcrId);
			query.setParameter("flag", 2);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			
			data = query.list();
			
			if (data != null && !data.isEmpty()) {
				customerVoiceDtoList = new ArrayList<>();
				
				for (Object object : data) {
					Map row = (Map) object;
					CustomerVoiceDto customerVoiceDto = new CustomerVoiceDto();
					customerVoiceDto.setCustomerConcern((String)row.get("customerConcern"));
					customerVoiceDto.setServiceRepresentative((String)row.get("serviceRepresentative"));;
					customerVoiceDto.setActivityToDone((String)row.get("activityToDone"));
					
					customerVoiceDtoList.add(customerVoiceDto);
				}
				response.setCustomerVoiceDto(customerVoiceDtoList);
			}
			
			//For flag 3
			query = null;
			data = null;
			
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pcrId", pcrId);
			query.setParameter("flag", 3);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			
			data = query.list();
			
			if (data != null && !data.isEmpty()) {
				outSideLabourChargeList = new ArrayList<>();
				
				for (Object object : data) {
					Map row = (Map) object;
					LabourChargeDTO outSideLabourCharge = new LabourChargeDTO();
					outSideLabourCharge.setLabourCode((String)row.get("LabourCode"));
					outSideLabourCharge.setLabourDescription((String)row.get("LabourDesc"));
					outSideLabourCharge.setHours((BigInteger)row.get("hours"));
					outSideLabourCharge.setClaimQty((int)row.get("ClaimQty"));
					outSideLabourCharge.setRate((BigDecimal)row.get("rate"));
					outSideLabourCharge.setTotalAmt((BigDecimal)row.get("totalAmt"));
					
					outSideLabourChargeList.add(outSideLabourCharge);
				}
				response.setOutSideLabourCharge(outSideLabourChargeList);
			}
			
			//For flag 4
			query = null;
			data = null;
			
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pcrId", pcrId);
			query.setParameter("flag", 4);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			
			data = query.list();
			
			if (data != null && !data.isEmpty()) {
				labourChargeList = new ArrayList<>();
				
				for (Object object : data) {
					Map row = (Map) object;
					LabourChargeDTO labourCharge = new LabourChargeDTO();
					labourCharge.setLabourCode((String)row.get("LabourCode"));
					labourCharge.setLabourDescription((String)row.get("LabourDesc"));
					labourCharge.setHours((BigInteger)row.get("hours"));
					labourCharge.setClaimQty((int)row.get("ClaimQty"));
					labourCharge.setRate((BigDecimal)row.get("rate"));
					labourCharge.setTotalAmt((BigDecimal)row.get("totalAmt"));
					
					labourChargeList.add(labourCharge);
				}
				response.setLabourCharge(labourChargeList);
			}
			
			
			//For flag 5
			query = null;
			data = null;
			
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pcrId", pcrId);
			query.setParameter("flag", 5);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			
			data = query.list();
			
			if (data != null && !data.isEmpty()) {
				JobCardPcrPartDtoList = new ArrayList<>();
				
				for (Object object : data) {
					Map row = (Map) object;
					JobCardPcrPartDto jobCardPcrPartDto = new JobCardPcrPartDto();
					jobCardPcrPartDto.setPartId((int)row.get("partId"));
					jobCardPcrPartDto.setPartNumber((String)row.get("partNumber"));
					jobCardPcrPartDto.setPartDesc((String)row.get("partDesc"));
					jobCardPcrPartDto.setClaimQty((BigDecimal)row.get("ClaimQty"));
					jobCardPcrPartDto.setApprovedQty((BigDecimal)row.get("approvedQty"));
					jobCardPcrPartDto.setFailureType((String)row.get("Failuretype"));
					jobCardPcrPartDto.setFailureCode((String)row.get("failureCode"));
					jobCardPcrPartDto.setFailureDescription((String)row.get("failureDesc"));
					
					JobCardPcrPartDtoList.add(jobCardPcrPartDto);
				}
				response.setJobCardPcrPartDto(JobCardPcrPartDtoList);
			}
			
			//For flag 6
			query = null;
			data = null;
			
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pcrId", pcrId);
			query.setParameter("flag", 6);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			
			data = query.list();
			
			if (data != null && !data.isEmpty()) {
				serviceHistoryDtoList = new ArrayList<>();
				
				for (Object object : data) {
					Map row = (Map) object;
					ServiceHistoryDto serviceHistoryDto = new ServiceHistoryDto();
					serviceHistoryDto.setTypeOfService((String)row.get("TypeOfService"));
	                serviceHistoryDto.setHour((BigInteger)row.get("hour"));
	                serviceHistoryDto.setJobcardNo((String)row.get("JobcardNumber"));
	                serviceHistoryDto.setJobcardDate((Date)row.get("JobcardDate"));;
	                serviceHistoryDto.setPcrNo((String)row.get("PcrNo"));
	                serviceHistoryDto.setPcrDate((Date)row.get("PcrDate"));

	                serviceHistoryDtoList.add(serviceHistoryDto);
				}
				response.setServiceHistoryDto(serviceHistoryDtoList);
			}
			
			//For flag 7
			query = null;
			data = null;
			
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pcrId", pcrId);
			query.setParameter("flag", 7);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			
			data = query.list();
			
			//added by mahesh.kumar on 28-02-2025
			String filePath = "";
			
			if(System.getProperty("os.name").toLowerCase().startsWith("windows")) {
				filePath = "C:/VST-DMS-APPS/FILES/Template/pcr/Image/"+pcrId + "/";
			}else {
				filePath = "/var/VST-DMS-APPS/FILES/Template/pcr/Image/"+pcrId + "/";
			}
			
			
			if (data != null && !data.isEmpty()) {
				warrantyPcrPhotosList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					WarrantyPcrPhotos warrantyPcrPhotos = new WarrantyPcrPhotos();
					warrantyPcrPhotos.setId((BigInteger)row.get("id"));
					warrantyPcrPhotos.setFileName((String)row.get("fileName"));
					
					warrantyPcrPhotos.setFilePath(filePath);//added by mahesh.kumar on 28-02-2025
					
					warrantyPcrPhotosList.add(warrantyPcrPhotos);
				}
				response.setWarrantyPcrPhotos(warrantyPcrPhotosList);
			}
			
		} 
		catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return response;
	}
	
	
	@Override
	public List<Map<String, Object>> getRejectedReason() {
	    Session session = null;
	    List<Map<String, Object>> responseModelList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [SV_WA_GET_REJECTION_REASON]";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("id", row.get("id"));
	                responseModel.put("rejectionReason", row.get("rejection_reason"));
	                responseModelList.add(responseModel);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session.isOpen())
	            session.close();
	    }

	    return responseModelList;
	}
	
	
	@Override
	public List<Map<String, Object>> getProductType() {
	    Session session = null;
	    List<Map<String, Object>> responseModelList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [SV_WA_GET_PRODUCT_TYPE]";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("id", row.get("id"));
	                responseModel.put("productType", row.get("product"));
	                responseModelList.add(responseModel);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session.isOpen())
	            session.close();
	    }

	    return responseModelList;
	}
	
	@Override
	public List<Map<String, Object>> getDefectCode(Integer prodId, String userCode) {
	    Session session = null;
	    List<Map<String, Object>> responseModelList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [SV_WA_GET_DEFECT_CODE] :prodId";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("prodId", prodId);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("defectId", row.get("id"));
	                responseModel.put("defectCode", row.get("defect_code"));
	                responseModelList.add(responseModel);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session.isOpen())
	            session.close();
	    }

	    return responseModelList;
	}
	
	
	@Override
	public List<Map<String, Object>> getDefectDesc(Integer defectId, String userCode) {
	    Session session = null;
	    List<Map<String, Object>> responseModelList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [SV_WA_GET_DEFECT_DESC] :defectId";
	    try {
	        session = sessionFactory.openSession();
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("defectId", defectId);
//	        query.setParameter("prodId", prodId);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                Map<String, Object> responseModel = new HashMap<>();
	                responseModel.put("defectId", row.get("id"));
	                responseModel.put("defectDesc", row.get("defect_description"));
	                responseModelList.add(responseModel);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    } finally {
	        if (session.isOpen())
	            session.close();
	    }

	    return responseModelList;
	}

}
