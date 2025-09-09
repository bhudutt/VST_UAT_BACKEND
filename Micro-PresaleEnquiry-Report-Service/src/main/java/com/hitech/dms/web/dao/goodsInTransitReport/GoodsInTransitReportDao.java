package com.hitech.dms.web.dao.goodsInTransitReport;

import java.math.BigInteger;
import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.goodsInTransitReport.request.GoodsInTransitReportRequest;

public interface GoodsInTransitReportDao {

	List<?> goodsInTransitSearch(String userCode, GoodsInTransitReportRequest resquest, Device device);

	List<?> itemDetailList(BigInteger modelId, String userCode);
	
	

}
