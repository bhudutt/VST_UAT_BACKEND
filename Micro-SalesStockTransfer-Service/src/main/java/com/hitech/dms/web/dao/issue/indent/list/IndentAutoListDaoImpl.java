/**
 * 
 */
package com.hitech.dms.web.dao.issue.indent.list;

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

import com.hitech.dms.web.model.issue.indent.list.request.IndentAutoListRequestModel;
import com.hitech.dms.web.model.issue.indent.list.response.IndentAutoListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class IndentAutoListDaoImpl implements IndentAutoListDao {
	private static final Logger logger = LoggerFactory.getLogger(IndentAutoListDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<IndentAutoListResponseModel> fetchIndentListForIssue(String userCode,
			IndentAutoListRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchIndentListForIssue invoked.." + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<IndentAutoListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SA_TR_Indent_Auto_Search] :userCode, :pcId, :dealerId, :branchId, :searchText";
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
				responseModelList = new ArrayList<IndentAutoListResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					IndentAutoListResponseModel responseModel = new IndentAutoListResponseModel();
					responseModel.setIndentId((BigInteger) row.get("IndentId"));
					responseModel.setIndentNumber((String) row.get("IndentNumber"));
					responseModel.setIndentDate((String) row.get("IndentDate"));
					responseModel.setDisplayValue((String) row.get("DisplayValue"));
					responseModel.setBranchId((BigInteger) row.get("BranchId"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setIndentBy((BigInteger) row.get("IndentBy"));
					responseModel.setIndentByName((String) row.get("IndentByName"));
					responseModel.setIndentToBranchId((BigInteger) row.get("IndentToBranchId"));
					responseModel.setIndentToBranch((String) row.get("IndentToBranch"));

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
