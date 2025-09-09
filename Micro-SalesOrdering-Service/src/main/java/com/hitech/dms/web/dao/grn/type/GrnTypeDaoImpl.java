/**
 * 
 */
package com.hitech.dms.web.dao.grn.type;

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

import com.hitech.dms.web.model.grn.type.response.GrnTypeResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class GrnTypeDaoImpl implements GrnTypeDao {
	private static final Logger logger = LoggerFactory.getLogger(GrnTypeDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<GrnTypeResponseModel> fetchGrnTypeList(String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchGrnTypeList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<GrnTypeResponseModel> responseModelList = null;
		String sqlQuery = "Select * from SA_MST_MACHINE_GRN_TYPE (nolock) where active_status = 'Y' ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<GrnTypeResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					GrnTypeResponseModel responseModel = new GrnTypeResponseModel();
					responseModel.setGrnTypeId((Integer) row.get("GRN_type_id"));
					responseModel.setGrnTypeCode((String) row.get("grn_type_code"));
					responseModel.setGrnType((String) row.get("grn_type"));
					
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
