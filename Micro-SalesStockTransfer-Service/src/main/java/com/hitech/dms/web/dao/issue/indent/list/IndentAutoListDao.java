package com.hitech.dms.web.dao.issue.indent.list;

import java.util.List;

import com.hitech.dms.web.model.issue.indent.list.request.IndentAutoListRequestModel;
import com.hitech.dms.web.model.issue.indent.list.response.IndentAutoListResponseModel;

public interface IndentAutoListDao {
	public List<IndentAutoListResponseModel> fetchIndentListForIssue(String userCode,
			IndentAutoListRequestModel requestModel);
}
