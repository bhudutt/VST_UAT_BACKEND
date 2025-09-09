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
	private BigInteger userCodeId;
	private Date loginTime;
	private Date logoutTime;
	private String ipAddress;
	private String macAddress;
	private Date lastAccessTime;
}
