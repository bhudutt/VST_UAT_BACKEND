/**
 * 
 */
package com.hitech.dms.web.model.activityplan.search.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class ActivityPlanSearchResultResponseModel {
	private List<ActivityPlanSearchResponseModel> searchResult;
	private Integer recordCount;
}
