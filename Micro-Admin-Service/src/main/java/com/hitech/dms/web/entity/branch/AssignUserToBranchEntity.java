/**
 * 
 */
package com.hitech.dms.web.entity.branch;

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
@Table(name = "ADM_BP_DEALER_EMP_VS_BRANCH")
@Data
public class AssignUserToBranchEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 7908817253129025836L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "branch_emp_id")
	private BigInteger branchEmpId;
	@Column(name = "emp_id")
	private BigInteger empId;
	@Column(name = "branch_id")
	private BigInteger branchId;
	@Column(name = "IsBranchPrimary")
	@Type(type = "yes_no")
	private Boolean isBranchPrimary;
	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean isActive;
	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;
	@Column(name = "ModifiedBy")
	private String modifiedBy;
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
}
