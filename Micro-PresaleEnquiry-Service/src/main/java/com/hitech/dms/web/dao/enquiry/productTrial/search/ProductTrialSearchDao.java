package com.hitech.dms.web.dao.enquiry.productTrial.search;

import com.hitech.dms.web.model.productTrial.search.request.ProductTrialSearchRequestModel;
import com.hitech.dms.web.model.productTrial.search.response.ProductTrialSearchMainResponseModel;

/**
 * @author vinay.gautam
 *
 */
public interface ProductTrialSearchDao {
	public ProductTrialSearchMainResponseModel fetchProductTrialSearchList(String userCode, ProductTrialSearchRequestModel requestModel);

}
