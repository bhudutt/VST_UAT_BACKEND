package com.hitech.dms.web.dao.pc;

import java.util.List;

import com.hitech.dms.web.model.pc.response.ProfitCenterUnderUserResponseModel;

public interface ProfitCenterDao {
	public List<ProfitCenterUnderUserResponseModel> fetchPcUnderUserList(String userCode);
}
