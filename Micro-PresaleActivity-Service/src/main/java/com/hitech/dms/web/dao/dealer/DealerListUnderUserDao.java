package com.hitech.dms.web.dao.dealer;

import java.util.List;

import com.hitech.dms.web.model.dealer.UserDealerResponseModel;

public interface DealerListUnderUserDao {
	public List<UserDealerResponseModel> fetchUserDealerList(String authorizationHeader, String isInactiveInclude);
	public List<UserDealerResponseModel> fetchUserDealerListNew(String authorizationHeader, String isInactiveInclude,Integer pcId,String userCode);
}
