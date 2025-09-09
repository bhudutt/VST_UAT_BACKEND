/**
 * 
 */
package com.hitech.dms.web.model.log.activity.list.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class ActivityLogListResponseModel {
	private String userCode;
	private String ip;
//	private String method;
	private String event; 
	private String url;
	private String page;
	private String userAgent;
	private String latitude;
	private String longitude;
	private String loggedTime;
}
