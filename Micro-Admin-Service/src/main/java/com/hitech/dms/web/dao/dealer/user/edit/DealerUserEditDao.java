package com.hitech.dms.web.dao.dealer.user.edit;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.dealer.user.create.request.DealerUserCreateRequestModel;
import com.hitech.dms.web.model.dealer.user.create.response.DealerUserCreateResponseModel;

/**
 * @author vinay.gautam
 *
 */
public interface DealerUserEditDao {
	public DealerUserCreateResponseModel editDealerUser(String userCode, DealerUserCreateRequestModel requestModel, Device device);

}
