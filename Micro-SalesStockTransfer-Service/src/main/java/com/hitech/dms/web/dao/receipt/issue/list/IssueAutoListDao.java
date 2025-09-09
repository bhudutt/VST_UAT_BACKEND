package com.hitech.dms.web.dao.receipt.issue.list;

import java.util.List;

import com.hitech.dms.web.model.stock.receipt.issue.request.IssueAutoListRequestModel;
import com.hitech.dms.web.model.stock.receipt.issue.response.IssueAutoListResponseModel;

public interface IssueAutoListDao {
	public List<IssueAutoListResponseModel> fetchIssueListForIssue(String userCode,
			IssueAutoListRequestModel requestModel);
}
