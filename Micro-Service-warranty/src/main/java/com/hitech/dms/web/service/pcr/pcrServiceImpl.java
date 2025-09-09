package com.hitech.dms.web.service.pcr;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.exceptions.docs.FileStorageException;
import com.hitech.dms.web.common.ApiResponse;
import com.hitech.dms.web.dao.pcr.pcrSearchDao;
import com.hitech.dms.web.entity.pcr.ServiceWarrantyPcr;
import com.hitech.dms.web.model.pcr.ComplaintAggregateDto;
import com.hitech.dms.web.model.pcr.CreatePcrDto;
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
import org.springframework.core.io.UrlResource;

/**
 * @author mahesh.kumar
 * @apiNote this is the service Implementation
 */
@Service
public class pcrServiceImpl implements pcrService {
	
	private Path fileStorageLocation;

	@Autowired
	private pcrSearchDao pcrSearchDao;
	
	@Value("${file.upload-dir.Temp:C:\\\\VST-DMS-APPS\\\\template\\\\pcr\\\\\\\\}")
    private String uploadDir;
	
	@Value("${file.upload-dir.Template}")
    private String imageDirectory;

	@Override
	public ApiResponse<List<PcrSearchResponseDto>> pcrSearchList(String userCode, PcrSearchRequestDto requestModel) {

		if (userCode == null || userCode.isEmpty()) {
			throw new IllegalArgumentException("Invalid userCode.");
		}

		ApiResponse<List<PcrSearchResponseDto>> responseModel = pcrSearchDao.pcrSearchList(userCode, requestModel);
		return responseModel;
	}

	@Override
	public List<Map<String, Object>> fetchAllFailureType(String userCode, BigInteger roId) {

		if (userCode == null || userCode.isEmpty()) {
			throw new IllegalArgumentException("Invalid userCode.");
		}

		List<Map<String, Object>> responseModel = pcrSearchDao.fetchAllFailureType(userCode, roId);

		return responseModel;
	}
	
	@Override
	public List<ComplaintAggregateDto> getComplaintAggregate(String authorizationHeader, String userCode) {
		List<ComplaintAggregateDto> complaintAggregate = pcrSearchDao.getComplaintAggregate(authorizationHeader,
				userCode);
		return complaintAggregate;
	}
	
	@Override
	public List<Map<String, Object>> getComplaintCode(Integer ID, String userCode) {
		List<Map<String, Object>> complaintCode = pcrSearchDao.getComplaintCode(ID,userCode);
		return complaintCode;
	}

	@Override
	public JobCardPcrDto fetchJobCardForPcr(String userCode, BigInteger roId) {

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
	    


			responseModel = pcrSearchDao.fetchJobCardPcrView(userCode, roId, 1);
			jobCardPcrDto.setJobCardPcrViewDto(responseModel);
			
			customerVoiceDto = pcrSearchDao.fetchCustomerVoiceDto(userCode, roId, 2);
			jobCardPcrDto.setCustomerVoiceDto(customerVoiceDto);
			        
			labourCharge = pcrSearchDao.fetchLabourCharges(userCode, roId, 3);
			jobCardPcrDto.setLabourCharge(labourCharge);
			        
			outSideLabourCharge = pcrSearchDao.fetchOutsideLabourCharge(userCode, roId, 4);
			jobCardPcrDto.setOutSideLabourCharge(outSideLabourCharge);
			
			jobCardPcrPartDto = pcrSearchDao.jobCardPcrPartDto(userCode, roId, 5);
			jobCardPcrDto.setJobCardPcrPartDto(jobCardPcrPartDto);
			
			serviceHistoryDto = pcrSearchDao.fetchServiceHistoryDto(userCode, roId, 6);
			jobCardPcrDto.setServiceHistoryDto(serviceHistoryDto);

	    return jobCardPcrDto;
	}

	
	
