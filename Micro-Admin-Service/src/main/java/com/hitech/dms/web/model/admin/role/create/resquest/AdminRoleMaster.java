package com.hitech.dms.web.model.admin.role.create.resquest;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class AdminRoleMaster {
	
    private Long id;

    private String roleCode;

	@JsonProperty(value = "roleName", required = true)
	@NotNull(message = "Role Name is Required")
    private String roleName;

	@JsonProperty(value = "applicableTo", required = true)
	@NotNull(message = "ApplicableTo is Required")
    private String applicableTo;

    private Character activeStatus;

}
