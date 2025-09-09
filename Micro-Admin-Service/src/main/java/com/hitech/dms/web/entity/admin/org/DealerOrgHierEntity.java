/**
 * 
 */
package com.hitech.dms.web.entity.admin.org;

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

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "ADM_BP_DEALER_ORGHIER")
@Data
public class DealerOrgHierEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -2770820416383153787L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dealer_vs_fieldroleID")
	private BigInteger dealerVsFieldRoleId;
	@Column(name = "dealer_id")
	private BigInteger dealerId;
	@Column(name = "pc_id")
	private Integer pcId;
	@Column(name = "department_id")
	private Integer departmentId;
	@Column(name = "org_hierarchy_id")
	private BigInteger orgHierId;
	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean isActive;
	@Column(name = "created_by", updatable = false)
	private String createdBy;
	@Column(name = "created_date", updatable = false)
	private Date createdDate;
	@Column(name = "last_modified_by")
	private String modifiedBy;
	@Column(name = "last_modified_date")
	private Date modifiedDate;
}
