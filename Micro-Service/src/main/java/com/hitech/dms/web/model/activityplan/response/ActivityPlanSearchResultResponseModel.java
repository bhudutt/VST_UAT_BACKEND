package com.hitech.dms.web.model.activityplan.response;

import java.util.List;

import lombok.Data;

@Data
public class ActivityPlanSearchResultResponseModel {
  
	List<ActivityPlanSearchResponseModel> searchResult;
	private Integer recordCount;
}
