package com.hitech.dms.web.dao.spare.party.mapping;

import java.util.List;

import com.hitech.dms.web.model.spare.party.mapping.request.PartyMappingRequest;
import com.hitech.dms.web.model.spare.party.mapping.response.DealerCodeSearchResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.DistributorDetailResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCodeSearchResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyDetailResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyDistributorMappingResponse;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyMappingResponse;

public interface PartyMappingDao {

	List<PartyCategoryResponse> fetchPartyCategoryMaster(String userCode);

	List<PartyCodeSearchResponse> searchPartyCodeByPartyCategory(int partyCategoryId, String searchText, String userCode);

	PartyDetailResponse fetchPartyDetailsByPartyCode(int partyBranchId);

	List<DealerCodeSearchResponse> searchDealers(String dealer, String userCode);

	DistributorDetailResponse fetchDealerDetailsBydealer(int parentDealerId);

	PartyMappingResponse createPartyMapping(String userCode, PartyMappingRequest partyMappingRequest);

	PartyMappingResponse deletePartyMapping(List<Integer> id, String isActive);

	List<PartyDistributorMappingResponse> fetchPartyMappingList(Integer partyCategoryId, Integer partyBranchId);

	boolean checkIfPartyMappingAlreadyExist(Integer partyBranchId, Integer partyCategoryId, Integer dealerId);
}
