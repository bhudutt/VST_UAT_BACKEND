/**
 * 
 */
package com.hitech.dms.web.dao.permission.impl;

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
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.dao.permission.ValidatePermissionDao;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ValidatePermissionDaoImpl implements ValidatePermissionDao {
	private static final Logger logger = LoggerFactory.getLogger(ValidatePermissionDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public boolean validateMenuPermission(String userCode, String code) {
		if (logger.isDebugEnabled()) {
			logger.debug("validateMenuPermission invoked.." + code);
		}
		Session session = null;
		boolean isAccess = false;
		String sqlQuery = "select * from [UFN_checkPermission](:userCode, :code)";
		try {
			session = sessionFactory.openSession();
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("code", code);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					String status = (String) row.get("showValue");
					if (status != null && status.equalsIgnoreCase("true")) {
						isAccess = true;
					}
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
		return isAccess;
	}
}
