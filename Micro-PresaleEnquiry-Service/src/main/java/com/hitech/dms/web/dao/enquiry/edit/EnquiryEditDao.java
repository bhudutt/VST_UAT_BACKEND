package com.hitech.dms.web.dao.enquiry.edit;

import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.enquiry.edit.request.EnquiryEditAttachImagesRequestModel;
import com.hitech.dms.web.model.enquiry.edit.request.EnquiryEditRequestModel;
import com.hitech.dms.web.model.enquiry.edit.response.EnquiryEditResponseModel;

public interface EnquiryEditDao {
	public EnquiryEditResponseModel updateEnquiry(String userCode, EnquiryEditRequestModel enquiryEditRequestModel,
			List<EnquiryEditAttachImagesRequestModel> enquiryAttachImgsList, Device device);
}
