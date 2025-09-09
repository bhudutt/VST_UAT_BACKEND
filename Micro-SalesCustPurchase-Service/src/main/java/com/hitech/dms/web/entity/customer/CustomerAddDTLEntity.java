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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "CM_CUST_AD")
@Data
public class CustomerAddDTLEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1867514944801926157L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Cust_AD_Id")
	private BigInteger custADId;

	@ManyToOne
	@JoinColumn(name = "Customer_Id")
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private CustomerHDREntity customerHdr;

	@Column(name = "AddressTypeID")
	private BigInteger addressTypeID;

	@Column(name = "CustAddLine1")
	private String custAddLine1;

	@Column(name = "CustAddLine2")
	private String custAddLine2;

	@Column(name = "CustAddLine3")
	private String custAddLine3;

	@Column(name = "Pin_Id")
	private BigInteger pinId;

	@Column(name = "Contact_Person")
	private String contactPerson;

	@Column(name = "Contact_No")
	private String contactNo;

	@Column(name = "Email")
	private String email;

	@Column(name = "FAX_NO")
	private String faxNo;

	@Column(name = "GST_NO")
	private String gstNo;

	@Column(name = "CreatedBy", updatable = false)
	private BigInteger createdBy;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

}
