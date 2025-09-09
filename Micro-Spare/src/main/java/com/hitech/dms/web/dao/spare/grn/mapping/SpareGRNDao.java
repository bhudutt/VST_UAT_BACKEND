package com.hitech.dms.web.dao.spare.grn.mapping;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.spare.grn.mapping.request.SpareGrnRequest;
import com.hitech.dms.web.model.spare.grn.mapping.response.BinLocationListResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.InvoiceNumberResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.PartyCodeDetailResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnDetailsResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnFromResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnPODetailsReponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnPONumberReponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.model.spare.grn.mapping.response.StoreResponse;
import com.hitech.dms.web.model.spare.inventory.response.SpareGrnInventoryResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DealerDistributorMappingResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCodeSearchResponse;

public interface SpareGRNDao {

	ApiResponse<List<SpareGrnFromResponse>> fetchGrnFromList(int dealerId, String userCode);

	SpareGrnFromResponse fetchGrnFromDetails(int dealerId, int grnTypeId, String userCode);

	List<PartyCodeSearchResponse> searchPartyCode(String searchText, String userCode);

	PartyCodeDetailResponse fetchPartyDetails(Integer partyCategoryId);

	List<InvoiceNumberResponse> searchInvoiceNumber(String searchText, String grnType, String userCode, String dealerCode, String partyCode);

	InvoiceNumberResponse fetchInvoiceDetails(String InvoiceNo, String grnType);

	List<BinLocationListResponse> searchBinLocation(String searchText, String userCode, int branchStoreId, String invoiceNo);

	List<PartNumberDetailResponse> fetchBinDetails(String invoiceNo, int grnTypeId, String userCode);

	List<StoreResponse> fetchStoreList(String userCode);

	List<StoreResponse> fetchStoreListByPartId(String userCode, Integer partId);
	
	HashMap<BigInteger, String> fetchBinByStore(Integer branchStoreId, Integer partId);
	
	SpareGrnResponse createSpareGrn(String userCode, SpareGrnRequest spareGrnRequest);
	
//	BigInteger checkIfSpareGrnHdrAlreadyExist(BigInteger branchStoreId, BigInteger grnTypeId, 
//			BigInteger branchId, BigInteger partyBranchId, String invoiceNumber);
//
//	boolean checkIfSpareGrnDtlMappingAlreadyExist(BigInteger id, int stockBinId);

//	SpareGrnResponse generateGrnNumber(Integer branchId);

	List<SpareGrnPONumberReponse> searchPONumber(String searchText, String grnType, String categoryCode,
			String partyCode, String userCode, Integer branchId, String isFor);

	List<SpareGrnPODetailsReponse> fetchPODetails(BigInteger poHdrId);

	List<String> searchGrnOrInvoiceNumber(String searchType, String searchText, String userCode);
	List<String> searchClaimOrInvoiceNumber(String searchType, String searchText, String userCode);


	List<String> searchInvoiceNumberByGrn(String grnNumber, String searchText);

	ApiResponse<List<SpareGrnDetailsResponse>> fetchGrnDetailsList(String grnNumber, String invoiceNo, String poNumber,
			Date fromDate, Date toDate, String userCode, Integer page, Integer size) throws ParseException;

	SpareGrnDetailsResponse fetchGrnHdrAndDtl(int grnHdrId, String pageName);	
}
