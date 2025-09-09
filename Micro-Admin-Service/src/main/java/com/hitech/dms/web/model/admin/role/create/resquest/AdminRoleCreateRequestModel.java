package com.hitech.dms.web.model.admin.role.create.resquest;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class AdminRoleCreateRequestModel {
	private String applicableTo;
	private String roleName;
	private Long roleId;

}
