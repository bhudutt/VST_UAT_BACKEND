package com.hitech.dms.web.dao.admin.village.add;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.admin.village.AddVillageDetailEntity;
import com.hitech.dms.web.model.admin.village.request.VillageRequest;
import com.hitech.dms.web.model.admin.village.response.VillageResponse;

@Repository
public class VillageDaoImpl implements VillageDao {
	
	
	private static final Logger logger = LoggerFactory.getLogger(VillageDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	
	
	@Override
	public VillageResponse addVillage(String userCode, List<VillageRequest> requestModel) {
	    VillageResponse response = new VillageResponse();
	    try (Session session = sessionFactory.openSession()) {
	        Transaction transaction = session.beginTransaction();
	        Integer id = null;
	        String result =null;
	        if (requestModel != null) {
	            for (VillageRequest bean : requestModel) {
	                AddVillageDetailEntity detailEntity = new AddVillageDetailEntity();
	                BeanUtils.copyProperties(bean, detailEntity);
	                id = (Integer) session.save(detailEntity);
	            }
	            session.flush(); // Ensure changes are flushed to the database
	        }

	        if (id != null) {
	            transaction.commit();
	             result = sendToProce(session); // Execute procedure after commit
	        } 
	        if(result.equals("Successfully")) {
	            response.setVillageId(id);
	            response.setStatusCode(WebConstants.STATUS_OK_200);
	            response.setMsg("Add village saved successfully.");
	    	}else if(result.equals("Failed")) {
	        	  transaction.rollback();
		          response.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
		          response.setMsg("Data Already Exist...");
	        } else {
	            transaction.rollback();
	            response.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	            response.setMsg("Failed to save village.");
	        }
	    } catch (Exception e) {
	        logger.error("Server side error in add village", e);
	        response.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	        response.setMsg("Server side error in add village: " + e.getMessage());
	    }
	    return response;
	}

	private String sendToProce(Session session) {
	    String sqlQuery = "exec [INSERT_CM_GEO_Mapping]";
	    Transaction transaction = null;
	    String result= null;
	    try {
	    	transaction = session.beginTransaction();
	        org.hibernate.query.Query<?> query = session.createSQLQuery(sqlQuery);
	        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
	        List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					
					result= (String) row.get("Resultset");
				}
			}
	        query.executeUpdate();
	        transaction.commit();
	    } catch (Exception e) {
	        logger.error("Error executing stored procedure: ", e);
	    }
	    return result;
	}

}
