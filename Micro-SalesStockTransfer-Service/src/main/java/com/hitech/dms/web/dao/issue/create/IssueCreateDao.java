package com.hitech.dms.web.dao.issue.create;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.issue.create.request.IssueCreateRequestModel;
import com.hitech.dms.web.model.issue.create.response.IssueCreateResponseModel;

public interface IssueCreateDao {
	public IssueCreateResponseModel createIssue(String authorizationHeader, String userCode,
			IssueCreateRequestModel requestModel, Device device);
}
