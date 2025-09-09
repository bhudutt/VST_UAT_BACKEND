/**
 * 
 */
package com.hitech.dms.web.model.user.activitylog.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class UserActivityLogResponseModel {
	private BigInteger id;
	private String msg;
	private int statusCode;
}
