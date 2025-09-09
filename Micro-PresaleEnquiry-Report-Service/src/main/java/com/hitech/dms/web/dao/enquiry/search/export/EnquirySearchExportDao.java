package com.hitech.dms.web.dao.enquiry.search.export;

import java.util.List;

import com.hitech.dms.web.model.enquiry.list.request.EnquiryListRequestModel;
import com.hitech.dms.web.model.enquiry.list.response.EnquiryListResponseModel;

public interface EnquirySearchExportDao {
	public List<EnquiryListResponseModel> exportEnquiryList(String authorizationHeader, String userCode,
			EnquiryListRequestModel enquiryListRequestModel);
}
