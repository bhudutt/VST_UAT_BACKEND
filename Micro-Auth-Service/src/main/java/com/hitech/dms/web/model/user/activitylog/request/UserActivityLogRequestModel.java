/**
 * 
 */
package com.hitech.dms.web.model.user.activitylog.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class UserActivityLogRequestModel {
	// UserActivityLogMapId
	private String userCode;
	private String ip;
	private String method;
	private String url;
	private String event;
	private String page;
	private String queryString;
	private String refererPage;
	private String userAgent;
	private boolean uniqueVisit;
	private String latitude;
	private String longitude;
}
