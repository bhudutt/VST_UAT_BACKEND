/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.prospectType;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.enquiry.prospect.request.EnqProspectRequestModel;
import com.hitech.dms.web.model.enquiry.prospect.response.EnqProspectResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class EnqProspectTypeDaoImpl implements EnqProspectTypeDao {
	private static final Logger logger = LoggerFactory.getLogger(EnqProspectTypeDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<EnqProspectResponseModel> fetchEnqProspectTypeList(String userCode, String enqOrFollowupdate, String edd) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqProspectTypeList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<EnqProspectResponseModel> enqProspectResponseModelList = null;
		String sqlQuery = "exec [SP_ENQ_getProspectType] :enqOrFollowupdate, :edd";
		try {
			System.out.println("get enqorfollowupdate  "+enqOrFollowupdate);
			System.out.println("get edd  "+edd);
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("enqOrFollowupdate", enqOrFollowupdate);
			query.setParameter("edd", edd);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				enqProspectResponseModelList = new ArrayList<EnqProspectResponseModel>();
				EnqProspectResponseModel enqProspectResponseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					enqProspectResponseModel = new EnqProspectResponseModel();
					enqProspectResponseModel.setEnqTypeId((BigInteger) row.get("sourceId"));
					enqProspectResponseModel.setEnqType((String) row.get("sourceCode"));
					enqProspectResponseModelList.add(enqProspectResponseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return enqProspectResponseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<EnqProspectResponseModel> fetchEnqProspectTypeList(String userCode,
			EnqProspectRequestModel enqProspectRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqProspectTypeList invoked.." + enqProspectRequestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<EnqProspectResponseModel> enqProspectResponseModelList = null;
		String sqlQuery = "exec [SP_ENQ_getProspectType] :enqOrFollowupdate, :edd";
		try {
			System.out.println("enqorfollowupdate  "+enqProspectRequestModel.getEnqOrFollowupdate());
			System.out.println("edd  "+enqProspectRequestModel.getEdd());
			
			String date=enqProspectRequestModel.getEnqOrFollowupdate();
	    	String[] arr=date.split("-");
	    	String month=arr[1].toString();
	    	System.out.println(month);
	    	String newDate=month.length()>3?month.substring(0, 3):month;
	    	String followupdate=arr[0]+"-"+newDate+"-"+arr[2];
	    	System.out.println(followupdate);
	    	
	    	
	    	String dateeod=enqProspectRequestModel.getEdd();
	    	String[] arreod=dateeod.split("-");
	    	String montheod=arreod[1].toString();
	    	System.out.println(montheod);
	    	String newDateeod=montheod.length()>3?montheod.substring(0, 3):montheod;
	    	System.out.println(newDateeod);
	    	String followupEod=arreod[0]+"-"+newDateeod+"-"+arreod[2];
	    	System.out.println(followupEod);
			
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("enqOrFollowupdate", followupdate);
			query.setParameter("edd", followupEod);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				enqProspectResponseModelList = new ArrayList<EnqProspectResponseModel>();
				EnqProspectResponseModel enqProspectResponseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					enqProspectResponseModel = new EnqProspectResponseModel();
					enqProspectResponseModel.setEnqTypeId((BigInteger) row.get("Enq_type_id"));
					enqProspectResponseModel.setEnqType((String) row.get("Enq_Type"));
					enqProspectResponseModelList.add(enqProspectResponseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return enqProspectResponseModelList;
	}
}
