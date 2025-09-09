package com.hitech.dms.web.service.goodsInTransitReport;

import java.math.BigInteger;
import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.goodsInTransitReport.request.GoodsInTransitReportRequest;
import com.hitech.dms.web.model.goodsInTransitReport.response.GoodsInTransitReportRepList;
import com.hitech.dms.web.model.goodsInTransitReport.response.GoodsInTransitReportResponse;
import com.hitech.dms.web.model.goodsInTransitReport.response.ModelItemList;

public interface GoodsInTransitReportService {

	GoodsInTransitReportRepList goodsInTransitSearch(String userCode, GoodsInTransitReportRequest resquest,
			Device device);

	List<ModelItemList> itemDetailList(BigInteger modelId, String userCode);
	
	
	
	

}
