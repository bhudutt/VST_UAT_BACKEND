package com.hitech.dms.web.service.report.poStatusSearch;

import java.math.BigInteger;

import org.springframework.mobile.device.Device;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.report.model.PoStatusSearchList;
import com.hitech.dms.web.model.report.model.PoStatusSearchRequest;

public interface PoStatusSearchService {
	ApiResponse<?>  autoCompletePoNo(String poNo, BigInteger branchId);
	
	PoStatusSearchList poStatusReportSearch(String userCode, PoStatusSearchRequest resquest, Device device);

}
