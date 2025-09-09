package com.hitech.dms.web.dao.report.partGrnSearch;

import java.util.List;

import org.hibernate.Session;
import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.report.model.PartGrnSearchRequest;

public interface PartGrnSearchDao {
	
	List<?> partGrnReportSearch(Session session, String userCode, PartGrnSearchRequest resquest, Device device);

}
