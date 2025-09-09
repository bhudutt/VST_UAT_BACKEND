/**
 * 
 */
package com.hitech.dms.web.dao.grn.invoice.autosearch;

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

import com.hitech.dms.web.model.grn.invoice.autosearch.request.InvoicesForGrnRequestModel;
import com.hitech.dms.web.model.grn.invoice.autosearch.response.InvoicesForGrnResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class InvoicesForGrnDaoImpl implements InvoicesForGrnDao {
	private static final Logger logger = LoggerFactory.getLogger(InvoicesForGrnDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<InvoicesForGrnResponseModel> fetchInvoiceListForGrn(String userCode,
			InvoicesForGrnRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchInvoiceListForGrn invoked.." + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<InvoicesForGrnResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SA_GRN_Invoices_AutoSearch] :userCode, :pcId, :dealerId, :includeInactive, :grnTypeId, :grnNo";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("includeInactive", requestModel.getIncludeInactive());
			query.setParameter("grnTypeId", requestModel.getGrnTypeId());
			query.setParameter("grnNo", requestModel.getGrnNumber());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<InvoicesForGrnResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					InvoicesForGrnResponseModel responseModel = new InvoicesForGrnResponseModel();
					responseModel.setInvoiceId((BigInteger) row.get("InvoiceId"));
					responseModel.setInvoiceNo((String) row.get("InvoiceNumber"));
					responseModel.setInvoiceType((String) row.get("InvoiceType"));
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
