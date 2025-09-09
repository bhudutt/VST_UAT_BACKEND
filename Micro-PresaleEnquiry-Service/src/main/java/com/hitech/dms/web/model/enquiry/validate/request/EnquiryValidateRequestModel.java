package com.hitech.dms.web.model.enquiry.validate.request;

import java.math.BigInteger;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EnquiryValidateRequestModel {

	 private BigInteger enquiryId;
	 private String validationStatus;
	 private Date validationDate;
	 private String remarks;	 
}
