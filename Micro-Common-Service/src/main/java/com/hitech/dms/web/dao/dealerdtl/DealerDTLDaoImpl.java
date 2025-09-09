/**
 * 
 */
package com.hitech.dms.web.dao.dealerdtl;

import java.math.BigInteger;
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

import com.hitech.dms.web.model.dealerdtl.request.DealerDTLRequestModel;
import com.hitech.dms.web.model.dealerdtl.response.DealerDTLResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class DealerDTLDaoImpl implements DealerDTLDao {
	private static final Logger logger = LoggerFactory.getLogger(DealerDTLDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public DealerDTLResponseModel fetchDealerDTLByDealerId(String userCode, DealerDTLRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDealerDTL invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		DealerDTLResponseModel responseModel = null;
		String sqlQuery = "exec [SP_CM_GETDEALERDTLBYID] :userCode, :dealerId, :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new DealerDTLResponseModel();
					responseModel.setDealerId((BigInteger) row.get("parent_dealer_id"));
					responseModel.setDealerCode((String) row.get("ParentDealerCode"));
					responseModel.setDealerName((String) row.get("ParentDealerName"));
					responseModel.setDealerLocation((String) row.get("ParentDealerLocation"));
					responseModel.setDealerType((String) row.get("dealer_type_desc"));
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
