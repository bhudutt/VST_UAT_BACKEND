package com.hitech.dms.web.dao.models;

import java.util.List;

import com.hitech.dms.web.model.models.request.MachineItemDTLRequestModel;
import com.hitech.dms.web.model.models.request.MachineItemRequestModel;
import com.hitech.dms.web.model.models.request.ModelItemListRequestModel;
import com.hitech.dms.web.model.models.request.ModelsForSeriesSegmentRequestModel;
import com.hitech.dms.web.model.models.request.ProductListRequestModel;
import com.hitech.dms.web.model.models.request.SeriesSegmentRequestModel;
import com.hitech.dms.web.model.models.response.MachineItemResponseModel;
import com.hitech.dms.web.model.models.response.ModelByPcIdResponseModel;
import com.hitech.dms.web.model.models.response.ModelItemListResponseModel;
import com.hitech.dms.web.model.models.response.ModelsForSeriesSegmentResponseModel;
import com.hitech.dms.web.model.models.response.ProductListResponseModel;
import com.hitech.dms.web.model.models.response.SeriesSegmentResponseModel;

public interface ProductListDao {
	public List<ProductListResponseModel> fetchProductList(String userCode, String productLevelName,
			String searchParentText, Long pcID);

	public List<ProductListResponseModel> fetchProductList(String userCode,
			ProductListRequestModel productListRequestModel);

	public List<SeriesSegmentResponseModel> fetchSeriesSegmentList(String userCode,
			SeriesSegmentRequestModel productListRequestModel);

	public List<ModelsForSeriesSegmentResponseModel> fetchModelsForSeriesSegment(String userCode,
			ModelsForSeriesSegmentRequestModel ssModel);

	public List<MachineItemResponseModel> fetchItemDTLList(String userCode, MachineItemRequestModel ssModel);

	public MachineItemResponseModel fetchItemDTL(String userCode, MachineItemDTLRequestModel ssModel);
	
	public List<ModelItemListResponseModel> fetchModelItemList(String userCode,
			ModelItemListRequestModel modelItemListRequestModel);
	
	public List<ModelByPcIdResponseModel> fetchModelListByPcId(String userCode,
			Integer pcId, String isFor);
}
