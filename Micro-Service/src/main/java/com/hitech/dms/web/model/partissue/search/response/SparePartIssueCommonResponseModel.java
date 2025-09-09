package com.hitech.dms.web.model.partissue.search.response;

import java.util.List;

import lombok.Data;

@Data
public class SparePartIssueCommonResponseModel {
	
 private SparePartIssueViewResponseModel sparePartIssueViewResponseModel;
 private List<SparePartIssueRequisitionDetailResponse> sparePartIssueRequisitionDetailResponse;
 private List<SparePartIssuePartDetailResponseModel> sparePartIssuePartDetailResponseModel;
}
