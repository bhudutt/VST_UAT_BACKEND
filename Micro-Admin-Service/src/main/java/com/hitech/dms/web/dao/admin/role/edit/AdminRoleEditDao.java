package com.hitech.dms.web.dao.admin.role.edit;

import java.math.BigInteger;
import java.util.HashMap;

import org.springframework.mobile.device.Device;

/**
 * @author vinay.gautam
 *
 */
public interface AdminRoleEditDao {
	public HashMap<String, Object> getRolesByRoleId(BigInteger roleId, Device device);
}
