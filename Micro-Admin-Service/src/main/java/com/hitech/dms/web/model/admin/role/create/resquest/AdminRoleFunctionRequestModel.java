package com.hitech.dms.web.model.admin.role.create.resquest;

import java.util.List;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class AdminRoleFunctionRequestModel {
	
	private RoleMasterRequestModel role;
    private List<RoleMenuRequestModel> roleMenu;

}
