/**
 * 
 */
package com.hitech.dms.web.model.admin.create.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class HoUserRoleRequestModel {
	private BigInteger userRoleId;
	private BigInteger userId;
	private BigInteger roleId;
	private String roleCode;
	private String roleDesc;
	private Boolean isActive;
}
