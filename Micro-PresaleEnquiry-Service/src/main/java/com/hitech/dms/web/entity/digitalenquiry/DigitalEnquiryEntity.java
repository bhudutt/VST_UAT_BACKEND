package com.hitech.dms.web.entity.digitalenquiry;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;
/**
 * @author suni.singh
 *
 */

@Table(name = "SA_MST_ENQ_SOURCE_DIGITAL")
@Entity
@Data
public class DigitalEnquiryEntity implements Serializable {

	private static final long serialVersionUID = -5042440987105847644L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Digital_Source_ID")
	private Integer digitalSourceID;
	@Column(name = "DigitalSourceName")
	private String digitalSourceName;
	@Column(name = "CompanyName")
	private String companyName;
	@Column(name = "CompanyContact_No")
	private String companyContactNo;
	@Column(name = "CompanyEmail_id")
	private String companyEmailid;
	@Column(name = "ContactPersonName")
	private String contactPersonName;
	@Column(name = "ContactPerson_No")
	private String contactPersonNo;
	@Column(name = "ContactPerson_Email_id")
	private String contactPersonEmailid;
	@Column(name = "CompanyAddress1")
	private String companyAddress1;
	@Column(name = "CompanyAddress2")
	private String companyAddress2;
	@Column(name = "CompanyAddress3")
	private String companyAddress3;
	@Column(name = "pin_code")
	private String pincode;
	@Column(name = "Pin_id")
	private BigInteger pinId;
	@Column(name = "Designation")
	private String  designation;
	@Column(name = "State")
	private BigInteger state;
	@Column(name = "District")
	private BigInteger district;
	@Column(name = "Tehsil")
	private BigInteger tehsil;
	@Column(name = "City")
	private BigInteger city;
	@Column(name = "GST_NO")
	private String gstNO;
	@Column(name = "PAN_NO")
	private String panNO;
	@Column(name = "TAN_NO")
	private String tanNO;
	@Column(name = "IsActive")
	private String isActive;
	
	@Column(name = "CreatedBy", updatable = false)
	private BigInteger createdBy;
	@Column(name = "CreatedDate", updatable = false)
	@JsonDeserialize(using = DateHandler.class)
	private Date createdDate =new Date();
	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
	@Column(name = "ModifiedDate")
	@JsonDeserialize(using = DateHandler.class)
	private Date modifiedDate =new Date();
	
	@Column(name = "DigitalEmpCode")
	private String digitalEmpCode;
	
	
	
}
