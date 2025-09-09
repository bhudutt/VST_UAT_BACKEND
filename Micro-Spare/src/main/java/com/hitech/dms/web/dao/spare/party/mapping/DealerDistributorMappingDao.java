package com.hitech.dms.web.dao.spare.party.mapping;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.spare.party.mapping.request.DealerDistributorMappingRequest;
import com.hitech.dms.web.model.spare.party.mapping.response.DealerCodeSearchResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DealerDistributorMappingResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DealerMappingHeaderResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DistributorDetailResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DistributorMappingResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCodeSearchResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyDetailResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyDistributorMappingResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyMappingResponse;

public interface DealerDistributorMappingDao {

	List<DealerMappingHeaderResponse> fetchHeaderList(String lookupTypeCode);

	List<DealerCodeSearchResponse> searchDealerDistributor(String isFor, String searchText, String userCode);

	DistributorDetailResponse fetchDistributorDetails(Integer distributorId);

	DealerDistributorMappingResponse createDealerDistributorMapping(String userCode,
			DealerDistributorMappingRequest dealerDistributorMappingRequest);

	DealerDistributorMappingResponse deleteDealerDistributorMapping(List<Integer> idList);

	List<DistributorMappingResponse> fetchDealerDistributorMappingList(Integer partyCategoryId,
			Integer mappingToId);

	List<DealerCodeSearchResponse> searchDistributorList(Integer parentDealerId, String searchText, String isFor, String userCode);

	boolean checkIfDealerDistributorDtlExist(BigInteger id, Integer parentDealerId);

	BigInteger checkIfDealerDistributorMappingAlreadyExist(BigInteger headerId, Integer distributorId);

//	DistributorDetailResponse fetchDistributorDetails(Integer distributorId, Integer headerId, Integer parentDealerId);



}
