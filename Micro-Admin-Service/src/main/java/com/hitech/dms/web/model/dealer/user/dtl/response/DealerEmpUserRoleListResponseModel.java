/**
 * 
 */
package com.hitech.dms.web.model.dealer.user.dtl.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DealerEmpUserRoleListResponseModel {
	private BigInteger roleId;
	private BigInteger userId;
	private BigInteger userRoleId;
	private String roleCode;
	private String roleDesc;
	private Boolean isActive;
}
