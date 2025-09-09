/**
 * 
 */
package com.hitech.dms.web.dao.addtionalcriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.DateUtils;
import com.hitech.dms.web.model.scheme.additionalCriteria.request.AdditinalCriteriaRequestModel;
import com.hitech.dms.web.model.scheme.additionalCriteria.response.AdditinalCriteriaResponseModel;
import com.hitech.dms.web.model.scheme.additionalCriteria.response.SchemeTypeOnchangeResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class AdditinalCriteriaDaoImpl implements AdditinalCriteriaDao {
	private static final Logger logger = LoggerFactory.getLogger(AdditinalCriteriaDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public SchemeTypeOnchangeResponseModel fetchSchemeTypeOnchangeDetail(String userCode,
			AdditinalCriteriaRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchSchemeTypeOnchangeDetail invoked..");
		}
		Session session = null;
		Query<?> query = null;
		SchemeTypeOnchangeResponseModel responseModel = new SchemeTypeOnchangeResponseModel();
		String sqlQuery = "exec [SP_SA_SCHEME_CRITERIA_LIST] :userCode, :schemeTypeId";
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("schemeTypeId", requestModel.getSchemeTypeId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				List<AdditinalCriteriaResponseModel> additionalCriteriaList = new ArrayList<AdditinalCriteriaResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					AdditinalCriteriaResponseModel model = new AdditinalCriteriaResponseModel();
					model.setId((Integer) row.get("Id"));
					model.setCriteriaCode((String) row.get("CriteriaCode"));
					model.setCriteriaName((String) row.get("CriteriaName"));
					model.setCriteriaPer((Float) row.get("CriteriaPer"));

					additionalCriteriaList.add(model);
				}
				responseModel.setAdditionalCriteriaList(additionalCriteriaList);
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			responseModel.setMonthList(DateUtils.getDayMonth(3));
			responseModel.setYearList(DateUtils.getYears(1));
		}
		return responseModel;
	}
}
