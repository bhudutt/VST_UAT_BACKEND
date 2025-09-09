package com.hitech.dms.web.dao.updateoldchassis;

import java.util.List;

import org.hibernate.Session;

import com.hitech.dms.web.model.updateoldchassis.VinAndCustDtlResponse;

public interface UpdateOldChassisDao {
	
	List<?> autoSearchChassisNo(Session session, String chassisNo);
	
	List<VinAndCustDtlResponse> fetchVinAndCustDetails(Session session, String chassisNo);

}
