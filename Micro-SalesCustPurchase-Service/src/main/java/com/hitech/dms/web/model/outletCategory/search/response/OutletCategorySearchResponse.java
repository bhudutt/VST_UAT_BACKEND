package com.hitech.dms.web.model.outletCategory.search.response;

import java.util.List;

import lombok.Data;

@Data
public class OutletCategorySearchResponse {
	
	private int statusCode;
	private String statusMessage;
	private List<OutletCategoryPartsModel> outletCategoryModel;

}
