package com.hitech.dms.web.dao.part.issue.search;

import java.util.List;

import com.hitech.dms.web.model.partissue.search.request.PartIssueSearchRequestModel;
import com.hitech.dms.web.model.partissue.search.response.PartIssueSearchListResultResponse;
import com.hitech.dms.web.model.partissue.search.response.SparePartIssueCommonResponseModel;


public interface PartIssueSearchDao {

	PartIssueSearchListResultResponse PartIssueSearchList(String userCode, PartIssueSearchRequestModel requestModel);

	SparePartIssueCommonResponseModel PartIssueViewList(String userCode, Integer issueId);

}