	@Override
	public PcrCreateResponseDto createPCR(String authorizationHeader, String userCode,
			ServiceWarrantyPcr requestModel, List<MultipartFile> files, Device device) {
	    
		PcrCreateResponseDto responseModel=pcrSearchDao.createPCR(authorizationHeader,userCode,requestModel,device, files);
		return responseModel;
		
	}
	
//	public List<String> storeFiles(List<MultipartFile> files, String filePath) {
//	    if (filePath == null) {
//	        filePath = uploadDir;
//	    }
//	    Path fileStorageLocation = Paths.get(filePath).toAbsolutePath().normalize();
//
//	    try {
//	        Files.createDirectories(fileStorageLocation);
//	    } catch (Exception ex) {
//	        throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
//	    }
//
//	    List<String> storedFileNames = new ArrayList<>();
//
//	    for (MultipartFile file : files) {
//	        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
//
//	        try {
//	            if (originalFileName.contains("..")) {
//	                throw new FileStorageException("Sorry! Filename contains invalid path sequence: " + originalFileName);
//	            }
//
//	            String storedFileName = "PCR" + System.currentTimeMillis() + "_" + originalFileName;
//	            Path targetLocation = fileStorageLocation.resolve(storedFileName);
//
//	            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//	            storedFileNames.add(storedFileName);
//	        } catch (IOException ex) {
//	            throw new FileStorageException("Could not store file " + originalFileName + ". Please try again!", ex);
//	        }
//	    }
//
//	    return storedFileNames;
//	}

	
//	public String storeFile(List<MultipartFile> files, String filePath) {
//		if (filePath == null) {
//			filePath = uploadDir;
//		}
//		this.fileStorageLocation = Paths.get(filePath).toAbsolutePath().normalize();
//		try {
//			Files.createDirectories(this.fileStorageLocation);
//		} catch (Exception ex) {
//			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
//					ex);
//		}
//		// Normalize file name
//		for (MultipartFile file : files) {
//			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//
//			try {
//				// Check if the file's name contains invalid characters
//				if (fileName.contains("..")) {
//					throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
//				}
//	
//				// Copy file to the target location (Replacing existing file with the same name)
//				Path targetLocation = this.fileStorageLocation.resolve(fileName);
//				Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//	
//				return fileName;
//			} catch (IOException ex) {
//				throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
//			}
//		}
//	}
	
	
	@Override
	public List<Map<String, Object>> autoSearchJcNo(String roNo) {
		List<Map<String, Object>> autoSearchJcNo = pcrSearchDao.autoSearchJcNo(roNo);
		return autoSearchJcNo;
	}
	
	@Override
	public List<Map<String, Object>> autoSearchPcrNo(String pcrNo) {
		List<Map<String, Object>> autoSearchPcrNo = pcrSearchDao.autoSearchPcrNo(pcrNo);
		return autoSearchPcrNo;
	}
	
	@Override
	public PcrApprovalResponseDto approveRejectPCR(String userCode,
			PCRApprovalRequestDto requestModel) {
		PcrApprovalResponseDto responseModel=pcrSearchDao.approveRejectPCR(userCode,requestModel);
		return responseModel;
	}
	
	
	@Override
	public JobCardPcrViewDto viewPCR(String userCode, BigInteger pcrId) {
		JobCardPcrViewDto viewPCR = pcrSearchDao.viewPCR(userCode, pcrId);
		return viewPCR;
	}
	
	@Override
	public List<Map<String, Object>> getRejectedReason() {
		List<Map<String, Object>> rejectedReason = pcrSearchDao.getRejectedReason();
		return rejectedReason;
	}
	
	@Override
	public List<Map<String, Object>> getProductType() {
		List<Map<String, Object>> productType = pcrSearchDao.getProductType();
		return productType;
	}
	
	@Override
	public List<Map<String, Object>> getDefectCode(Integer prodId, String userCode) {
		List<Map<String, Object>> defectCode = pcrSearchDao.getDefectCode(prodId,userCode);
		return defectCode;
	}
	
	@Override
	public List<Map<String, Object>> getDefectDesc(Integer defectId, String userCode) {
		List<Map<String, Object>> defectDesc = pcrSearchDao.getDefectDesc(defectId,userCode);
		return defectDesc;
	}
	
	@Override
	public Resource getImages(String fileName, BigInteger id, String moduleName, String userCode) {
	    String filePath = "";

	    if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
	        filePath = imageDirectory + moduleName + "/Image/" + id + "/";
	    } else {
	        filePath = "/var/VST-DMS-APPS/FILES/Template/" + moduleName + "/Image/" + id + "/";
	    }

	    // Build the full image path
	    Path imagePath = Paths.get(filePath + fileName); // Use filePath instead of imageDirectory

	    try {
	        // Load the image file as a resource
	        Resource resource = new UrlResource(imagePath.toUri());

	        // Check if the resource exists and is readable
	        if (resource.exists() && resource.isReadable()) {
	            return resource;
	        } else {
	            throw new IOException("Image not found or not readable.");
	        }
	    } catch (IOException e) {
	        // Catch the IOException and rethrow as a RuntimeException
	        throw new RuntimeException("Failed to load image: " + fileName, e);
	    }
	}


}
