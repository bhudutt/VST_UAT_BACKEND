package com.hitech.dms.web.dao.enquiry.followup;

import java.util.List;

import com.hitech.dms.web.entity.enquiry.EnquiryFollowupEntity;
import com.hitech.dms.web.model.enquiry.followup.request.EnquiryFollowupResponse;
import com.hitech.dms.web.model.enquiry.followup.request.FollowupCreateRequestModel;

public interface EnquiryFollowupDao {

	List<EnquiryFollowupResponse> getFollowupDetailsByEnquiryId(Integer enquiryId);
	
	String createEnquiryFollowup(String usercode, EnquiryFollowupEntity entity);
	
	String updateFollowup(String usercode, FollowupCreateRequestModel followupCreateRequestModel);
}
