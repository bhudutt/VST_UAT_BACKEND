package com.hitech.dms.web.dao.admin.dtl.dao;

import com.hitech.dms.web.dao.common.model.CommonHoDetailResponse;
import com.hitech.dms.web.model.admin.dtl.request.HoUserDTLRequestModel;
import com.hitech.dms.web.model.admin.dtl.response.HoUserDTLResponseModel;

public interface HoUserDTLDao {
	public HoUserDTLResponseModel fetchHoUserDtl(String userCode, HoUserDTLRequestModel requestModel);
	public CommonHoDetailResponse fetchCommonHoOrgDetail(Integer flag ,String userCode);


}
