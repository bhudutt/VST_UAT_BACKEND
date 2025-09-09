/**
 * 
 */
package com.hitech.dms.web.entity.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "ADM_USER", uniqueConstraints = { @UniqueConstraint(columnNames = "user_id") })
@NamedQueries({
		@NamedQuery(name = "UserEntity.findByUserName", query = "select u from UserEntity u where u.username = :username") })
@Data
public class UserEntity implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6323816528222962715L;

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@JsonIgnore
	private transient UserHoEntity userHoModel;

	@Column(name = "UserCode", nullable = false)
	private String username;

	@Column(name = "Password", nullable = false)
	@JsonIgnore
	private String password;
	private transient String passwordHash;

	@Column(name = "LASTPASSWORDRESETDATE")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date lastPasswordResetDate;
	
	@Column(name = "dlr_emp_id")
	private Long dlrEmpId;

	@Column(name = "ho_usr_id")
	private Long hoUserId;

	@Column(name = "Status")
	private String status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_TYPE_ID", nullable = false)
	@JsonIgnore
	private UserTypeEntity userType;

	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean isActive;

	@Column(name = "Last_OTP_No")
	@JsonIgnore
	private String lastOtpNumber;
	@Column(name = "OtpExpDate")
	@JsonIgnore
	private Date otpExpDate;
	@Column(name = "IsOTPVerified")
	@JsonIgnore
	private String isOTPVerified;

	@Column(name = "isAccountValidated")
	@Type(type = "yes_no")
	@JsonIgnore
	private Boolean isAccountValidated;

	@Column(name = "account_enabled")
	@JsonIgnore
	private boolean enabled;
	@Column(name = "account_non_expired")
	@JsonIgnore
	private boolean accountNonExpired;
	@Column(name = "account_non_locked")
	@JsonIgnore
	private boolean accountNonLocked;
	@Column(name = "credentials_non_expired")
	@JsonIgnore
	private boolean credentialsNonExpired;

	@Column(name = "CreatedBy", updatable = false)
	@JsonIgnore
	private String createdBy;

	@Column(name = "CreatedDate", updatable = false)
	@JsonIgnore
	private Date createdDate;

	@Column(name = "ModifiedBy")
	@JsonIgnore
	private String modifiedBy;

	@Column(name = "ModifiedDate")
	@JsonIgnore
	private Date modifiedDate;

	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<UserTypeEntity> roleList = new ArrayList<UserTypeEntity>(1);
		roleList.add(getUserType());
		return roleList;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UserEntity))
			return false;

		UserEntity user = (UserEntity) obj;
		return (this.username.equals(user.username));

	}

	@Override
	public int hashCode() {
		int result = 31;
		result = result * (this.username == null ? 0 : this.username.hashCode());
		return result;
	}
}
