package com.hitech.dms.web.model.admin.role.create.resquest;

import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class RoleMasterRequestModel {

	private Long roleId;
	
//	@JsonProperty(value = "roleCode", required = true)
//	@NotNull(message = "Role Code is Required")
	private String roleCode;

//	@JsonProperty(value = "roleName", required = true)
//	@NotNull(message = "Role Name is Required")
	private String roleName;
	
	
	@JsonProperty(value = "isActive", required = true)
	@NotNull(message = "IsActive is Required")
	@Type(type = "yes_no")
	private Boolean isActive;
	
	private Integer isFor;
	

}
