package com.hitech.dms.web.dao.admin.role.search;

import com.hitech.dms.web.model.admin.role.search.response.AdminRoleSearchMainResponse;
import com.hitech.dms.web.model.admin.role.search.resquest.AdminRoleSearchRequest;

/**
 * @author vinay.gautam
 *
 */
public interface AdminRoleSearchDao {
	public AdminRoleSearchMainResponse fetchAdminRoleSearch(String userCode, AdminRoleSearchRequest requestModel);
}
