package com.hitech.dms.web.dao.allotment.item.dtl;

import com.hitech.dms.web.model.allot.item.dtl.request.ChassisNoDtlRequestModel;
import com.hitech.dms.web.model.allot.item.dtl.request.ItemDtlForAllotRequestModel;
import com.hitech.dms.web.model.allot.item.dtl.response.ChassisDtlForAllotResponseModel;
import com.hitech.dms.web.model.allot.item.dtl.response.ItemDtlForAllotResponseModel;

public interface ItemDtlForAllotDao {
	public ItemDtlForAllotResponseModel fetchItemDetailForAllot(String userCode,
			ItemDtlForAllotRequestModel requestModel);

	public ChassisDtlForAllotResponseModel fetchChassisNoDetailForAllot(String userCode,
			ChassisNoDtlRequestModel requestModel);
}
