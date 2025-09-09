package com.hitech.dms.web.model.user;

import java.util.Date;
import java.util.List;

public class User {
	
	private Integer userId;
	private String userCode;
	private String actualUserCode; // for dealer User code
	// todo: need to add localization for validation messages
	private String email;

	// todo: need to add localization for validation messages
	// @NotEmpty(message = "Password is required")
	private String passwordHash;
	private String password;
	private String currentPassword;
	private String confirmPassword;
	private String otp;
	private String token;
	private Date lastPasswordResetDate;
	private Boolean isActive;
	private UserHoModel userHoModel;
	private String deviceId;

	// todo: need to add localization for validation messages
	private String address1;

	private String address2;

	private String city;

	private String state;

	private String country;

	private String pincode;

	private String contactNo;

	private String mobileNo;

	private Date regDate;

	private String status;

	private Date creationdate;

	private String createdBy;

	private Date modifiedOn;

	private String modifiedBy;

	private UserType userType;
	
	private String userFullName;
	private String userName;
	private String username; // for API
	private String tempMsg;
	private String isFor;
	private Integer parentId;
	private Integer branchId;
	private String branchName;
	private String branchAddress;
	private Integer empId;
	private Integer hoUserId;
	private List<Role> roleList;

	private Date lastChanged;
	
	private Boolean isAccountValidated;

	private transient boolean enabled;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;

	public User() {

	}

	public User(String email, String passwordHash, String userCode) {
		this.email = email;
		this.passwordHash = passwordHash;
		this.userCode = userCode;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreationdate() {
		return creationdate;
	}

	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		if(username == null && this.getEmail() != null) {
			username = this.getEmail();
		}
		return username;
	}

	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		enabled = true;
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getLastChanged() {
		return lastChanged;
	}

	public void setLastChanged(Date lastChanged) {
		this.lastChanged = lastChanged;
	}

	@Override
	public String toString() {
		return " User [ id: " + userCode + "\n" + " name: " + userName + "\n" + " email: " + email + "\n" + " passwd: "
				+ password + "\n " + " enabled: " + enabled + "\n" + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof User))
			return false;

		User user = (User) obj;
		return (this.userCode == user.userCode && this.email.equals(user.email)
				&& this.enabled == user.enabled);

	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getTempMsg() {
		return tempMsg;
	}

	public void setTempMsg(String tempMsg) {
		this.tempMsg = tempMsg;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public Boolean getIsAccountValidated() {
		return isAccountValidated;
	}

	public void setIsAccountValidated(Boolean isAccountValidated) {
		this.isAccountValidated = isAccountValidated;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getEmpId() {
		return empId;
	}

	public void setEmpId(Integer empId) {
		this.empId = empId;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public Boolean getIsActive() {
		if(isActive == null) {
			isActive = false;
		}
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		if(isActive == null) {
			isActive = false;
		}
		this.isActive = isActive;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getHoUserId() {
		return hoUserId;
	}

	public void setHoUserId(Integer hoUserId) {
		this.hoUserId = hoUserId;
	}

	public UserHoModel getUserHoModel() {
		return userHoModel;
	}

	public void setUserHoModel(UserHoModel userHoModel) {
		this.userHoModel = userHoModel;
	}

	public String getIsFor() {
		return isFor;
	}

	public void setIsFor(String isFor) {
		this.isFor = isFor;
	}

	public Date getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}

	public void setLastPasswordResetDate(Date lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getActualUserCode() {
		return actualUserCode;
	}

	public void setActualUserCode(String actualUserCode) {
		this.actualUserCode = actualUserCode;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
