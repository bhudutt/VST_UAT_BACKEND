package com.hitech.dms.web.service.spareClaim;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.spare.claim.request.AgreeOrDisagreeClaimRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimSearchRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimUpdateRequest;
import com.hitech.dms.web.model.spare.claim.response.SpareGrnClaimResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.inventory.response.SpareGrnInventoryResponse;

public interface SpareClaimService {

	HashMap<BigInteger, String> fetchClaimType();

	SpareGrnResponse createSpareClaim(String userCode, SpareClaimRequest spareClaimRequest, MultipartFile file);

	ApiResponse<List<SpareGrnClaimResponse>> fetchGrnList(SpareClaimSearchRequest spareClaimSearchRequest, String userCode) throws ParseException;

	HashMap<BigInteger, String> searchGrnNumberForClaimType(String searchType, String searchText, String claimType, String userCode);

	SpareGrnResponse updateSpareClaim(String userCode, SpareClaimUpdateRequest spareClaimUpdateRequest);

	SpareGrnResponse agreeOrDisagree(String userCode, AgreeOrDisagreeClaimRequest agreeOrDisagreeRequest);

	List<PartNumberDetailResponse> fetchGrnPartDetails(int grnHdrId, String pageOrClaimType);

	SpareGrnResponse approveOrReject(String userCode, String claimStatus, BigInteger claimHdrId, String claimType,
			BigInteger branchId);

	SpareGrnClaimResponse fetchGrnDetails(int grnHdrId, String pageName);

	HashMap<BigInteger, String> searchClaimNumber(String searchType, String searchText, String userCode);

	SpareGrnResponse ReSubmitSpareClaim(String userCode, SpareClaimRequest spareClaimRequest, MultipartFile file);

	SpareGrnClaimResponse fetchClaimDetails(int claimHdrId);

	List<PartNumberDetailResponse> fetchGrnClaimPartDetails(int claimHdrId, String pageOrClaimType);

}
