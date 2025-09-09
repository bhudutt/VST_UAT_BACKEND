/**
 * 
 */
package com.hitech.dms.web.model.activity.claim.response;

import java.util.List;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityClaimSearchMainResponseModel {
	private List<ActivityClaimSearchListResponseModel> acSearch;
	private Integer recordCount;
}
