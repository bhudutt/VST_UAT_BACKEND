package com.hitech.dms.web.dao.enquiry.view;

import java.math.BigInteger;

import com.hitech.dms.web.model.enquiry.view.request.EnquiryViewRequestModel;
import com.hitech.dms.web.model.enquiry.view.response.EnquiryViewMainResponseModel;

public interface EnquiryViewDao {
	public EnquiryViewMainResponseModel fetchEnqDTL(String userCode, EnquiryViewRequestModel enquiryViewRequestModel);

	public EnquiryViewMainResponseModel fetchEnqDTL(String userCode, BigInteger enquiryId, int flag);
}
