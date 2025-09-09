/**
 * 
 */
package com.hitech.dms.web.dao.indent.search;

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
import com.hitech.dms.web.model.indent.search.request.IndentSearchRequestModel;
import com.hitech.dms.web.model.indent.search.response.IndentSearchMainResponseModel;
import com.hitech.dms.web.model.indent.search.response.IndentSearchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class IndentSearchDaoImpl implements IndentSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(IndentSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public IndentSearchMainResponseModel searchIndentList(Session session, String userCode,
			IndentSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchIndentList invoked.." + requestModel.toString());
		}
		Query query = null;
		IndentSearchMainResponseModel responseListModel = null;
		List<IndentSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SA_TR_INDENT_SEARCH] :userCode, :pcID, :orgHierID, :dealerID, :branchID, :indentNumber, :indentBy, :indentToBranchId, :series, :segment, :model, :variant, :indentStatus, :fromDate, :todate, "
				+ " :includeInactive, :page, :size";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcID", requestModel.getPcId());
			query.setParameter("orgHierID", requestModel.getOrgHierID());
			query.setParameter("dealerID", requestModel.getDealerId());
			query.setParameter("branchID", requestModel.getBranchId());
			query.setParameter("indentNumber", requestModel.getIndentNumber());
			query.setParameter("indentBy", requestModel.getIndentBy());
			query.setParameter("indentToBranchId", requestModel.getIndentToBranchId());
			query.setParameter("series", requestModel.getSeries());
			query.setParameter("segment", requestModel.getSegment());
			query.setParameter("model", requestModel.getModel());
			query.setParameter("variant", requestModel.getVariant());
			query.setParameter("indentStatus", requestModel.getIndentStatus());
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
				responseListModel = new IndentSearchMainResponseModel();
				responseModelList = new ArrayList<IndentSearchResponseModel>();
				IndentSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new IndentSearchResponseModel();
					responseModel.setId((BigInteger) row.get("indentId"));
					responseModel.setId1((BigInteger) row.get("dealerId"));
					responseModel.setId2((BigInteger) row.get("branchId"));
					responseModel.setDealerShip((String) row.get("DealerShip"));
					responseModel.setProfitCenter((String) row.get("ProfitCenter"));
					responseModel.setIndentNumber((String) row.get("IndentNumber"));
					responseModel.setIndentDate((String) row.get("IndentDate"));
//					responseModel.setIndentStatus((String) row.get("IndentStatus"));
					responseModel.setIndentBy((String) row.get("IndentBy"));
					responseModel.setIndentFrom((String) row.get("IndentFrom"));
					responseModel.setIndentToBranch((String) row.get("IndentToBranch"));
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
	public IndentSearchMainResponseModel searchIndentList(String userCode, IndentSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchIndentList invoked.." + requestModel.toString());
		}
		Session session = null;
		IndentSearchMainResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = searchIndentList(session, userCode, requestModel);
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
