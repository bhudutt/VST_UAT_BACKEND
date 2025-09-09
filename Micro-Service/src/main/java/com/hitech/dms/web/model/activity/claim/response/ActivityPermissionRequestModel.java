/**
 * 
 */
package com.hitech.dms.web.model.activity.claim.response;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.activity.claim.request.ActivityClaim;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class ActivityPermissionRequestModel {
	private BigInteger id;
	private String isFor;
	private List<ActivityClaim> activityList;
}
