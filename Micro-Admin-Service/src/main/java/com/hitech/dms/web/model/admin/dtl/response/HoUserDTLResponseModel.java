package com.hitech.dms.web.model.admin.dtl.response;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.admin.org.dtl.response.HoUserVSFieldRoleResponseModel;
import com.hitech.dms.web.model.user.role.response.UserRoleResponseModel;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class HoUserDTLResponseModel {
	private BigInteger hoUserId;
	private String employeeCode;
	private String employeeName;
	private String empContactNo;
	private String empMail;
	private Boolean isActive;
	private Integer departmentId;
	private String departmentDesc;
	private Integer hoDesignationId;
	private String designationDesc;
	private Integer hoDesignationLevelId;
	private String designationLevelDesc;
	private BigInteger userId;
	private String userCode;
	private String password;
	private String msg;
	
	private Integer userForId;
	private Integer platformId;

	private List<HoUserVSFieldRoleResponseModel> hoUserFieldRoleOrgList;
	private List<UserRoleResponseModel> userRoleList;
}
