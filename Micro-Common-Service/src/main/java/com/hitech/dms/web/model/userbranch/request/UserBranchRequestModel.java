/**
 * 
 */
package com.hitech.dms.web.model.userbranch.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class UserBranchRequestModel {
	private String userCode;
	private String includeInActive;
}
