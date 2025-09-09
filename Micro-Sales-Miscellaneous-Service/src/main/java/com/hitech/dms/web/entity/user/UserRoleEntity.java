/**
 * 
 */
package com.hitech.dms.web.entity.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "ADM_MENU_ROLE_USER_HDR")
public class UserRoleEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4419457296792771042L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_role_id")
	private Integer userRoleId;
	
	@Column(name = "role_id")
	private Integer roleId;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean isActive;
	
	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	public Integer getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
