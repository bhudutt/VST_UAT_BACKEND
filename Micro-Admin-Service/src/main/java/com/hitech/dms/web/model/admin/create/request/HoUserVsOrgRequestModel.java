/**
 * 
 */
package com.hitech.dms.web.model.admin.create.request;

import java.math.BigInteger;
import java.util.Objects;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class HoUserVsOrgRequestModel {
	private BigInteger hoUserOrgId;
	private BigInteger hoUserId;
	private BigInteger orgHierarchyId;
	private String orgDesc;
	private String pcDesc;
	private Boolean isActive;
	private String departmentDesc;
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HoUserVsOrgRequestModel other = (HoUserVsOrgRequestModel) obj;
		return Objects.equals(hoUserId, other.hoUserId) && Objects.equals(orgHierarchyId, other.orgHierarchyId);
	}
	@Override
	public int hashCode() {
		return Objects.hash(hoUserId, orgHierarchyId);
	}
	
	
}
