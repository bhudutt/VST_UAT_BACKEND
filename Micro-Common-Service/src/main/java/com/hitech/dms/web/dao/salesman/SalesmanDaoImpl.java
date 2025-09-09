/**
 * 
 */
package com.hitech.dms.web.dao.salesman;

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

import com.hitech.dms.web.model.salesman.request.SalesmanListFormModel;
import com.hitech.dms.web.model.salesman.response.SalesmanListModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class SalesmanDaoImpl implements SalesmanDao {
	private static final Logger logger = LoggerFactory.getLogger(SalesmanDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<SalesmanListModel> fetchSalesmanList(String userCode, String dealerOrBranch, Long dealerOrBranchID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchSalesmanList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<SalesmanListModel> salesmanListModelList = null;
		String sqlQuery = "exec [SP_ENQ_getSalesmanList] :userCode, :dealerOrBranch, :dealerOrBranchID";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerOrBranch", dealerOrBranch);
			query.setParameter("dealerOrBranchID", dealerOrBranchID);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				salesmanListModelList = new ArrayList<SalesmanListModel>();
				SalesmanListModel salesmanModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					salesmanModel = new SalesmanListModel();
					salesmanModel.setSalesmanID((BigInteger) row.get("salesmanID"));
					salesmanModel.setSalesmanName((String) row.get("salesmanName"));
					salesmanListModelList.add(salesmanModel);
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
		return salesmanListModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<SalesmanListModel> fetchSalesmanList(SalesmanListFormModel salesmanListFormModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchSalesmanList invoked.." + salesmanListFormModel.toString());
		}
		Session session = null;
		Query query = null;
		List<SalesmanListModel> salesmanListModelList = null;
		String sqlQuery = "exec [SP_ENQ_getSalesmanList] :userCode, :dealerOrBranch, :dealerOrBranchID";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", salesmanListFormModel.getUserCode());
			query.setParameter("dealerOrBranch", salesmanListFormModel.getDealerOrBranch());
			query.setParameter("dealerOrBranchID", salesmanListFormModel.getDealerOrBranchID());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				salesmanListModelList = new ArrayList<SalesmanListModel>();
				SalesmanListModel salesmanModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					salesmanModel = new SalesmanListModel();
					salesmanModel.setSalesmanID((BigInteger) row.get("salesmanID"));
					salesmanModel.setSalesmanName((String) row.get("salesmanName"));
					salesmanListModelList.add(salesmanModel);
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
		return salesmanListModelList;
	}
}
