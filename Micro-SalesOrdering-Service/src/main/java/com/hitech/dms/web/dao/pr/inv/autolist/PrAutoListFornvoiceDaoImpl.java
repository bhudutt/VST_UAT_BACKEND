/**
 * 
 */
package com.hitech.dms.web.dao.pr.inv.autolist;

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

import com.hitech.dms.web.model.pr.inv.request.PrAutoListFornvoiceRequestModel;
import com.hitech.dms.web.model.pr.inv.response.PrAutoListFornvoiceResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class PrAutoListFornvoiceDaoImpl implements PrAutoListFornvoiceDao {
	private static final Logger logger = LoggerFactory.getLogger(PrAutoListFornvoiceDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<PrAutoListFornvoiceResponseModel> fetchPurchaseReturnAutoList(String userCode,
			PrAutoListFornvoiceRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPurchaseReturnAutoList invoked.." + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<PrAutoListFornvoiceResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SA_GRN_PUR_RET_INV_AutoList] :userCode, :pcId, :dealerId, :includeInactive, :prNo";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("includeInactive", requestModel.getIncludeInactive());
			query.setParameter("prNo", requestModel.getPurchaseReturnNumber());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<PrAutoListFornvoiceResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					PrAutoListFornvoiceResponseModel responseModel = new PrAutoListFornvoiceResponseModel();
					responseModel.setPurchaseReturnId((BigInteger) row.get("PurchaseReturnId"));
					responseModel.setPurchaseReturnNumber((String) row.get("PurchaseReturnNo"));
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
