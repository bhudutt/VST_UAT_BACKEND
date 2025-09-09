/**
 * 
 */
package com.hitech.dms.web.dao.userdlr;

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

import com.hitech.dms.web.model.userdlr.request.UserDealerRequestModel;
import com.hitech.dms.web.model.userdlr.response.UserDealerResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class UserDealerDaoImpl implements UserDealerDao {
	private static final Logger logger = LoggerFactory.getLogger(UserDealerDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	public List<UserDealerResponseModel> fetchUserDealerList(String userCode, String includeInactive, String isFor) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchUserDealerList invoked.." + userCode);
		}
		Session session = null;
		List<UserDealerResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchUserDealerList(session, userCode, includeInactive, isFor);

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
	public List<UserDealerResponseModel> fetchUserDealerList(Session session, String userCode, String isInactiveInclude,
			String isFor) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchUserDealerList invoked.." + userCode);
		}
		Query query = null;
		List<UserDealerResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_getDealersUnderUser] :userCode, :isInactiveInclude, :isFor";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("isInactiveInclude", isInactiveInclude);
			query.setParameter("isFor", isFor);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<UserDealerResponseModel>();
				UserDealerResponseModel branchResponseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					branchResponseModel = new UserDealerResponseModel();
					branchResponseModel.setDealerId((BigInteger) row.get("DEALER_ID"));
					branchResponseModel.setDealerCode((String) row.get("DEALER_CODE"));
					branchResponseModel.setDealerName((String) row.get("DEALER_NAME"));
					branchResponseModel.setDealerLocation((String) row.get("DEALER_LOCATION"));
					responseModelList.add(branchResponseModel);
				}
			}

		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}

	public List<UserDealerResponseModel> fetchUserDealerList(String userCode, UserDealerRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchUserDealerList invoked.." + userCode);
		}
		Session session = null;
		List<UserDealerResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchUserDealerList(session, userCode, requestModel.getIncludeInactive(),
					requestModel.getIsFor());

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

}
