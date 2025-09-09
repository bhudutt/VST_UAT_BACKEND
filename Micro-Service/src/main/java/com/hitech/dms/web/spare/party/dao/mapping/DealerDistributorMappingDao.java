package com.hitech.dms.web.spare.party.dao.mapping;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.baymaster.responselist.BayTypeModel;
import com.hitech.dms.web.spare.party.model.mapping.request.DealerDistributorMappingRequest;
import com.hitech.dms.web.spare.party.model.mapping.response.DealerCodeSearchResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DealerDistributorMappingResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DealerMappingHeaderResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DistributorDetailResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DistributorMappingResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyCodeSearchResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyDetailResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyDistributorMappingResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyMappingResponse;

public interface DealerDistributorMappingDao {

	List<DealerMappingHeaderResponse> fetchHeaderList(String lookupTypeCode);

	List<DealerCodeSearchResponse> searchDealerDistributor(String isFor, String searchText, String userCode);

	DistributorDetailResponse fetchDistributorDetails(Integer distributorId);

	DealerDistributorMappingResponse createDealerDistributorMapping(String userCode,
			DealerDistributorMappingRequest dealerDistributorMappingRequest);

	DealerDistributorMappingResponse deleteDealerDistributorMapping(List<Integer> idList);

	List<DistributorMappingResponse> fetchDealerDistributorMappingList(Integer partyCategoryId,
			Integer hdrId);

	List<DealerCodeSearchResponse> searchDistributorList(Integer parentDealerId, String searchText, String isFor, String userCode);

	BigInteger checkIfDealerDistributorDtlExist(Integer headerId, Integer parentDealerId);

	boolean checkIfDealerDistributorMappingAlreadyExist(BigInteger id, Integer distributorId);

//	DistributorDetailResponse fetchDistributorDetails(Integer distributorId, Integer headerId, Integer parentDealerId);



}
