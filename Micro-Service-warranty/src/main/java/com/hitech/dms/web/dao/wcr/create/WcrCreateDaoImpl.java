package com.hitech.dms.web.dao.wcr.create;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.dao.pcr.pcrSearchDaoImpl;
import com.hitech.dms.web.entity.pcr.ServiceWarrantyPcr;
import com.hitech.dms.web.entity.pcr.WarrantyPcrApprovalEntity;
import com.hitech.dms.web.entity.wcr.WarrantyWcrApproval;
import com.hitech.dms.web.entity.wcr.WarrantyWcrEntity;
import com.hitech.dms.web.model.pcr.CustomerVoiceDto;
import com.hitech.dms.web.model.pcr.JobCardPcrDto;
import com.hitech.dms.web.model.pcr.JobCardPcrPartDto;
import com.hitech.dms.web.model.pcr.JobCardPcrViewDto;
import com.hitech.dms.web.model.pcr.LabourChargeDTO;
import com.hitech.dms.web.model.pcr.ServiceHistoryDto;
import com.hitech.dms.web.model.wcr.create.WcrCreateResponseDto;

@SuppressWarnings("deprecation")
@Repository
public class WcrCreateDaoImpl implements WcrCreateDao{
	
private static final Logger logger = LoggerFactory.getLogger(pcrSearchDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CommonDao commonDao;
	
	
	@SuppressWarnings({ "unused"})
	@Override
	public JobCardPcrViewDto fetchJobCardPcrView(String userCode, BigInteger roId, Integer flag) {
	    Session session = null;
	    Map<String, Object> responseModel = null;
	    JobCardPcrViewDto jobCardPcrViewDto = null;
	    List<JobCardPcrDto> responseModelList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sp_SV_warranty_pcr_job_card_data_for_wcr] :roId, :flag";
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
	                jobCardPcrViewDto.setPcrNo(row.get("pcr_no").toString());
//	                jobCardPcrViewDto.setTotalHour((Double) row.get("totalHour"));
	                jobCardPcrViewDto.setJobCardNo(row.get("jobCardNo").toString());
	                jobCardPcrViewDto.setJobCardDate(row.get("jobCardDate").toString());
//	                jobCardPcrViewDto.setDateOfInstallation(row.get("dateOfInstallation").toString());
	                jobCardPcrViewDto.setCustomerName(row.get("customerName").toString());
	                jobCardPcrViewDto.setModel(row.get("model").toString());
	                jobCardPcrViewDto.setAddress(row.get("address").toString());
	                jobCardPcrViewDto.setMobileNo(row.get("mobileNo").toString());
	                jobCardPcrViewDto.setDateOfFailure(row.get("dateOfFailure").toString());
	                jobCardPcrViewDto.setRegistrationNumber(row.get("registrationNumber").toString());
	                jobCardPcrViewDto.setChassisNo(row.get("chassisNo").toString());
	                jobCardPcrViewDto.setEngineNo(row.get("engineNo").toString());
	                jobCardPcrViewDto.setVinNo(row.get("vinNo").toString());
	                jobCardPcrViewDto.setSoldToDealer(row.get("soldToDealer").toString());
	                jobCardPcrViewDto.setServiceDealer(row.get("serviceDealer").toString());
	                jobCardPcrViewDto.setServiceDealerAddress(row.get("serviceDealerAddress").toString());
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
	    String sqlQuery = "exec [sp_SV_warranty_pcr_job_card_data_for_wcr] :roId, :flag";
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
	                customerVoiceDto.setCustomerConcern(row.get("CUSTOMER_CONCERN").toString());
	                customerVoiceDto.setServiceRepresentative(row.get("ServiceRepresentative").toString());
	                customerVoiceDto.setActivityToDone(row.get("Activity_To_Done").toString());

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
	
	
	@SuppressWarnings({ "null" })
	@Override
	public List<JobCardPcrPartDto> jobCardPcrPartDto(String userCode, BigInteger roId, Integer flag) {
	    Session session = null;
	    List<JobCardPcrPartDto> jobCardPcrPartDtoList = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sp_SV_warranty_pcr_job_card_data_for_wcr] :roId, :flag";
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
	                jobCardPcrPartDto.setPartNumber(row.get("PartNumber").toString());
	                jobCardPcrPartDto.setPartDesc(row.get("PartDesc").toString());
	                jobCardPcrPartDto.setClaimQty((BigDecimal) row.get("ClaimQty"));
	                jobCardPcrPartDto.setApprovedQty((BigDecimal) row.get("ApprovedQty"));
	                jobCardPcrPartDto.setFailureCode(row.get("FailureCode").toString());
	                jobCardPcrPartDto.setFailureDescription(row.get("FailureDesc").toString());

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
	    String sqlQuery = "exec [sp_SV_warranty_pcr_job_card_data_for_wcr] :roId, :flag";
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
	                labourChargeDTO.setLabourCode(row.get("LabourCode").toString());
	                labourChargeDTO.setLabourDescription(row.get("LabourDesc").toString());
	                labourChargeDTO.setHours((BigInteger) row.get("total_hour"));
	                labourChargeDTO.setClaimQty((Integer) row.get("ClaimQty"));

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
	    String sqlQuery = "exec [sp_SV_warranty_pcr_job_card_data_for_wcr] :roId, :flag";
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
	                outsideLabourChargeDTO.setLabourCode(row.get("LabourCode").toString());
	                outsideLabourChargeDTO.setLabourDescription(row.get("LabourDesc").toString());
	                outsideLabourChargeDTO.setHours((BigInteger) row.get("total_hour"));
	                outsideLabourChargeDTO.setClaimQty((Integer) row.get("ClaimQty"));

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
	    String sqlQuery = "exec [sp_SV_warranty_pcr_job_card_data_for_wcr] :roId, :flag";
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
	                serviceHistoryDto.setJobcardNo(row.get("JobcardNumber").toString());
	                serviceHistoryDto.setJobcardDate((Date)row.get("JobcardDate"));;
	                serviceHistoryDto.setPcrNo(row.get("PcrNo").toString());
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
	
	@SuppressWarnings("unused")
	@Override
	public WcrCreateResponseDto createWCR(String authorizationHeader, String userCode,
			WarrantyWcrEntity requestModel, Device device) {	
		if (logger.isDebugEnabled()) {
			logger.debug("createWCR invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		WcrCreateResponseDto responseModel = new WcrCreateResponseDto();
		String wcrNo = null;
		boolean isSuccess = true;
		Map<String, Object> mapData = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);

			BigInteger branchId = requestModel.getBranchId();
			if (requestModel != null) {
				wcrNo = commonDao.getDocumentNumberById("WCR", branchId, session);
				commonDao.updateDocumentNumber("Warranty Claim Request",branchId, wcrNo, session);
			
				requestModel.setWcrNo(wcrNo);
				requestModel.setWcrDate(LocalDateTime.now());
				if(requestModel.getId() == null) {
					requestModel.setCreatedBy((BigInteger)mapData.get("userId"));
					requestModel.setCreatedOn(new Date());
		        }
				else {
					requestModel.setModifiedBy((BigInteger)mapData.get("userId"));
					requestModel.setModifiedOn(new Date());
		        }
		
				session.save(requestModel);
				
				//Setting hierarchy in table
		        List<WarrantyWcrApproval> warrantyWcrApprovals = fetchApprovalEntities(session, requestModel);
		        
		        if (!warrantyWcrApprovals.isEmpty()) {
		            for (WarrantyWcrApproval approvalEntity : warrantyWcrApprovals) {
		                session.save(approvalEntity);
		            }
		        }
				

			} else {
				isSuccess = false;
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg("Resource Not Found");
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
					responseModel.setWcrNo(requestModel.getWcrNo());
					responseModel.setMsg("WCR Number Created Successfully.");
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
	
	private List<WarrantyWcrApproval> fetchApprovalEntities(Session session, WarrantyWcrEntity requestModel) {
	    List<WarrantyWcrApproval> warrantyWcrApprovals = new ArrayList<>();
	    Query<?> query = null;
	    String sqlQuery = "exec [sp_wa_wcr_get_approval_hierarchy_level]";
	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
	        if (data != null && !data.isEmpty()) {
	            for (Object object : data) {
	                @SuppressWarnings("rawtypes")
	                Map row = (Map) object;
	                WarrantyWcrApproval hierarchyData = new WarrantyWcrApproval();
	                hierarchyData.setWarrantyWcrId(requestModel.getId());
	                hierarchyData.setApproverLevelSeq((Integer)row.get("approver_level_seq"));
	                hierarchyData.setDesignationLevelId((Integer)row.get("designation_level_id"));
	                hierarchyData.setGrpSeqNo((Integer)row.get("grp_seq_no"));
	                hierarchyData.setApprovalStatus((String)row.get("approvalStatus"));
	                hierarchyData.setIsfinalapprovalstatus((Character)row.get("isFinalApprovalStatus"));
	                hierarchyData.setRejectedFlag('N');
	                warrantyWcrApprovals.add(hierarchyData);
	            }
	        }
	    } catch (Exception e) {
	        logger.error(this.getClass().getName(), e);
	    }
	    return warrantyWcrApprovals;
	}

}
