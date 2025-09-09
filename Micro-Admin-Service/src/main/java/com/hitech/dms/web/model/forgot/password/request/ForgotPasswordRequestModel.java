/**
 * 
 */
package com.hitech.dms.web.model.forgot.password.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class ForgotPasswordRequestModel {
	private String usercode;
	private String usermail;
}
