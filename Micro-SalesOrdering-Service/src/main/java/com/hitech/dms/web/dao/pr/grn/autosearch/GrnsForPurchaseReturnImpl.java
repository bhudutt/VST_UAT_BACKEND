/**
 * 
 */
package com.hitech.dms.web.dao.pr.grn.autosearch;

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

import com.hitech.dms.web.model.pr.grn.autosearch.request.GrnsForPurchaseReturnRequestModel;
import com.hitech.dms.web.model.pr.grn.autosearch.response.GrnsForPurchaseReturnResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class GrnsForPurchaseReturnImpl implements GrnsForPurchaseReturnDao {
	private static final Logger logger = LoggerFactory.getLogger(GrnsForPurchaseReturnImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<GrnsForPurchaseReturnResponseModel> fetchGrnListForPurchaseReturn(String userCode,
			GrnsForPurchaseReturnRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchGrnListForPurchaseReturn invoked.." + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<GrnsForPurchaseReturnResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SA_GRN_PUR_RET_GRNAUTOLIST] :userCode, :pcId, :dealerId, :includeInactive, :grnNo";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("includeInactive", requestModel.getIncludeInactive());
			query.setParameter("grnNo", requestModel.getGrnNumber());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<GrnsForPurchaseReturnResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					GrnsForPurchaseReturnResponseModel responseModel = new GrnsForPurchaseReturnResponseModel();
					responseModel.setGrnId((BigInteger) row.get("Id"));
					responseModel.setGrnNumber((String) row.get("GRNNumber"));
					responseModel.setGrnType((String) row.get("GrnType"));
					responseModel.setInvoiceNumber((String) row.get("InvoiceNo"));
					responseModel.setDisplayValue((String) row.get("DisplayValue"));

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
