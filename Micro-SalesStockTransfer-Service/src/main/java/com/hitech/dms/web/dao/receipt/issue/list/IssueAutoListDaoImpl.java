/**
 * 
 */
package com.hitech.dms.web.dao.receipt.issue.list;

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

import com.hitech.dms.web.model.stock.receipt.issue.request.IssueAutoListRequestModel;
import com.hitech.dms.web.model.stock.receipt.issue.response.IssueAutoListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class IssueAutoListDaoImpl implements IssueAutoListDao {
	private static final Logger logger = LoggerFactory.getLogger(IssueAutoListDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<IssueAutoListResponseModel> fetchIssueListForIssue(String userCode,
			IssueAutoListRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchIssueListForIssue invoked.." + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<IssueAutoListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SA_TR_INDENT_ISSUE_AUTO_SEARCH] :userCode, :pcId, :dealerId, :branchId, :searchText";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("searchText", requestModel.getSearchText());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<IssueAutoListResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					IssueAutoListResponseModel responseModel = new IssueAutoListResponseModel();
					responseModel.setIssueId((BigInteger) row.get("IssueId"));
					responseModel.setIssueNumber((String) row.get("IssueNumber"));
					responseModel.setIssueDate((String) row.get("IssueDate"));
					responseModel.setDisplayValue((String) row.get("DisplayValue"));
					responseModel.setBranchId((BigInteger) row.get("BranchId"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setIssueBy((BigInteger) row.get("IssueBy"));
					responseModel.setIssueByName((String) row.get("IssueByName"));
					responseModel.setIssueToBranchId((BigInteger) row.get("IssueToBranchId"));
					responseModel.setIssueToBranch((String) row.get("IssueToBranch"));

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
