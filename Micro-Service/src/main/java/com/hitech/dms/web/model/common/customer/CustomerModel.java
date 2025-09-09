package com.hitech.dms.web.model.common.customer;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CustomerModel {

	private Integer customerId;
	private String customerCode;
	private String enquiryNo;
	private String customerGroupName;
	private String groupOwner;
	private String firstName;
	private String qualification;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dob;
	private String middleName;
	private String panTinNo;
	private String ContactTitle;
	private Integer IsMarried;
	private String mariatalStatus;
	private String lastName;
	private String designation;
	private String mobileNo;
	private String alternateNo;
	private String telephoneNo;
	private String deleteId;
	private Integer customerTypeId;
	private String companyName;
	private String contactName;
	private String customerFullName;
	private String fatherFirstName;
	private String fatherMiddleName;
	private String fatherLastName;

	private String oildiscountType;
	private String partdiscountType;
	private String labourDiscType;
}
