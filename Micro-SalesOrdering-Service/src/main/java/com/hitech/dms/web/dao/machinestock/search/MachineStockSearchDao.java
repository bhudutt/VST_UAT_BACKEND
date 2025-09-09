package com.hitech.dms.web.dao.machinestock.search;


import java.util.List;

import com.hitech.dms.web.model.machinestock.search.MachineStockListRequestModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockListResponseModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockOverallResultResponseModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockSearchRequestModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockSearchResponseModel;
import com.hitech.dms.web.model.machinestock.search.MachineStockSearchResultResponseModel;

public interface MachineStockSearchDao {

	
	public MachineStockSearchResultResponseModel machineStockSearchList(String userCode,
			MachineStockSearchRequestModel machineStockSearchRequestModel);
	
	public MachineStockOverallResultResponseModel machineStockOverAllSearchList(String userCode,
			MachineStockSearchRequestModel machineStockSearchRequestModel);
	
	public MachineStockOverallResultResponseModel machineTransitStockSearchList(String userCode,
			MachineStockSearchRequestModel machineStockSearchRequestModel);
	
	
	

	
}
