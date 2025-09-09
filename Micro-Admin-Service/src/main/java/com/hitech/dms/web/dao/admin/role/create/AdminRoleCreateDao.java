/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.dao.admin.role.create;

import java.util.Collection;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.admin.role.create.response.AdminRoleCreateResponseModel;
import com.hitech.dms.web.model.admin.role.create.resquest.AdminRoleCreateRequestModel;
import com.hitech.dms.web.model.admin.role.create.resquest.AdminRoleFunctionRequestModel;

/**
 * @author vinay.gautam
 *
 */
public interface AdminRoleCreateDao {
	
	public AdminRoleCreateResponseModel createAdminRole(String userCode, AdminRoleFunctionRequestModel requestModel, Device device);
	
	public Collection<AdminRoleTreeNode<AdminRoleUnit>> getAssignedFunctionalityToRole(AdminRoleCreateRequestModel requestModel, Device device);

}
