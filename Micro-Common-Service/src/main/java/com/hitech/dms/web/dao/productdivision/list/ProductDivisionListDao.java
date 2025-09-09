package com.hitech.dms.web.dao.productdivision.list;

import java.util.List;

import com.hitech.dms.web.model.productdivision.request.ProductDivisionListRequestModel;
import com.hitech.dms.web.model.productdivision.response.ProductDivisionListResponseModel;

public interface ProductDivisionListDao {
	public List<ProductDivisionListResponseModel> fetchPcForBranchDealerList(String userCode,
			ProductDivisionListRequestModel requestModel);
}
