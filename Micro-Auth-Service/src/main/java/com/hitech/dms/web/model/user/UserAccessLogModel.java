package com.hitech.dms.web.model.user;

import java.io.Serializable;
import java.util.Date;

public class UserAccessLogModel implements Serializable{

	/**
	 * 
	 */
	private Integer userLoginId;
	private String userId;
	private String loginTime;
	/*private String logOutTime;*/
	private String lastAccessTime;
	private String ip;
	private String macAddr;
	
	public Integer getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(Integer userLoginId) {
		this.userLoginId = userLoginId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	/*public String getLogOutTime() {
		return logOutTime;
	}
	public void setLogOutTime(String logOutTime) {
		this.logOutTime = logOutTime;
	}*/
	public String getLastAccessTime() {
		return lastAccessTime;
	}
	public void setLastAccessTime(String lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMacAddr() {
		return macAddr;
	}
	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}
	
	

}
