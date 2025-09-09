package com.hitech.dms.web.dao.quotation.search;

import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.quotation.search.request.EnquiryDTLForQUORequestModel;
import com.hitech.dms.web.model.quotation.search.request.VehQuoSearchRequestModel;
import com.hitech.dms.web.model.quotation.search.response.EnquiryDTLForQUOResponseModel;
import com.hitech.dms.web.model.quotation.search.response.VehQuoSearchMainResponseModel;

public interface QuotationSearchDao {
	public List<EnquiryDTLForQUOResponseModel> fetchEnquiryListForQuotation(String userCode,
			EnquiryDTLForQUORequestModel enquiryListRequestModel);
	
	public VehQuoSearchMainResponseModel searchQuotationList(String userCode, VehQuoSearchRequestModel requestModel,
			Device device);
}
