package com.hitech.dms.web.dao.enquiry.model;

import java.util.List;

import com.hitech.dms.web.model.enquiry.model.request.BrandListRequestModel;
import com.hitech.dms.web.model.enquiry.model.request.EnqModelRequestModel;
import com.hitech.dms.web.model.enquiry.model.request.ModelItemDTLRequestModel;
import com.hitech.dms.web.model.enquiry.model.request.ModelItemListRequestModel;
import com.hitech.dms.web.model.enquiry.model.request.ModelVariantListRequestModel;
import com.hitech.dms.web.model.enquiry.model.response.BrandListResponseModel;
import com.hitech.dms.web.model.enquiry.model.response.EnqModelResponseModel;
import com.hitech.dms.web.model.enquiry.model.response.ModelItemDTLResponseModel;
import com.hitech.dms.web.model.enquiry.model.response.ModelItemListResponseModel;
import com.hitech.dms.web.model.enquiry.model.response.ModelVariantListResponseModel;

public interface EnqModelDao {
	public List<EnqModelResponseModel> fetchEnqModelList(String userCode, Integer pcId, Long activityPlanID,
			String searchText, Long activityId, Long dealerId);

	public List<EnqModelResponseModel> fetchEnqModelList(String userCode, EnqModelRequestModel enqModelRequestModel);

	public List<ModelVariantListResponseModel> fetchEnqVariantModelList(String userCode, Long modelID);

	public List<ModelVariantListResponseModel> fetchEnqVariantModelList(String userCode,
			ModelVariantListRequestModel modelVariantListRequestModel);

	public List<ModelItemListResponseModel> fetchEnqModelItemList(String userCode, Integer pcID, Long activityPlanID,
			Long modelID, String searchValue, Long activityId, Long dealerId, String isFor);

	public List<ModelItemListResponseModel> fetchEnqModelItemList(String userCode,
			ModelItemListRequestModel modelItemListRequestModel);

	public ModelItemDTLResponseModel fetchEnqModelItemDTL(String userCode, Long itemID);

	public ModelItemDTLResponseModel fetchEnqModelItemDTL(String userCode,
			ModelItemDTLRequestModel modelItemDTLRequestModel);

	public List<BrandListResponseModel> fetchEnqBrandList(String userCode, String isFor);

	public List<BrandListResponseModel> fetchEnqBrandList(String userCode,
			BrandListRequestModel modelItemListRequestModel);
}
