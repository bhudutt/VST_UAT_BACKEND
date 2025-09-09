package com.hitech.dms.web.dao.enquiry.validate;

import java.math.BigInteger;
import java.util.Date;

import com.hitech.dms.web.model.enquiry.validate.response.EnquiryValidateResponseModel;

public interface EnquiryValidateDao {

	EnquiryValidateResponseModel validateEnquiry(String userCode, BigInteger enquiryId, String validationStatus, Date validationDate, String remarks);
}
