/**
 * 
 */
package com.hitech.dms.web.model.activityplan.dtl.response;

import org.json.simple.JSONArray;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivityPlanDTLResponseModel {
	private ActivityPlanHDRResponseModel activityPlanHDTDTL;
	private JSONArray activityPlanJsonArr;
}
