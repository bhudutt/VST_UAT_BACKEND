package com.hitech.dms.web.dao.enquiry.retailFollowUp;


import java.math.BigInteger;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.retailFollowUp.create.request.RetailFollowUpSubmitRequestModel;
import com.hitech.dms.web.model.retailFollowUp.create.response.RetailFollowUpCreateResponseModel;
import com.hitech.dms.web.model.retailFollowUp.create.response.RetailFollowUpEnquirySelectionResponseModel;

/**
 * @author vinay.gautam
 *
 */
public interface RetailFollowUpCreateDao {
	
	//public Map<String, Object> fetchretailsStages(String userCode, BigInteger enquryId, Device device);
	
	public RetailFollowUpEnquirySelectionResponseModel fetchEnqDtlRetailHistory(String userCode, BigInteger enquryId, Device device);
	
	public RetailFollowUpCreateResponseModel createRetailFollowUp(String userCode, RetailFollowUpSubmitRequestModel ptModel, Device device);

}
