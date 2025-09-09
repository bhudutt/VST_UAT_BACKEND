/**
 * 
 */
package com.hitech.dms.web.model.admin.create.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class HoUserCreateResponseModel {
	private BigInteger hoUserId;
	private String employeeCode;
	private BigInteger userId;
	private String userCode;
	private String msg;
	private int statusCode;
}
