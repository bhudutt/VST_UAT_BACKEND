/**
 * 
 */
package com.hitech.dms.web.dao.aop.search;

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
import com.hitech.dms.web.model.aop.search.request.AopTargetSearchRequestModel;
import com.hitech.dms.web.model.aop.search.response.AopTargetSearchMainResponseModel;
import com.hitech.dms.web.model.aop.search.response.AopTargetSearchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class AopTargetSearchDaoImpl implements AopTargetSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(AopTargetSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public AopTargetSearchMainResponseModel searchAopTargetList(Session session, String userCode,
			AopTargetSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchAopTargetList invoked.." + requestModel.toString());
		}
		Query query = null;
		AopTargetSearchMainResponseModel responseListModel = null;
		List<AopTargetSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SA_MIS_AOP_TARGET_LIST] :userCode, :orgHierID, :pcID, :dealerId, :aopTargetNumber, :series, :segment, :model, :variant, :fromDate, :todate, "
				+ " :includeInactive, :page, :size";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("orgHierID", requestModel.getOrgHierID());
			query.setParameter("pcID", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("aopTargetNumber", requestModel.getAopTargetNumber());
			query.setParameter("series", requestModel.getSeries());
			query.setParameter("segment", requestModel.getSegment());
			query.setParameter("model", requestModel.getModel());
			query.setParameter("variant", requestModel.getVariant());
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
				responseListModel = new AopTargetSearchMainResponseModel();
				responseModelList = new ArrayList<AopTargetSearchResponseModel>();
				AopTargetSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new AopTargetSearchResponseModel();
					responseModel.setId((BigInteger) row.get("AopId"));
					responseModel.setAction((String) row.get("Action"));
					responseModel.setDealerShip((String) row.get("DealerShip"));
					responseModel.setProfitCenter((String) row.get("ProfitCenter"));
					responseModel.setAopNumber((String) row.get("AopNumber"));
					responseModel.setAopDate((String) row.get("AopDate"));
					responseModel.setAopStatus((String) row.get("AopStatus"));
					responseModel.setRemarks((String) row.get("Remarks"));
					responseModel.setFinYear((String) row.get("FinYear"));
					responseModel.setAopUpdatedDate((String) row.get("AopUpdatedDate"));
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
	public AopTargetSearchMainResponseModel searchAopTargetList(String userCode,
			AopTargetSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchAopTargetList invoked.." + requestModel.toString());
		}
		Session session = null;
		AopTargetSearchMainResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = searchAopTargetList(session, userCode, requestModel);
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
