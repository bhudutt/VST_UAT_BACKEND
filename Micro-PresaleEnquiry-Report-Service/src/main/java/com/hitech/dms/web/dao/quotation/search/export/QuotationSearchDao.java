package com.hitech.dms.web.dao.quotation.search.export;

import java.util.List;

import com.hitech.dms.web.model.quotation.search.request.VehQuoSearchRequestModel;
import com.hitech.dms.web.model.quotation.search.response.VehQuoSearchResponse;

public interface QuotationSearchDao {
	public List<VehQuoSearchResponse> exportQuotationList(String authorizationHeader, String userCode,
			VehQuoSearchRequestModel enquiryListRequestModel);
}
