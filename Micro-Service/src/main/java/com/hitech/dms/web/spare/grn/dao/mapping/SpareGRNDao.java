package com.hitech.dms.web.spare.grn.dao.mapping;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.spare.grn.model.mapping.response.InvoiceNumberResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.PartyCodeDetailResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnDetailsResponse;
import com.hitech.dms.web.spare.grn.model.mapping.request.SpareGrnRequest;
import com.hitech.dms.web.spare.grn.model.mapping.response.BinLocationListResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnFromResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnPODetailsReponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnPONumberReponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.StoreResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DealerDistributorMappingResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyCodeSearchResponse;

public interface SpareGRNDao {

	List<SpareGrnFromResponse> fetchGrnFromList(int dealerId, String userCode);

	List<PartyCodeSearchResponse> searchPartyCode(String searchText, String userCode);

	PartyCodeDetailResponse fetchPartyDetails(BigInteger partyCategoryId);

	List<InvoiceNumberResponse> searchInvoiceNumber(String searchText, String userCode);

	InvoiceNumberResponse fetchInvoiceDetails(String InvoiceNo);

	List<BinLocationListResponse> searchBinLocation(String searchText, String userCode, int branchStoreId, String invoiceNo);

	List<PartNumberDetailResponse> fetchBinDetails(String invoiceNo, int grnTypeId);


	List<StoreResponse> fetchStoreList(String userCode);

	SpareGrnResponse createSpareGrn(String userCode, SpareGrnRequest spareGrnRequest);
	
//	BigInteger checkIfSpareGrnHdrAlreadyExist(BigInteger branchStoreId, BigInteger grnTypeId, 
//			BigInteger branchId, BigInteger partyBranchId, String invoiceNumber);
//
//	boolean checkIfSpareGrnDtlMappingAlreadyExist(BigInteger id, int stockBinId);

	SpareGrnResponse generateGrnNumber(Integer branchId);

	List<SpareGrnPONumberReponse> searchPONumber(String searchText, String userCode);

	SpareGrnPODetailsReponse fetchPODetails(BigInteger poHdrId);

	List<String> searchGrnOrInvoiceNumber(String searchType, String searchText);

	List<String> searchInvoiceNumberByGrn(String grnNumber, String searchText);

	List<SpareGrnDetailsResponse> fetchGrnDetails(String grnNumber, String invoiceNo,
			Date fromDate, Date toDate) throws ParseException;
	
}
