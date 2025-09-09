/**
 * 
 */
package com.hitech.dms.web.dao.branch;

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

import com.hitech.dms.web.model.branchdtl.request.BranchDTLRequestModel;
import com.hitech.dms.web.model.branchdtl.response.BranchDTLResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class BranchDaoImpl implements BranchDao {
	private static final Logger logger = LoggerFactory.getLogger(BranchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<BranchDTLResponseModel> fetchBranchListByDealerId(String userCode, BranchDTLRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchBranchListByDealerId invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<BranchDTLResponseModel> responseModelList = null;
		BranchDTLResponseModel responseModel = null;
		String sqlQuery = "exec [SP_ADM_GETBRANCHLIST_BYDEALERID] :userCode, :dealerId, :branchId, :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<BranchDTLResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new BranchDTLResponseModel();
					responseModel.setBranchId((BigInteger) row.get("branch_id"));
					responseModel.setBranchCode((String) row.get("BranchCode"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setBranchLocation((String) row.get("BranchLocation"));
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
	@Override
	public BranchDTLResponseModel fetchMainBranchDtlByDealerId(String userCode, BranchDTLRequestModel requestModel) {
		// TODO Auto-generated method stub
		if (logger.isDebugEnabled()) {
			logger.debug("fetchBranchListByDealerId invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		BranchDTLResponseModel responseModel = null;
		String sqlQuery = "select *, "
				+ " ISNULL(stuff(coalesce(' '+nullif(DB.BranchName, ''), '') + coalesce(' '+nullif(DB.BranchCode, ''), '') + coalesce(' '+nullif(DB.BranchLocation, ''), '') ,1,1,''),'') as displayValue "
				+ " from ADM_BP_DEALER_BRANCH (nolock) DB where IsFromSAPAndMainBranch = 'Y' and parent_dealer_id =:dealerId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new BranchDTLResponseModel();
					responseModel.setBranchId((BigInteger) row.get("branch_id"));
					responseModel.setBranchCode((String) row.get("BranchCode"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setBranchLocation((String) row.get("BranchLocation"));
					responseModel.setDisplayValue((String) row.get("displayValue"));
					
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
		return responseModel;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public BranchDTLResponseModel fetchBranchDtlByBranchId(String userCode, BranchDTLRequestModel requestModel) {
		// TODO Auto-generated method stub
		if (logger.isDebugEnabled()) {
			logger.debug("fetchBranchDtlByBranchId invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		BranchDTLResponseModel responseModel = null;
		String sqlQuery = "select *, "
				+ " ISNULL(stuff(coalesce(' '+nullif(DB.BranchName, ''), '') + coalesce(' '+nullif(DB.BranchCode, ''), '') + coalesce(' '+nullif(DB.BranchLocation, ''), '') ,1,1,''),'') as displayValue "
				+ " from ADM_BP_DEALER_BRANCH (nolock) DB where branch_id =:branchId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("branchId", requestModel.getBranchId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new BranchDTLResponseModel();
					responseModel.setBranchId((BigInteger) row.get("branch_id"));
					responseModel.setBranchCode((String) row.get("BranchCode"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setBranchLocation((String) row.get("BranchLocation"));
					responseModel.setDisplayValue((String) row.get("displayValue"));
					
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
		return responseModel;
	}
}
