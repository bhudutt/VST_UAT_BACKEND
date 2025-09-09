package com.hitech.dms.web.dao.issue.view;

import com.hitech.dms.web.model.issue.view.request.IssueViewRequestModel;
import com.hitech.dms.web.model.issue.view.response.IssueViewResponseModel;

public interface IssueViewDao {
	public IssueViewResponseModel fetchIssueView(String userCode, IssueViewRequestModel requestModel);
}
