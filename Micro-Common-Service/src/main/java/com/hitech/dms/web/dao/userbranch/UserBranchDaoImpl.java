/**
 * 
 */
package com.hitech.dms.web.dao.userbranch;

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

import com.hitech.dms.web.model.userbranch.request.UserBranchRequestModel;
import com.hitech.dms.web.model.userbranch.response.UserBranchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class UserBranchDaoImpl implements UserBranchDao {
	private static final Logger logger = LoggerFactory.getLogger(UserBranchDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<UserBranchResponseModel> fetchUserBranchList(String userCode, String isInactiveInclude) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchUserBranchList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<UserBranchResponseModel> userBranchResponseModelList = null;
		String sqlQuery = "Select * from [FN_CM_getBranchesUnderUser] (:userCode, :isInactiveInclude)";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("isInactiveInclude", isInactiveInclude);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				userBranchResponseModelList = new ArrayList<UserBranchResponseModel>();
				UserBranchResponseModel branchResponseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					branchResponseModel = new UserBranchResponseModel();
					branchResponseModel.setBranchID((BigInteger) row.get("BRANCH_ID"));
					branchResponseModel.setDealerID((BigInteger) row.get("dealer_id"));
					branchResponseModel.setBranchCode((String) row.get("BranchCode"));
					branchResponseModel.setBranchName((String) row.get("BranchName"));
					branchResponseModel.setBranchLocation((String) row.get("BranchLocation"));
					branchResponseModel.setIsDefaultBranch((String) row.get("IsDefaultBranch"));
					userBranchResponseModelList.add(branchResponseModel);
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
		return userBranchResponseModelList;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<UserBranchResponseModel> fetchUserBranchList(UserBranchRequestModel branchRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchUserBranchList invoked.." + branchRequestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<UserBranchResponseModel> userBranchResponseModelList = null;
		String sqlQuery = "Select * from [FN_CM_getBranchesUnderUser] (:userCode, :isInactiveInclude)";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", branchRequestModel.getUserCode());
			query.setParameter("isInactiveInclude", branchRequestModel.getIncludeInActive());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				userBranchResponseModelList = new ArrayList<UserBranchResponseModel>();
				UserBranchResponseModel branchResponseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					branchResponseModel = new UserBranchResponseModel();
					branchResponseModel.setBranchID((BigInteger) row.get("BRANCH_ID"));
					branchResponseModel.setDealerID((BigInteger) row.get("dealer_id"));
					branchResponseModel.setBranchCode((String) row.get("BranchCode"));
					branchResponseModel.setBranchName((String) row.get("BranchName"));
					branchResponseModel.setBranchLocation((String) row.get("BranchLocation"));
					branchResponseModel.setIsDefaultBranch((String) row.get("IsDefaultBranch"));
					userBranchResponseModelList.add(branchResponseModel);
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
		return userBranchResponseModelList;
	}

	@Override
	public List<UserBranchResponseModel> brancheByUserCode(String userCode, String isInactiveInclude) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchUserBranchList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<UserBranchResponseModel> userBranchResponseModelList = null;
		String sqlQuery = "Select * from [FN_CM_GETBRANCH_BYUSERCODE] (:userCode, :isInactiveInclude)";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("isInactiveInclude", isInactiveInclude);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				userBranchResponseModelList = new ArrayList<UserBranchResponseModel>();
				UserBranchResponseModel branchResponseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					branchResponseModel = new UserBranchResponseModel();
					branchResponseModel.setBranchID((BigInteger) row.get("BRANCH_ID"));
					branchResponseModel.setDealerID((BigInteger) row.get("dealer_id"));
					branchResponseModel.setBranchCode((String) row.get("BranchCode"));
					branchResponseModel.setBranchName((String) row.get("BranchName"));
					branchResponseModel.setBranchLocation((String) row.get("BranchLocation"));
					branchResponseModel.setIsDefaultBranch((String) row.get("IsDefaultBranch"));
					userBranchResponseModelList.add(branchResponseModel);
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
		return userBranchResponseModelList;
	}
}
