package com.hitech.dms.web.dao.admin.role.create;

import java.math.BigInteger;
import java.util.Objects;

import org.apache.tomcat.util.buf.CharChunk;

/**
 * @author vinay.gautam
 *
 */
public class AdminRoleUnit implements AdminRoleElement<BigInteger>{
	
 	private final BigInteger id;

    private final String description;

    private final BigInteger parentId;
    
    private final Character isActive;

    public AdminRoleUnit(BigInteger id, String description) {
        this.id = id;
        this.description = description;
        this.parentId = null;
		this.isActive = null;
    }
    
//    public AdminRoleUnit(BigInteger id, String description,Character isActive) {
//        this.id = id;
//        this.description = description;
//        this.parentId = null;
//		this.isActive = isActive;
//    }

    public AdminRoleUnit(BigInteger id, String description, BigInteger parentId) {
        this.id = id;
        this.description = description;
        this.parentId = parentId;
		this.isActive = null;
    }
    
    public AdminRoleUnit(BigInteger id, String description, BigInteger parentId, Character isActive) {
        this.id = id;
        this.description = description;
        this.parentId = parentId;
		this.isActive = isActive;
    }

    @Override
    public BigInteger getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public BigInteger getParentId() {
        return parentId;
    }
    

    public Character getIsActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminRoleUnit that = (AdminRoleUnit) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(parentId, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, parentId, isActive);
    }


}
