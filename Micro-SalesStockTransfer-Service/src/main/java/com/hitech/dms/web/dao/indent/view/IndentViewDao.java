package com.hitech.dms.web.dao.indent.view;

import com.hitech.dms.web.model.indent.view.request.IndentViewRequestModel;
import com.hitech.dms.web.model.indent.view.response.IndentViewResponseModel;

public interface IndentViewDao {
	public IndentViewResponseModel fetchIndentView(String userCode, IndentViewRequestModel requestModel);
}
