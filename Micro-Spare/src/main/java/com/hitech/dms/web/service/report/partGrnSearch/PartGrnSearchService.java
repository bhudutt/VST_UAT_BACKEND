package com.hitech.dms.web.service.report.partGrnSearch;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.report.model.PartGrnSearchList;
import com.hitech.dms.web.model.report.model.PartGrnSearchRequest;

public interface PartGrnSearchService {
	
	PartGrnSearchList partGrnSearch(String userCode, PartGrnSearchRequest resquest, Device device);

}
