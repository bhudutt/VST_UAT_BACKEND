package com.hitech.dms.web.entity.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name="ADM_USR_ACCESS_LOG")
@Entity
@Data
public class AccessLogEntity implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2890460737038110774L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_login_id")
	private Long userLoginId;
	
	@Column(name="user_id")
	private Long userCodeId;
	
	@Column(name="LoginTime")
	private Date loginTime;
	
	@Column(name="LogoutTime")
	private Date logoutTime;
	
	@Column(name="IP")
	private String ipAddress;
	
	@Column(name="MacAdd")
	private String macAddress;
	
	@Column(name="LastAccessTime")
	private Date lastAccessTime;
	
}
