package com.hitech.dms.web.dao.dealer.user.create;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.dealer.user.create.request.DealerUserCreateRequestModel;
import com.hitech.dms.web.model.dealer.user.create.response.DealerUserCreateResponseModel;

/**
 * @author vinay.gautam
 *
 */
public interface DealerUserCreateDao {
	
	public DealerUserCreateResponseModel createDealerUser(String userCode, DealerUserCreateRequestModel requestModel, Device device);

}
