/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.stage;

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

import com.hitech.dms.web.model.enquiry.stages.request.EnqStageRequestModel;
import com.hitech.dms.web.model.enquiry.stages.response.EnqStageResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class EnqStageDaoImpl implements EnqStageDao {
	private static final Logger logger = LoggerFactory.getLogger(EnqStageDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<EnqStageResponseModel> fetchEnqStageList(String userCode, String isFor) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqStageList invoked.." + isFor);
		}
		Session session = null;
		Query query = null;
		List<EnqStageResponseModel> stageResponseModelList = null;
		String sqlQuery = "exec [SP_ENQ_getStageList] :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("isFor", isFor);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				stageResponseModelList = new ArrayList<EnqStageResponseModel>();
				EnqStageResponseModel stageResponseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					stageResponseModel = new EnqStageResponseModel();
					stageResponseModel.setEnqStageId((Integer) row.get("enq_stage_id"));
					stageResponseModel.setStageCode((String) row.get("StageCode"));
					stageResponseModel.setStageDesc((String) row.get("Stage_desc"));
					stageResponseModelList.add(stageResponseModel);
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
		return stageResponseModelList;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<EnqStageResponseModel> fetchEnqStageList(String userCode, EnqStageRequestModel enqStageRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqStageList invoked.." + enqStageRequestModel.getIsFor());
		}
		Session session = null;
		Query query = null;
		List<EnqStageResponseModel> stageResponseModelList = null;
		String sqlQuery = "exec [SP_ENQ_getStageList] :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("isFor", enqStageRequestModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				stageResponseModelList = new ArrayList<EnqStageResponseModel>();
				EnqStageResponseModel stageResponseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					stageResponseModel = new EnqStageResponseModel();
					stageResponseModel.setEnqStageId((Integer) row.get("enq_stage_id"));
					stageResponseModel.setStageCode((String) row.get("StageCode"));
					stageResponseModel.setStageDesc((String) row.get("Stage_desc"));
					stageResponseModelList.add(stageResponseModel);
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
		return stageResponseModelList;
	}
}
