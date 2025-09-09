/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.sources;

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

import com.hitech.dms.web.model.enquiry.sources.request.EnqSourcesRequestModel;
import com.hitech.dms.web.model.enquiry.sources.response.EnqSourcesResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class EnqSourcesDaoImpl implements EnqSourcesDao {
	private static final Logger logger = LoggerFactory.getLogger(EnqSourcesDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<EnqSourcesResponseModel> fetchEnqSourcesList(String userCode, String isApplicableFor) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqSourcesList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<EnqSourcesResponseModel> sourcesResponseModelList = null;
		String sqlQuery = "exec [SP_ENQ_getSources] :isApplicableFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("isApplicableFor", isApplicableFor);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				sourcesResponseModelList = new ArrayList<EnqSourcesResponseModel>();
				EnqSourcesResponseModel sourcesResponseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					sourcesResponseModel = new EnqSourcesResponseModel();
					sourcesResponseModel.setSourceId((Integer) row.get("sourceId"));
					sourcesResponseModel.setSourceCode((String) row.get("sourceCode"));
					sourcesResponseModel.setSourceName((String) row.get("sourceName"));
					sourcesResponseModel.setIsDigitalSource((String) row.get("IsDigitalSource"));
					sourcesResponseModel.setIsFieldSource((String) row.get("IsFieldSource"));
					sourcesResponseModelList.add(sourcesResponseModel);
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
		return sourcesResponseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<EnqSourcesResponseModel> fetchEnqSourcesList(String userCode,
			EnqSourcesRequestModel sourcesRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqSourcesList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<EnqSourcesResponseModel> sourcesResponseModelList = null;
		String sqlQuery = "exec [SP_ENQ_getSources] :isApplicableFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("isApplicableFor", sourcesRequestModel.getIsApplicableFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				sourcesResponseModelList = new ArrayList<EnqSourcesResponseModel>();
				EnqSourcesResponseModel sourcesResponseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					sourcesResponseModel = new EnqSourcesResponseModel();
					sourcesResponseModel.setSourceId((Integer) row.get("sourceId"));
					sourcesResponseModel.setSourceCode((String) row.get("sourceCode"));
					sourcesResponseModel.setSourceName((String) row.get("sourceName"));
					sourcesResponseModel.setIsDigitalSource((String) row.get("IsDigitalSource"));
					sourcesResponseModel.setIsFieldSource((String) row.get("IsFieldSource"));
					sourcesResponseModelList.add(sourcesResponseModel);
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
		return sourcesResponseModelList;
	}
}
