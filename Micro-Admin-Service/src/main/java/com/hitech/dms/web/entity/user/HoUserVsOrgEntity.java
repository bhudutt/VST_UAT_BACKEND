/**
 * 
 */
package com.hitech.dms.web.entity.user;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

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
@Entity
@Table(name = "ADM_HO_USER_ORGHIER")
@Data
public class HoUserVsOrgEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6267191638307252760L;
	@Id
	@Column(name = "UsrID_vs_OrgHierID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger hoUserOrgId;

	@Column(name = "ho_usr_id")
	private BigInteger hoUserId;

	@Column(name = "org_hierarchy_id")
	private BigInteger orgHierarchyId;

	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean isActive;

	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;

	@JsonIgnore
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@JsonIgnore
	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@JsonIgnore
	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HoUserVsOrgEntity other = (HoUserVsOrgEntity) obj;
		return Objects.equals(hoUserId, other.hoUserId) && Objects.equals(orgHierarchyId, other.orgHierarchyId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(hoUserId, orgHierarchyId);
	}
}
