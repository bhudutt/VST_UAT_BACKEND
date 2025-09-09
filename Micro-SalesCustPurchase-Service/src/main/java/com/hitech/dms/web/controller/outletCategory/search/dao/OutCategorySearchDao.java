package com.hitech.dms.web.controller.outletCategory.search.dao;

import java.util.List;

import com.hitech.dms.web.model.outletCategory.search.response.OutletCategoryPartsModel;
import com.hitech.dms.web.model.outletCategory.search.response.OutletCategorySearchResponse;

public interface OutCategorySearchDao {
	
	public OutletCategorySearchResponse getOutLetCategorySearch(String userCode,String categoryId);
	public List<OutletCategoryPartsModel> getOutLetCategoryList(String userCode ,String categoryId, String partyCode);

}
