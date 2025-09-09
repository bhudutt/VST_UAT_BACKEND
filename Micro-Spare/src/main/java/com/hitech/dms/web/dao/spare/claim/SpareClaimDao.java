package com.hitech.dms.web.dao.spare.claim;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.common.GeneratedNumberModel;
import com.hitech.dms.web.model.spare.claim.request.AgreeOrDisagreeClaimRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimSearchRequest;
import com.hitech.dms.web.model.spare.claim.request.SpareClaimUpdateRequest;
import com.hitech.dms.web.model.spare.claim.response.SpareGrnClaimResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnDetailsResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.inventory.request.SpareInventoryRequest;
import com.hitech.dms.web.model.spare.inventory.response.SpareGrnInventoryResponse;

public interface SpareClaimDao {

	SpareGrnResponse createSpareClaim(String fileName, String fileType, String userCode,
			SpareClaimRequest spareClaimRequest);

	HashMap<BigInteger, String> fetchClaimType();

	ApiResponse<List<SpareGrnClaimResponse>> fetchGrnList(SpareClaimSearchRequest spareClaimSearchRequest,
			String userCode) throws ParseException;

	HashMap<BigInteger, String> searchGrnNumberForClaimType(String searchType, String searchText, String claimType,
			String userCode);

	SpareGrnResponse updateSpareClaim(String userCode, SpareClaimUpdateRequest spareClaimUpdateRequest);

	SpareGrnResponse agreeOrDisagree(String userCode, AgreeOrDisagreeClaimRequest agreeOrDisagreeRequest);

	List<PartNumberDetailResponse> fetchGrnPartDetails(int grnHdrId, String pageOrClaimType);

	SpareGrnResponse approveOrReject(String userCode, String claimStatus, BigInteger grnHdrId, String claimType,
			BigInteger branchId);

	SpareGrnClaimResponse fetchGrnDetails(int grnHdrId, String pageName);

	HashMap<BigInteger, String> searchClaimNumber(String searchType, String searchText, String userCode);

	SpareGrnResponse ReSubmitSpareClaim(String fileName, String fileType, String userCode,
			SpareClaimRequest spareClaimRequest);

	SpareGrnClaimResponse fetchClaimDetails(int claimHdrId);

	List<PartNumberDetailResponse> fetchGrnClaimPartDetails(int claimHdrId, String pageOrClaimType);

//	BigInteger saveFileDetails(String fileName, String contentType, String userCode,
//			SpareClaimRequest spareClaimRequest);

}
