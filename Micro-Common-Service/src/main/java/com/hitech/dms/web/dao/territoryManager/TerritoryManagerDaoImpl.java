/**
 * 
 */
package com.hitech.dms.web.dao.territoryManager;

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

import com.hitech.dms.web.model.territoryManager.TerritoryManagerListModel;

@Repository
public class TerritoryManagerDaoImpl implements TerritoryManagerDao {
	private static final Logger logger = LoggerFactory.getLogger(TerritoryManagerDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<TerritoryManagerListModel> fetchTerritoryManagerList(String userCode, int territoryId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchTerritoryManagerList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<TerritoryManagerListModel> territoryManagerListModelList = null;
		String sqlQuery = "exec [SP_GET_Territory_Manager] :userCode, :territoryId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("territoryId", territoryId);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				territoryManagerListModelList = new ArrayList<TerritoryManagerListModel>();
				TerritoryManagerListModel territoryManagerListModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					territoryManagerListModel = new TerritoryManagerListModel();
					territoryManagerListModel.setTerritoryManagerId((BigInteger) row.get("ho_usr_id"));
					territoryManagerListModel.setTerritoryManagerName((String) row.get("vst_executive"));
					territoryManagerListModelList.add(territoryManagerListModel);
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
		return territoryManagerListModelList;
	}

}
