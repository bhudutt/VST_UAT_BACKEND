package com.hitech.dms.web.model.enquiry.digitalenquiry.create.request;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

//import com.hitech.dms.web.model.enquiry.create.request.CustomerHDRRequestModel;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sunil.singh
 *
 */
@Data
@Getter
@Setter
public class DigitalEnquiryCreateRequestModel {

	//@NotNull(message="Digital Platform Source is required")
	private String digitalSourceName;
	//@NotNull(message="Company Name is required")
	private String companyName;
	//@NotNull(message="Company Contact No. is required")
	private String companyContactNo;
	//@NotNull(message="Company Email ID is required")
	private String companyEmailid;
	//@NotNull(message="Contact Person Name is required")
	private String contactPersonName;
	//@NotNull(message="Contact Person No. is required")
	private String contactPersonNo;
	//@NotNull(message="Contact Person Email ID is required")
	private String contactPersonEmailid;
	//@NotNull(message="Company Address1 is required")
	private String companyAddress1;
	private String companyAddress2;
	private String companyAddress3;
	//@NotNull(message="State is required")
	private BigInteger pincode;
	//@NotNull(message="Pin Id is required")
	private BigInteger pinId;
	//@NotNull(message="ContactPersonDesignation is required")
	private String designation;
	private String state;
	//@NotNull(message="District is required")
	private String district;
	//@NotNull(message="Tehsil is required")
	private String tehsil;
	//@NotNull(message="Village is required")
	private String village;
	//@NotNull(message="Pincode is required")
	//@NotNull(message="GST NO. is required")
	private String gstNO;
	//@NotNull(message="TAN NO. is required")
	private String tanNO;
	//@NotNull(message="PAN NO. is required")
	private String panNO;
	//@NotNull(message="Active Status is required")
	private String isActive;
	
	private BigInteger createdBy;
	@JsonDeserialize(using = DateHandler.class)
	private Date createdDate=new Date();
	private BigInteger modifiedBy;
	@JsonDeserialize(using = DateHandler.class)
	private Date modifiedDate =new Date();;
	
	
}
