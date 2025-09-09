package com.hitech.dms.web.dao.dc.search;

import com.hitech.dms.web.model.dc.search.request.DcSearchRequestModel;
import com.hitech.dms.web.model.dc.search.response.DcSearchMainResponseModel;
import com.hitech.dms.web.model.dc.search.response.StockDetailsMainResponseModel;

public interface DcSearchDao {
	public DcSearchMainResponseModel searchDcList(String userCode, DcSearchRequestModel requestModel);
	public StockDetailsMainResponseModel searchStock(String userCode, DcSearchRequestModel requestModel);
	public StockDetailsMainResponseModel updateStock(String userCode,Integer vinId);
	public void deleteStockErrorHistory();
}
