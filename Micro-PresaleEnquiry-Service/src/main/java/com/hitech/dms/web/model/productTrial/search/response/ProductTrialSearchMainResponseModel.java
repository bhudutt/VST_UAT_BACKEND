package com.hitech.dms.web.model.productTrial.search.response;

import java.util.List;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class ProductTrialSearchMainResponseModel {
	private List<ProductTrialSearchResponseModel> productTrailSearch;
	private Integer recordCount;
}
