package com.hitech.dms.web.dao.report.poStatusSearch;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Session;
import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.report.model.PoStatusSearchRequest;

public interface PoStatusSearchDao {
	
	List<?> autoCompletePoNo(Session session, String poNo, BigInteger branchId);
	
	List<?> poStatusReportSearch(Session session, String userCode, PoStatusSearchRequest resquest, Device device);

}
