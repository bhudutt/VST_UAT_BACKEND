/**
 * 
 */
package com.hitech.dms.web.model.activityplan.upload.response;

import org.json.simple.JSONArray;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
//@JsonInclude(Include.NON_NULL)
public class ActivityPlanUploadResponseModel {
	private String msg;
	private Integer statusCode;
	private JSONArray activityPlanJsonArr;
	private String planActivityNumber;
}
