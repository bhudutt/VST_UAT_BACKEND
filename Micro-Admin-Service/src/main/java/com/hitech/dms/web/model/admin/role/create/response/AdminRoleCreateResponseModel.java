package com.hitech.dms.web.model.admin.role.create.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class AdminRoleCreateResponseModel {
	private String msg;
	private Integer statusCode;
	private String roleCode;
	private Long roleId;
}
