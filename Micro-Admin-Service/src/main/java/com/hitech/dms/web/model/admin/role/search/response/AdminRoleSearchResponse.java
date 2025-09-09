/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.model.admin.role.search.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class AdminRoleSearchResponse {
	private BigInteger id;
	private String action;
//	private String edit;
	private String roleCode;
	private String roleName;
	private char status;
	private String applicableTo;
	private String createdDate;

}
