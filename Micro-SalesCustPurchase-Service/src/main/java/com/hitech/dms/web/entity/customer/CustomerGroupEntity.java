/**
 * 
 */
package com.hitech.dms.web.entity.customer;

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

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "CM_CUST_GRP")
@Data
public class CustomerGroupEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4557928957846116467L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CustGrp_Id")
	private BigInteger custmerGrpId;

	@Column(name = "GroupName")
	private String customerGroupName;

	@Column(name = "GroupType_Id")
	private BigInteger groupTypeId;

	@Column(name = "AddLine1")
	private String address1;

	@Column(name = "AddLine2")
	private String address2;

	@Column(name = "AddLine3")
	private String address3;

	@Column(name = "Telephone")
	private String telephone;

	@Column(name = "Email")
	private String email;

	@Column(name = "Fax")
	private String fax;

	@Column(name = "Pin_Id")
	private BigInteger locality;

	@Column(name = "CreatedBy", updatable = false)
	private BigInteger createdBy;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;
}
