/**
 * 
 */
package com.hitech.dms.web.dao.opex.search;

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

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.opex.search.request.OpexBudgetSearchRequestModel;
import com.hitech.dms.web.model.opex.search.response.OpexBudgetSearchMainResponseModel;
import com.hitech.dms.web.model.opex.search.response.OpexBudgetSearchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class OpexBudgetSearchDaoImpl implements OpexBudgetSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(OpexBudgetSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public OpexBudgetSearchMainResponseModel searchOpexBudgetList(Session session, String userCode,
			OpexBudgetSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchOpexBudgetList invoked.." + requestModel.toString());
		}
		Query query = null;
		OpexBudgetSearchMainResponseModel responseListModel = null;
		List<OpexBudgetSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SA_MIS_OPEX_BUDGET_LIST] :userCode, :OrgHierId, :pcID, :opexBudgetNumber, :stateId, :fromDate, :todate, "
				+ " :includeInactive, :page, :size";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("OrgHierId", requestModel.getOrgHierID());
			query.setParameter("pcID", requestModel.getPcId());
			query.setParameter("opexBudgetNumber", requestModel.getOpexNumber());
			query.setParameter("stateId", requestModel.getStateId());
			query.setParameter("fromDate", (requestModel.getFromDate() == null ? null
					: DateToStringParserUtils.addStratTimeOFTheDay(requestModel.getFromDate())));
			query.setParameter("todate", (requestModel.getToDate() == null ? null
					: DateToStringParserUtils.addEndTimeOFTheDay(requestModel.getToDate())));
			query.setParameter("includeInactive", requestModel.getIncludeInActive());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel = new OpexBudgetSearchMainResponseModel();
				responseModelList = new ArrayList<OpexBudgetSearchResponseModel>();
				OpexBudgetSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new OpexBudgetSearchResponseModel();
					responseModel.setId((BigInteger) row.get("OpexId"));
					responseModel.setAction((String) row.get("Action"));
					responseModel.setState((String) row.get("State"));
					responseModel.setProfitCenter((String) row.get("ProfitCenter"));
					responseModel.setOpexNumber((String) row.get("OpexNumber"));
					responseModel.setOpexDate((String) row.get("OpexDate"));
					responseModel.setOpexStatus((String) row.get("OpexStatus"));
					responseModel.setRemarks((String) row.get("Remarks"));
					responseModel.setFinYear((String) row.get("FinYear"));
					responseModel.setOpexUpdatedDate((String) row.get("OpexUpdatedDate"));
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}

					responseModelList.add(responseModel);
				}

				responseListModel.setRecordCount(recordCount);
				responseListModel.setSearchList(responseModelList);

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseListModel;
	}

	@Override
	public OpexBudgetSearchMainResponseModel searchOpexBudgetList(String userCode,
			OpexBudgetSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchOpexBudgetList invoked.." + requestModel.toString());
		}
		Session session = null;
		OpexBudgetSearchMainResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = searchOpexBudgetList(session, userCode, requestModel);
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
		return responseModel;
	}
}
