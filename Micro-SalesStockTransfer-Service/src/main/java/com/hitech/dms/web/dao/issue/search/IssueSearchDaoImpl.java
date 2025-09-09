/**
 * 
 */
package com.hitech.dms.web.dao.issue.search;

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
import com.hitech.dms.web.model.issue.search.request.IssueSearchRequestModel;
import com.hitech.dms.web.model.issue.search.response.IssueSearchMainResponseModel;
import com.hitech.dms.web.model.issue.search.response.IssueSearchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class IssueSearchDaoImpl implements IssueSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(IssueSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public IssueSearchMainResponseModel searchIssueList(Session session, String userCode,
			IssueSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchIssueList invoked.." + requestModel.toString());
		}
		Query query = null;
		IssueSearchMainResponseModel responseListModel = null;
		List<IssueSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_SA_TR_ISSUE_SEARCH] :userCode, :pcID, :orgHierID, :dealerID, :branchID, :issueNumber, :issueBy, :issueToBranchId, :series, :segment, :model, :variant, :fromDate, :todate, "
				+ " :includeInactive, :page, :size";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcID", requestModel.getPcId());
			query.setParameter("orgHierID", requestModel.getOrgHierID());
			query.setParameter("dealerID", requestModel.getDealerId());
			query.setParameter("branchID", requestModel.getBranchId());
			query.setParameter("issueNumber", requestModel.getIssueNumber());
			query.setParameter("issueBy", requestModel.getIssueBy());
			query.setParameter("issueToBranchId", requestModel.getIssueToBranchId());
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
				responseListModel = new IssueSearchMainResponseModel();
				responseModelList = new ArrayList<IssueSearchResponseModel>();
				IssueSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new IssueSearchResponseModel();
					responseModel.setId((BigInteger) row.get("IssueId"));
					responseModel.setId1((BigInteger) row.get("dealerId"));
					responseModel.setId2((BigInteger) row.get("branchId"));
					responseModel.setDealerShip((String) row.get("DealerShip"));
					responseModel.setProfitCenter((String) row.get("ProfitCenter"));
					responseModel.setIssueNumber((String) row.get("IssueNumber"));
					responseModel.setIssueDate((String) row.get("IndentDate"));
					responseModel.setIssueBy((String) row.get("IssueBy"));
					responseModel.setIssueFrom((String) row.get("IssueFrom"));
					responseModel.setIssueToBranch((String) row.get("IssueToBranch"));
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
	public IssueSearchMainResponseModel searchIssueList(String userCode, IssueSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchIssueList invoked.." + requestModel.toString());
		}
		Session session = null;
		IssueSearchMainResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			responseModel = searchIssueList(session, userCode, requestModel);
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
