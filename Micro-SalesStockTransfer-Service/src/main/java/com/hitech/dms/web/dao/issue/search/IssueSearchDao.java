package com.hitech.dms.web.dao.issue.search;

import com.hitech.dms.web.model.issue.search.request.IssueSearchRequestModel;
import com.hitech.dms.web.model.issue.search.response.IssueSearchMainResponseModel;

public interface IssueSearchDao {
	public IssueSearchMainResponseModel searchIssueList(String userCode, IssueSearchRequestModel requestModel);
}
