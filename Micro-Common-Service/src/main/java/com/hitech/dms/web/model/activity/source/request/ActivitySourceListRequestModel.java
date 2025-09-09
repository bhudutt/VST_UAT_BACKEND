/**
 * 
 */
package com.hitech.dms.web.model.activity.source.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ActivitySourceListRequestModel {
	private String userCode;
	private Integer pcId;
	private String isFor;
	private String isIncludeInActive;
}
