package com.hitech.dms.web.model.user;

import java.io.Serializable;
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

import org.springframework.security.core.GrantedAuthority;

public class UserType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7513070932357501395L;

	private String userTypeId;
    private String userType;
    private String description;
    private Set<User> userRecords = new HashSet<User>();
        
    public Set<User> getUserRecords() {
        return this.userRecords;
    }

	public String getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(String userTypeId) {
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
	
	public void setUserRecords(Set<User> userRecords) {
        this.userRecords = userRecords;
    }
}
