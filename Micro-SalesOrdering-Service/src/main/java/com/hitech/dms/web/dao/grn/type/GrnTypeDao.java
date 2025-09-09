package com.hitech.dms.web.dao.grn.type;

import java.util.List;

import com.hitech.dms.web.model.grn.type.response.GrnTypeResponseModel;

public interface GrnTypeDao {
	public List<GrnTypeResponseModel> fetchGrnTypeList(String userCode);
}
