/**
 * 
 */
package com.hitech.dms.web.model.user.accesslog.request;

import java.math.BigInteger;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class AccessLogRequestModel {
	private BigInteger userLoginId;
	private String userCode;
	private Date loginTime;
	private Date logoutTime;
	private String userAgent;
	private String ipAddress;
	private String latitude;
	private String longitude;
	private Date lastAccessTime;
}
