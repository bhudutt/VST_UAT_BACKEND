/**
 * 
 */
package com.hitech.dms.web.entity.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "ADM_MENU_ROLE_HDR")
@Data
public class RoleEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long roleId;

	@Column(name = "RoleCode")
	private String roleCode;

	@Column(name = "RoleName")
	private String roleName;

	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean isActive;
	
	@Column(name = "IsFor")
	private Integer isFor;
	
	@Transient
	private int[] menuIDs;
	
	@Transient
	private boolean selectedRole;
	
	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	
	/**
	 * Parameterized Constructor
	 * @param roleId
	 * @param roleCode
	 * @param roleName
	 */
	public RoleEntity(Long roleId, String roleCode, String roleName){
		this.roleId = roleId;
		this.roleCode = roleCode;
		this.roleName = roleName;
	}
	
	public RoleEntity(Long roleId, String roleCode, String roleName, Integer isFor){
		this.roleId = roleId;
		this.roleCode = roleCode;
		this.roleName = roleName;
		this.isFor = isFor;
	}
	
	
	/**
	 * Non-Parameterized Constructor
	 */
	public RoleEntity(){
	}
	
	public String getRoleCode() {
		if(roleCode != null) {
			roleCode = roleCode.toUpperCase();
		}
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		if(roleCode != null) {
			roleCode = roleCode.toUpperCase();
		}
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		if(roleName != null) {
			roleName = roleName.toUpperCase();
		}
		return roleName;
	}

	public void setRoleName(String roleName) {
		if(roleName != null) {
			roleName = roleName.toUpperCase();
		}
		this.roleName = roleName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 0;
		result = result * prime
				+ (this.roleCode != null ? this.roleCode.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		RoleEntity entity = (RoleEntity) obj;
		return (this.getRoleId() == entity.getRoleId() && this.getRoleCode() == entity
				.getRoleCode());
	}

	@Override
	public String toString() {
		return "role : " + roleId + " : " + roleName+ " : " + selectedRole+ " : "+isFor;
	}
}
