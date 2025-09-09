package com.hitech.dms.web.dao.report.kpdSearch;

import java.util.List;

import org.hibernate.Session;
import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.report.model.KpdOrderStatusSearchRequest;

public interface KpdOrderStatusSearchDao {
	
	List<?> kpdOrderReportSearch(Session session, String userCode, KpdOrderStatusSearchRequest resquest, Device device);

}
