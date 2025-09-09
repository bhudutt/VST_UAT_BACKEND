package com.hitech.dms.web.service.wcr;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.dao.wcr.create.WcrCreateDao;
import com.hitech.dms.web.dao.wcr.search.WcrSearchDao;
import com.hitech.dms.web.entity.goodwill.WarrantyGoodwillPhoto;
import com.hitech.dms.web.entity.pcr.ServiceWarrantyPcr;
import com.hitech.dms.web.entity.wcr.WarrantyWcrEntity;
import com.hitech.dms.web.model.goodwill.GoodwillViewDto;
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
import com.hitech.dms.web.model.wcr.create.WcrCreateResponseDto;
import com.hitech.dms.web.model.wcr.search.WCRApprovalRequestDto;
import com.hitech.dms.web.model.wcr.search.WcrApprovalResponseDto;
import com.hitech.dms.web.model.wcr.search.WcrSearchRequestDto;
import com.hitech.dms.web.model.wcr.search.WcrSearchResponseDto;

/**
 * @author mahesh.kumar
 * @apiNote this is the service Implementation
 */
@Service
public class wcrServiceImpl implements wcrService{
	
	@Autowired
	private WcrCreateDao wcrCreateDao;
	
	@Autowired
	private WcrSearchDao wcrSearchDao;
	
	@Override
	public JobCardPcrDto fetchDetailsForWcr(String userCode, BigInteger roId) {

	    if (userCode == null || userCode.isEmpty()) {
	        throw new IllegalArgumentException("Invalid userCode.");
	    }

	    JobCardPcrDto jobCardPcrDto = new JobCardPcrDto();
	    JobCardPcrViewDto responseModel = null;
	    List<CustomerVoiceDto> customerVoiceDto = new ArrayList<>();
	    List<ServiceHistoryDto> serviceHistoryDto = new ArrayList<>();
	    List<JobCardPcrPartDto> jobCardPcrPartDto = new ArrayList<>();
	    List<LabourChargeDTO> labourCharge = new ArrayList<>();
	    List<LabourChargeDTO> outSideLabourCharge = new ArrayList<>();
	    


			responseModel = wcrCreateDao.fetchJobCardPcrView(userCode, roId, 1);
			jobCardPcrDto.setJobCardPcrViewDto(responseModel);
			
			customerVoiceDto = wcrCreateDao.fetchCustomerVoiceDto(userCode, roId, 2);
			jobCardPcrDto.setCustomerVoiceDto(customerVoiceDto);
			        
			labourCharge = wcrCreateDao.fetchLabourCharges(userCode, roId, 3);
			jobCardPcrDto.setLabourCharge(labourCharge);
			        
			outSideLabourCharge = wcrCreateDao.fetchOutsideLabourCharge(userCode, roId, 4);
			jobCardPcrDto.setOutSideLabourCharge(outSideLabourCharge);
			
			jobCardPcrPartDto = wcrCreateDao.jobCardPcrPartDto(userCode, roId, 5);
			jobCardPcrDto.setJobCardPcrPartDto(jobCardPcrPartDto);
			
			serviceHistoryDto = wcrCreateDao.fetchServiceHistoryDto(userCode, roId, 6);
			jobCardPcrDto.setServiceHistoryDto(serviceHistoryDto);

	    return jobCardPcrDto;
	}
	
	@Override
	public WcrCreateResponseDto createWCR(String authorizationHeader, String userCode,
			WarrantyWcrEntity requestModel, Device device) {
		WcrCreateResponseDto responseModel=wcrCreateDao.createWCR(authorizationHeader,userCode,requestModel,device);
		return responseModel;
	}
	
	@Override
	public List<Map<String, Object>> autoSearchWcrNo(String wcrNo) {
		List<Map<String, Object>> autoSearchWcrNo = wcrSearchDao.autoSearchWcrNo(wcrNo);
		return autoSearchWcrNo;
	}
	
	@Override
	public List<Map<String, Object>> autoSearchPcrNo(String pcrNo) {
		List<Map<String, Object>> autoSearchPcrNo = wcrSearchDao.autoSearchPcrNo(pcrNo);
		return autoSearchPcrNo;
	}
	
