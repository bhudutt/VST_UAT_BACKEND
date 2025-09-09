/**
 * 
 */
package com.hitech.dms.web.model.admin.create.request;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class HoUserCreateRequestModel {
	private BigInteger hoUserId;
	private String employeeCode;
	private String employeeName;
	private transient String pFirstName;
	private transient String pMiddleName;
	private transient String pLastName;
	private String empContactNo;
	private String empMail;
	private Integer departmentId;
	private Integer hoDesignationId;
	private Integer hoDesignationLevelId;
	private Boolean isActive;
	private String userCode;
	private BigInteger userId;
	private String password;
	private String confirmPassword;
	private Integer userForId;
	private Integer platformId;

	private List<HoUserVsOrgRequestModel> hoUserVsOrgList;
	private List<HoUserRoleRequestModel> hoUserRoleList;
}
