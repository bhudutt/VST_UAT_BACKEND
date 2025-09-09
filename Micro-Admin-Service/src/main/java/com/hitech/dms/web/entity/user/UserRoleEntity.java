/**
 * 
 */
package com.hitech.dms.web.entity.user;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
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
	private BigInteger userRoleId;
	
	@Column(name = "role_id")
	private BigInteger roleId;
	
	@Column(name = "user_id")
	private BigInteger userId;
	
	@Column(name = "is_active")
	@Type(type = "yes_no")
	private Boolean isActive;

	
	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;
	
	@Column(name = "modified_by")
	private String modifiedBy;

	@JsonIgnore
	@Column(name = "modified_date")
	private Date modifiedDate;
}
