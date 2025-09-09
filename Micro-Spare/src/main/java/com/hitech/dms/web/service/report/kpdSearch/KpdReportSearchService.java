package com.hitech.dms.web.service.report.kpdSearch;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.report.model.KpdOrderStatusSearchList;
import com.hitech.dms.web.model.report.model.KpdOrderStatusSearchRequest;

public interface KpdReportSearchService {

	KpdOrderStatusSearchList kpdOrderReportSearch(String userCode, KpdOrderStatusSearchRequest resquest, Device device);
}
