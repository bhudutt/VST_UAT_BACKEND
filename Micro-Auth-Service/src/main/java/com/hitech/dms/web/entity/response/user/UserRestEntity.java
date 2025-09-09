/**
 * 
 */
package com.hitech.dms.web.entity.response.user;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hitech.dms.web.entity.user.UserHoEntity;
import com.hitech.dms.web.entity.user.UserTypeEntity;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class UserRestEntity {

	private Long userId;

	@JsonIgnore
	private transient UserHoEntity userHoModel;

	private String username;

	private Long userDLRId;

	private Long hoUserId;

	private transient String userFullName;

	private String status;
	
	private String role;
	
	private BigInteger dealerId;
	
	private BigInteger branchId;
	
	private String email;
	
	private String locality;
	
	private String mobileNumber;
	
	private String dealerCode;
	
	private String dealerName;
	
	private Integer dealerTypeId;
	
	private String dealerLocation;
	
	private String designationDesc;

	
//	private String designationDesclevel;
	
	

	
	private String designationDescLevel;//added by mahesh.kumar on 10-02-2025


	@JsonIgnore
	private UserTypeEntity userType;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UserRestEntity))
			return false;

		UserRestEntity user = (UserRestEntity) obj;
		return (this.username.equals(user.username));

	}

	@Override
	public int hashCode() {
		int result = 31;
		result = result * (this.username == null ? 0 : this.username.hashCode());
		return result;
	}
}
