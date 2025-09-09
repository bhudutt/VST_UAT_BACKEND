package com.hitech.dms.web.dao.enquiry.transfer;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.enquiry.transfer.request.EnquiryTransferRequestModel;
import com.hitech.dms.web.model.enquiry.transfer.response.EnquiryTransferResponseModel;

public interface EnquiryTransferDao {
	public EnquiryTransferResponseModel transferEnqSalesman(String authorizationHeader, String usercode,
			EnquiryTransferRequestModel enquiryTransferRequestModel, Device device);
}
