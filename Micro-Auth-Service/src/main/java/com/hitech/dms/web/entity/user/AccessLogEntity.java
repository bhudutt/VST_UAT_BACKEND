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
	private BigInteger userLoginId;
	
//	@Column(name="user_id")
//	private BigInteger userCodeId;
	
	@Column(name="userCode")
	private String userCode;
	
	@Column(name="LoginTime")
	private Date loginTime;
	
	@Column(name="LogoutTime")
	private Date logoutTime;
	
	@Column(name="userAgent")
	private String userAgent;
	
	@Column(name="IP")
	private String ipAddress;
	
	@Column(name="LogoutType")
	private String logoutType;
	
	@Column(name="LogoutIp")
	private String logoutIp;
	
	@Column(name="latitude")
	private String latitude;
	
	@Column(name="longitude")
	private String longitude;
	
	@Column(name="LastAccessTime")
	private Date lastAccessTime;
	
}
