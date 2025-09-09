package com.hitech.dms.web.dao.partycode.search;

import java.util.List;

import com.hitech.dms.web.model.outletCategory.search.response.PartyMasterApproveEntity;
import com.hitech.dms.web.model.partybybranch.create.request.PartyCodeCreateRequestModel;
import com.hitech.dms.web.model.partycode.search.response.DealerDetailResponse;
import com.hitech.dms.web.model.partycode.search.response.PartyCategoryResponse;
import com.hitech.dms.web.model.partycode.search.response.PartyCodeSearchMainResponseModel;
import com.hitech.dms.web.model.partycode.search.response.PartyDetailAddressResponse;
import com.hitech.dms.web.model.partycode.search.response.PartyMasterChangeStatusResponse;
import com.hitech.dms.web.model.partycode.search.response.PartyNameResponseModel;

public interface PartyCodeSearchDao {

	public PartyCodeSearchMainResponseModel searchPartyCodeList(String userCode,
			PartyCodeCreateRequestModel requestModel);
	
	public List<PartyNameResponseModel> searchPartyNameList(String userCode,Integer categoryId);
	
	public List<PartyCategoryResponse> searchPartyCategoryMaster(String userCode);
	
	public PartyMasterChangeStatusResponse updatePartyMasterActiveStatus(Integer id,String isActive);
	
	public PartyMasterChangeStatusResponse updatePartyApproveStatus(PartyMasterApproveEntity request,String userCode);

	public List<PartyDetailAddressResponse> getDealerAdressDetail(String pinId,String dealerCode,String branchId);
	
}
