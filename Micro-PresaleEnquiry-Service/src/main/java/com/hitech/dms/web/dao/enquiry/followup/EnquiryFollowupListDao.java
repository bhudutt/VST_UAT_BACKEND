package com.hitech.dms.web.dao.enquiry.followup;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.enquiry.followup.request.FollowupListRequestModel;
import com.hitech.dms.web.model.enquiry.followup.request.FollowupListResponseModel;


public interface EnquiryFollowupListDao {

	public List<FollowupListResponseModel> FollowupList(String userCode,
			FollowupListRequestModel followupListRequestModel);
}
