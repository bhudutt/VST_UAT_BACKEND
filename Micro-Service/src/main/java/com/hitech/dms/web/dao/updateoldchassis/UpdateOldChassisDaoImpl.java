package com.hitech.dms.web.dao.updateoldchassis;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.updateoldchassis.VinAndCustDtlResponse;

@Repository
public class UpdateOldChassisDaoImpl implements UpdateOldChassisDao{
	
	@Override
	public List<?> autoSearchChassisNo(Session session, String chassisNo) {
		String sqlQuery = "exec [SA_GET_CHASSIS_NO] :chassisNo";

	    Query<?> query = null;
	    List<?> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("chassisNo", chassisNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}

	@Override
	public List<VinAndCustDtlResponse> fetchVinAndCustDetails(Session session, String chassisNo) {
	    String sqlQuery = "exec [Get_Vin_and_Cust_Details_By_ChassisNo] :chassisNo";

	    Query<VinAndCustDtlResponse> query = null;
	    List<VinAndCustDtlResponse> data = null;

	    try {
	        query = session.createSQLQuery(sqlQuery);
	        query.setParameter("chassisNo", chassisNo);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        data = query.list();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return data;
	}
}
