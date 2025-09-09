package com.hitech.dms.web.model.admin.role.create.resquest;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class RoleMenuRequestModel {
	private BigInteger roleMenuId;
	private  Long menuId;
	private Character isActive;

}
