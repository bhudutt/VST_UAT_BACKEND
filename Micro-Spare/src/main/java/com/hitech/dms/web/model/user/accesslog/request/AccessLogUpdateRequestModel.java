/**
 * 
 */
package com.hitech.dms.web.model.user.accesslog.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class AccessLogUpdateRequestModel {
	private BigInteger userLoginId;
}
