/**
 * 
 */
package com.hitech.dms.web.entity.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "ADM_MST_USER_TYPE")
@NamedQuery(name = "UserTypeEntity.findByUserType", query = "select r from UserTypeEntity r where r.userType = :userType")
public class UserTypeEntity {
	@Id
	@GeneratedValue
	@Column(name = "USER_TYPE_ID", unique = true, nullable = false)
	private Integer userTypeId;

	@Column(name = "USER_TYPE", nullable = false, unique = true, length = 100)
	private String userType;

	@Column(name = "DESCRIPTION", nullable = false, length = 100)
	private String description;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "userType")
	private Set<UserEntity> userRecords = new HashSet<UserEntity>();

	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean isActive;

	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	public String getAuthority() {
		return getUserType();
	}

	public Set<UserEntity> getUserRecords() {
		return this.userRecords;
	}

	public Integer getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUserRecords(Set<UserEntity> userRecords) {
		this.userRecords = userRecords;
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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