	@Override
	public ApiResponse<List<WcrSearchResponseDto>> wcrSearchList(String userCode, WcrSearchRequestDto requestModel) {

		if (userCode == null || userCode.isEmpty()) {
			throw new IllegalArgumentException("Invalid userCode.");
		}

		ApiResponse<List<WcrSearchResponseDto>> responseModel = wcrSearchDao.wcrSearchList(userCode, requestModel);
		return responseModel;
	}
	
	
	@Override
	public WcrApprovalResponseDto approveRejectWCR(String userCode,
			WCRApprovalRequestDto requestModel) {
		WcrApprovalResponseDto responseModel=wcrSearchDao.approveRejectWCR(userCode,requestModel);
		return responseModel;
	}
	
	
//	@Override
//	public ApiResponse<?> viewWcr(BigInteger wcrId) 
//	{		
//		ApiResponse<GoodwillViewDto> apiResponse = new ApiResponse<>();
//		
//		Session session = null;
//		GoodwillViewDto goodwillViewDto = new GoodwillViewDto();
//		List<CustomerVoiceDto> customerVoiceDtoList;
//	    List<ServiceHistoryDto> serviceHistoryDtoList;
//	    List<LabourChargeDTO> outSideLabourChargeList;
//	    List<LabourChargeDTO> labourChargeList;
//	    List<JobCardPcrPartDto> JobCardPcrPartDtoList;		//failure part
//	    List<WarrantyGoodwillPhoto> warrantyGoodwillPhotoList;
//		
//		List<?> data = null;
//		
//		try {
//			session = sessionFactory.openSession();
//			
//			//For flag 1
//			data = goodwillDao.getViewData(session, goowillId, 1);
//			if (data != null && !data.isEmpty()) {
//				for (Object object : data) {
//					@SuppressWarnings("rawtypes")
//					Map row = (Map) object;
//					goodwillViewDto.setGoodwillNo((String)row.get("goodwillNo"));
//					goodwillViewDto.setGoodwillDate((String)row.get("goodwillDate"));
//					goodwillViewDto.setPcrNo((String)row.get("pcrNo"));
//					goodwillViewDto.setJobCardNo((String)row.get("jobCardNo"));
//					goodwillViewDto.setJobCardDate((String)row.get("jobCardDate"));
//					goodwillViewDto.setDateOfInstallation((String)row.get("dateOfInstallation"));
//					goodwillViewDto.setCustomerName((String)row.get("CustomerName"));
//					goodwillViewDto.setModel((String)row.get("model"));
//					goodwillViewDto.setAddress((String)row.get("address"));
//					goodwillViewDto.setMobileNo((String)row.get("mobileNo"));
//					goodwillViewDto.setDateOfFailure((String)row.get("dateOfFailure"));
//					goodwillViewDto.setChassisNo((String)row.get("chassisNo"));
//					goodwillViewDto.setEngineNo((String)row.get("engineNo"));
//					goodwillViewDto.setVinNo((String)row.get("vinNo"));
//					goodwillViewDto.setRegistrationNumber((String)row.get("registrationNumber"));
//					goodwillViewDto.setSoldToDealer((String)row.get("soldToDealer"));
//					goodwillViewDto.setServiceDealer((String)row.get("serviceDealer"));
//					goodwillViewDto.setServiceDealerAddress((String)row.get("serviceDealerAddress"));
//				}
//			}
//			
//			//For flag 2
//			data = null;
//			data = goodwillDao.getViewData(session, goowillId, 2);
//			
//			if (data != null && !data.isEmpty()) {
//				customerVoiceDtoList = new ArrayList<>();
//				
//				for (Object object : data) {
//					@SuppressWarnings("rawtypes")
//					Map row = (Map) object;
//					CustomerVoiceDto customerVoiceDto = new CustomerVoiceDto();
//					customerVoiceDto.setCustomerConcern((String)row.get("customerConcern"));
//					customerVoiceDto.setServiceRepresentative((String)row.get("serviceRepresentative"));;
//					customerVoiceDto.setActivityToDone((String)row.get("activityToDone"));
//					
//					customerVoiceDtoList.add(customerVoiceDto);
//				}
//				goodwillViewDto.setCustomerVoiceDto(customerVoiceDtoList);
//			}
//			
//			
//			
//			//For flag 3
//			data = null;
//			data = goodwillDao.getViewData(session, goowillId, 3);
//			
//			if (data != null && !data.isEmpty()) {
//				outSideLabourChargeList = new ArrayList<>();
//				
//				for (Object object : data) {
//					@SuppressWarnings("rawtypes")
//					Map row = (Map) object;
//					LabourChargeDTO outSideLabourCharge = new LabourChargeDTO();
//					outSideLabourCharge.setLabourCode((String)row.get("LabourCode"));
//					outSideLabourCharge.setLabourDescription((String)row.get("LabourDesc"));
//					outSideLabourCharge.setHours((BigInteger)row.get("total_hour"));
//					outSideLabourCharge.setClaimQty((int)row.get("ClaimQty"));
//					
//					outSideLabourChargeList.add(outSideLabourCharge);
//				}
//				goodwillViewDto.setOutSideLabourCharge(outSideLabourChargeList);
//			}
//			
//			//For flag 4
//			data = null;
//			data = goodwillDao.getViewData(session, goowillId, 4);
//			
//			if (data != null && !data.isEmpty()) {
//				labourChargeList = new ArrayList<>();
//				
//				for (Object object : data) {
//					@SuppressWarnings("rawtypes")
//					Map row = (Map) object;
//					LabourChargeDTO labourCharge = new LabourChargeDTO();
//					labourCharge.setLabourCode((String)row.get("LabourCode"));
//					labourCharge.setLabourDescription((String)row.get("LabourDesc"));
//					labourCharge.setHours((BigInteger)row.get("total_hour"));
//					labourCharge.setClaimQty((int)row.get("ClaimQty"));
//					
//					labourChargeList.add(labourCharge);
//				}
//				goodwillViewDto.setLabourCharge(labourChargeList);
//			}
//			
//			
//			//For flag 5
//			data = null;			
//			data = goodwillDao.getViewData(session, goowillId, 5);
//			
//			if (data != null && !data.isEmpty()) {
//				JobCardPcrPartDtoList = new ArrayList<>();
//				
//				for (Object object : data) {
//					@SuppressWarnings("rawtypes")
//					Map row = (Map) object;
//					JobCardPcrPartDto jobCardPcrPartDto = new JobCardPcrPartDto();
//					jobCardPcrPartDto.setPartId((int)row.get("partId"));
//					jobCardPcrPartDto.setPartNumber((String)row.get("partNumber"));
//					jobCardPcrPartDto.setPartDesc((String)row.get("partDesc"));
//					jobCardPcrPartDto.setClaimQty((BigDecimal)row.get("ClaimQty"));
//					jobCardPcrPartDto.setApprovedQty((BigDecimal)row.get("approvedQty"));
//					jobCardPcrPartDto.setFailureCode((String)row.get("failureCode"));
//					jobCardPcrPartDto.setFailureDescription((String)row.get("failureDesc"));
//					
//					JobCardPcrPartDtoList.add(jobCardPcrPartDto);
//				}
//				goodwillViewDto.setJobCardPcrPartDto(JobCardPcrPartDtoList);
//			}
//			
//			//For flag 6
//			data = null;
//			data = goodwillDao.getViewData(session, goowillId, 6);;
//			
//			if (data != null && !data.isEmpty()) {
//				serviceHistoryDtoList = new ArrayList<>();
//				
//				for (Object object : data) {
//					@SuppressWarnings("rawtypes")
//					Map row = (Map) object;
//					ServiceHistoryDto serviceHistoryDto = new ServiceHistoryDto();
//					serviceHistoryDto.setTypeOfService((String)row.get("TypeOfService"));
//	                serviceHistoryDto.setHour((BigInteger)row.get("hour"));
//	                serviceHistoryDto.setJobcardNo(row.get("JobcardNumber").toString());
//	                serviceHistoryDto.setJobcardDate((Date)row.get("JobcardDate"));;
//	                serviceHistoryDto.setPcrNo(row.get("PcrNo").toString());
//	                serviceHistoryDto.setPcrDate((Date)row.get("PcrDate"));
//
//	                serviceHistoryDtoList.add(serviceHistoryDto);
//				}
//				goodwillViewDto.setServiceHistoryDto(serviceHistoryDtoList);
//			}
//			
//			//For flag 7
//			data = null;
//			data = goodwillDao.getViewData(session, goowillId, 7);
//			
//			if (data != null && !data.isEmpty()) {
//				warrantyGoodwillPhotoList = new ArrayList<>();
//				for (Object object : data) {
//					@SuppressWarnings("rawtypes")
//					Map row = (Map) object;
//					WarrantyGoodwillPhoto warrantyGoodwillPhoto = new WarrantyGoodwillPhoto();
//					warrantyGoodwillPhoto.setId((BigInteger)row.get("id"));
//					warrantyGoodwillPhoto.setFileName((String)row.get("fileName"));
//					
//					warrantyGoodwillPhotoList.add(warrantyGoodwillPhoto);
//				}
//				goodwillViewDto.setWarrantyGoodwillPhoto(warrantyGoodwillPhotoList);
//			}
//			
//		} catch (SQLGrammarException sqlge) {
//			sqlge.printStackTrace();
//		} catch (HibernateException exp) {
//			logger.error(this.getClass().getName(), exp);
//		} catch (Exception exp) {
//			logger.error(this.getClass().getName(), exp);
//		} finally {
//
//			if (goodwillViewDto != null) {
//				apiResponse.setResult(goodwillViewDto);
//				apiResponse.setMessage("Goodwill Number Created Successfully.");
//				apiResponse.setStatus(WebConstants.STATUS_CREATED_201);
//			}
//
//			if (session != null) {
//				session.close();
//			}
//		}
//		
//		return apiResponse;
//	}

}
