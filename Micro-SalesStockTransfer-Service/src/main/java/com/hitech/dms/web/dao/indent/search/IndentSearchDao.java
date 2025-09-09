package com.hitech.dms.web.dao.indent.search;

import com.hitech.dms.web.model.indent.search.request.IndentSearchRequestModel;
import com.hitech.dms.web.model.indent.search.response.IndentSearchMainResponseModel;

public interface IndentSearchDao {
	public IndentSearchMainResponseModel searchIndentList(String userCode, IndentSearchRequestModel requestModel);
}
