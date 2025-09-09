package com.hitech.dms.web.dao.issue.indent.dtl;

import com.hitech.dms.web.model.issue.indent.dtl.request.IndentDtlRequestModel;
import com.hitech.dms.web.model.issue.indent.dtl.response.IndentDtlResponseModel;

public interface IndentDtlDao {
	public IndentDtlResponseModel fetchIndentDtlForIssue(String userCode, IndentDtlRequestModel requestModel);
}
