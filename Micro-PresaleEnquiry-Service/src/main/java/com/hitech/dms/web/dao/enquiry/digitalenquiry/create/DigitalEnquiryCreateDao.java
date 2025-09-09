package com.hitech.dms.web.dao.enquiry.digitalenquiry.create;

import java.util.List;
import org.springframework.mobile.device.Device;
import com.hitech.dms.web.entity.digitalenquiry.DigitalEnquiryEntity;
import com.hitech.dms.web.model.enquiry.digitalenquiry.create.response.DigitalEnquiryCreateResponseModel;
import com.hitech.dms.web.model.enquiry.digitalenquiry.create.response.DigitalEnquiryListResponseModel;

public interface DigitalEnquiryCreateDao {

	public DigitalEnquiryCreateResponseModel createDigitalEnquiry(String userCode,
			DigitalEnquiryEntity digitalEnquiryCreateRequestModel, Device device);
	public List<DigitalEnquiryListResponseModel> fetchDigitalFormMasterList(String userCode, Integer Digital_Source_ID);
	
	
}
