package com.hitech.dms.web.dao.activity.enq;

import java.util.List;

import com.hitech.dms.web.model.activity.enq.request.EnquiryListForActivityRequestModel;
import com.hitech.dms.web.model.activity.enq.response.EnquiryListForActivityResponseModel;

public interface EnquiryListForActivityDao {
	public List<EnquiryListForActivityResponseModel> fetchEnquiryListForActvity(String userCode,
			EnquiryListForActivityRequestModel requestModel);
}
