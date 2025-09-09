/**
 * 
 */
package com.hitech.dms.web.dao.syslookup;

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

import com.hitech.dms.web.model.lookup.request.SysLookupRequestModel;
import com.hitech.dms.web.model.lookup.response.SysLookupResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class SysLookupDaoImpl implements SysLookupDao {
	private static final Logger logger = LoggerFactory.getLogger(SysLookupDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<SysLookupResponseModel> fetchSysLookupList(String userCode, String lookupType, String includeInactive) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchSysLookupList invoked.." + lookupType);
		}
		Session session = null;
		Query query = null;
		List<SysLookupResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_GetLookupDetails] :lookupType, :includeInactive";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("lookupType", lookupType);
			query.setParameter("includeInactive", includeInactive);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SysLookupResponseModel>();
				SysLookupResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SysLookupResponseModel();
					responseModel.setValueId((BigInteger) row.get("value_id"));
					responseModel.setValueCode((String) row.get("valueCode"));
					responseModel.setDisplayValue((String) row.get("displayValue"));
					responseModelList.add(responseModel);
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
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<SysLookupResponseModel> fetchSysLookupList(String userCode,
			SysLookupRequestModel sysLookupRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchSysLookupList invoked.." + sysLookupRequestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<SysLookupResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_GetLookupDetails] :lookupType, :includeInactive";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("lookupType", sysLookupRequestModel.getLookupType());
			query.setParameter("includeInactive", sysLookupRequestModel.getIsIncludeActive());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SysLookupResponseModel>();
				SysLookupResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SysLookupResponseModel();
					responseModel.setValueId((BigInteger) row.get("value_id"));
					responseModel.setValueCode((String) row.get("valueCode"));
					responseModel.setDisplayValue((String) row.get("displayValue"));
					responseModelList.add(responseModel);
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
		return responseModelList;
	}
}
