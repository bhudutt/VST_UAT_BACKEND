/**
 * 
 */
package com.hitech.dms.web.model.user.accesslog.request;

import java.math.BigInteger;

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
public class AccessLogUpdateRequestModel {
	private BigInteger userLoginId;
	private String logoutType;
	private String logoutIp;
}
