package com.hitech.dms.web.dao.spare.customer.order;

import java.util.List;

import com.hitech.dms.web.model.spara.customer.order.request.PartyCodeCreateRequestModel;
import com.hitech.dms.web.model.spara.customer.order.response.CustomerOrderNumberResponse;
import com.hitech.dms.web.model.spara.customer.order.response.CustomerOrderPartyCodeSearchResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.PartyCodeListResponseModel;
import com.hitech.dms.web.model.spare.party.mapping.response.PartyCategoryResponse;
/*
*
*@Author Vivek Gupta
*/
public interface PartyCodeSearchDao {

	PartyCodeListResponseModel searchPartyCodeList(String userCode, PartyCodeCreateRequestModel requestModel);

	List<PartyCategoryResponse> searchPartyCategoryMaster(String userCode);

	CustomerOrderPartyCodeSearchResponseModel partyDetailsById(String userCode, Integer partyBranchId);

	List<CustomerOrderNumberResponse> searchCustomerOrderNumber(String userCode, String userCode2);

	List<PartyCategoryResponse> partyCodeList(String searchText, String userCode);

	

}
