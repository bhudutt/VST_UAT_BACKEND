/**
 * 
 */
package com.hitech.dms.web.dao.activity.permission;

import java.util.HashMap;
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

import com.hitech.dms.web.model.activity.permission.request.ActivityPermissionRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivityPermissionCheckDaoImpl implements ActivityPermissionCheckDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityPermissionCheckDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> checkActivityPermissions(String userCode, ActivityPermissionRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("checkActivityPermissions invoked..");
		}
		Session session = null;
		Query query = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		String sqlQuery = "exec [SP_SACT_VALIDATE_APPROVAL_PERMISSIONS] :userCode, :id, :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("id", requestModel.getId());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					mapData.put("IsApprovePermssion", (String) row.get("IsApprovePermssion"));
					mapData.put("IsRejectPermssion", (String) row.get("IsRejectPermssion"));
					mapData.put("approval_level_seq",(Integer) row.get("approval_seq"));
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mapData;
	}
}
