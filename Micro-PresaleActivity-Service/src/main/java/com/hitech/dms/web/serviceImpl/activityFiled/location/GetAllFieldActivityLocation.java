package com.hitech.dms.web.serviceImpl.activityFiled.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.model.activity.create.response.PoPlantResponseModel;
import com.hitech.dms.web.service.activityFiled.location.GetActivityFieldLocationList;


@Service
public class GetAllFieldActivityLocation implements GetActivityFieldLocationList{
	
	//private static final  Logger logger=LoggerFactory.get
	private static final Logger logger=LoggerFactory.getLogger(GetAllFieldActivityLocation.class);

	@Autowired
	SessionFactory sessionFactory;
	
	
//	@Override
//	public List<String> getAllLocationList(String userCode,Integer dealerId) {
//		
//		System.out.println("called from getLocation List");
//		List<String> AllLocation = null;
//		Session session=null;
//		String SqlQuery=null;
//		Query query = null;
//		String sqlQuery = "SELECT CONCAT(Plant_code, ' - ', Plant_name, ' - ', Location) AS Location FROM SA_MST_PO_PLANT";
//		try
//		{
//			session = sessionFactory.openSession();
//			query = session.createSQLQuery(sqlQuery);
//			
//			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);	
//			List data = query.list();
//			if (data != null && !data.isEmpty()) {
//
//				AllLocation = new ArrayList<>();
//				for (Object object : data) {
//					Map row = (Map) object;
//					String documentNo=(String) row.get("Location");
//					System.out.println("documentNo we get "+documentNo);
////					model.setDocumentNo(documentNo!=null?documentNo:"");
//					AllLocation.add(documentNo);
//					System.out.println(AllLocation);
//					
//					
//				}
//				}
//
//			
//		}
//		catch (SQLGrammarException exp) {
//			logger.error(this.getClass().getName(), exp);
//			
//			  
//		} catch (HibernateException exp) {
//			logger.error(this.getClass().getName(), exp);
//			
//
//		} catch (Exception exp) {
//			
//
//		} finally {
//			if (session != null) {
//				session.close();
//			}
//		}
//		System.out.println("before response "+AllLocation);
//		return AllLocation;
//
//	}
	
	
	@Override
	public List<PoPlantResponseModel> getAllLocationList(String userCode,Integer dealerId) {
		Session session = null;
		Query query = null;
		List<PoPlantResponseModel> responseModelList =null;
		String sqlQuery = "exec [SP_SAORD_GETPLANTWISE_LIST] :dealerId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerId", dealerId);
		
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<PoPlantResponseModel>();
				PoPlantResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PoPlantResponseModel();
					responseModel.setPoPlantId((Integer) row.get("plantId"));
					responseModel.setPlantCode((String) row.get("plantCode"));
					responseModel.setPlantName((String) row.get("plantCode")+" - "+(String) row.get("plantName")+" - "+(String) row.get("location"));
					responseModel.setLocation((String) row.get("location"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

}
