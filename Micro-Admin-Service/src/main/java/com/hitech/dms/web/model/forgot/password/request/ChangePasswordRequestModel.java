/**
 * 
 */
package com.hitech.dms.web.model.forgot.password.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class ChangePasswordRequestModel {
	private BigInteger userId;
	private String userCode;
	private String password;
	private String newPassword;
	private String confirmPassword;
}
