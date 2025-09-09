package com.hitech.dms.web.dao.enquiry.create;

import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.enquiry.create.request.EnquiryAttachImagesRequestModel;
import com.hitech.dms.web.model.enquiry.create.request.EnquiryCreateRequestModel;
import com.hitech.dms.web.model.enquiry.create.response.EnquiryCreateResponseModel;

public interface EnquiryCreateDao {
	public EnquiryCreateResponseModel createEnquiry(String userCode,
			EnquiryCreateRequestModel enquiryCreateRequestModel, List<EnquiryAttachImagesRequestModel> enquiryAttachImgsList, Device device);
}
