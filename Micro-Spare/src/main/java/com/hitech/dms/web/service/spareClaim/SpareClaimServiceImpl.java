package com.hitech.dms.web.service.spareClaim;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.exceptions.docs.FileStorageException;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.dao.common.CommonDaoImpl;
import com.hitech.dms.web.dao.spare.claim.SpareClaimDao;
import com.hitech.dms.web.dao.spare.claim.SpareClaimDaoImpl;
import com.hitech.dms.web.model.spare.claim.request.AgreeOrDisagreeClaimRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimSearchRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimUpdateRequest;
import com.hitech.dms.web.model.spare.claim.response.SpareGrnClaimResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.inventory.response.SpareGrnInventoryResponse;

@Service
public class SpareClaimServiceImpl implements SpareClaimService {
	private static final Logger logger = LoggerFactory.getLogger(SpareClaimDaoImpl.class);

	private Path fileStorageLocation;

	@Autowired
	private SpareClaimDao spareClaimDao;

	@Value("${file.upload-dir.SpareClaimUpload}")
	private String uploadDir;

	@Override
	public HashMap<BigInteger, String> fetchClaimType() {
		return spareClaimDao.fetchClaimType();
	}

	@Override
	public SpareGrnResponse createSpareClaim(String userCode, SpareClaimRequest spareClaimRequest, MultipartFile file) {
//		String fileName = 
		return storeFile(file, uploadDir, userCode, spareClaimRequest);
//		return spareClaimDao.createSpareClaim(userCode, spareClaimRequest);
	}

	@Override
	public SpareGrnResponse ReSubmitSpareClaim(String userCode, SpareClaimRequest spareClaimRequest,
			MultipartFile file) {
		return storeFile(file, uploadDir, userCode, spareClaimRequest);
	}

	public SpareGrnResponse storeFile(MultipartFile file, String filePath, String userCode,
			SpareClaimRequest spareClaimRequest) {
		if (filePath == null) {
			filePath = uploadDir;
		}
		this.fileStorageLocation = Paths.get(filePath).toAbsolutePath().normalize();

		logger.info("file path", fileStorageLocation);
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
		// Normalize file name
		String fileName = null;
		String fileType = null;
		try {
			if (file != null) {
				fileName = StringUtils.cleanPath(file.getOriginalFilename());

				// Check if the file's name contains invalid characters
				if (fileName.contains("..")) {
					throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
				}

				// Copy file to the target location (Replacing existing file with the same name)
				Path targetLocation = this.fileStorageLocation.resolve(fileName);
				Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

				logger.info("targetLocation", targetLocation);
				fileType = file.getContentType();
			}

			if (spareClaimRequest.getGrnClaimHdrId() != null) {
				return spareClaimDao.ReSubmitSpareClaim(fileName, fileType, userCode, spareClaimRequest);
			} else {
				return spareClaimDao.createSpareClaim(fileName, fileType, userCode, spareClaimRequest);
			}

//			BigInteger id = spareClaimDao.saveFileDetails(fileName, file.getContentType(), userCode, spareClaimRequest);
//			return fileName;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	@Override
	public ApiResponse<List<SpareGrnClaimResponse>> fetchGrnList(SpareClaimSearchRequest spareClaimSearchRequest,
			String userCode) throws ParseException {
		return spareClaimDao.fetchGrnList(spareClaimSearchRequest, userCode);
	}

	@Override
	public HashMap<BigInteger, String> searchGrnNumberForClaimType(String searchType, String searchText,
			String claimType, String userCode) {
		return spareClaimDao.searchGrnNumberForClaimType(searchType, searchText, claimType, userCode);
	}

	@Override
	public SpareGrnResponse updateSpareClaim(String userCode, SpareClaimUpdateRequest spareClaimUpdateRequest) {
		return spareClaimDao.updateSpareClaim(userCode, spareClaimUpdateRequest);
	}

	@Override
	public SpareGrnResponse agreeOrDisagree(String userCode, AgreeOrDisagreeClaimRequest agreeOrDisagreeRequest) {
		return spareClaimDao.agreeOrDisagree(userCode, agreeOrDisagreeRequest);
	}

	@Override
	public List<PartNumberDetailResponse> fetchGrnPartDetails(int grnHdrId, String pageOrClaimType) {
		return spareClaimDao.fetchGrnPartDetails(grnHdrId, pageOrClaimType);
	}

	@Override
	public SpareGrnResponse approveOrReject(String userCode, String claimStatus, BigInteger claimHdrId,
			String claimType, BigInteger branchId) {
		return spareClaimDao.approveOrReject(userCode, claimStatus, claimHdrId, claimType, branchId);
	}

	@Override
	public SpareGrnClaimResponse fetchGrnDetails(int grnHdrId, String pageName) {
		return spareClaimDao.fetchGrnDetails(grnHdrId, pageName);
	}

	@Override
	public HashMap<BigInteger, String> searchClaimNumber(String searchType, String searchText, String userCode) {
		return spareClaimDao.searchClaimNumber(searchType, searchText, userCode);
	}

	@Override
	public SpareGrnClaimResponse fetchClaimDetails(int claimHdrId) {
		return spareClaimDao.fetchClaimDetails(claimHdrId);
	}

	@Override
	public List<PartNumberDetailResponse> fetchGrnClaimPartDetails(int claimHdrId, String pageOrClaimType) {
		return spareClaimDao.fetchGrnClaimPartDetails(claimHdrId, pageOrClaimType);
	}

}
