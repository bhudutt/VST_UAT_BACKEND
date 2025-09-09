/**
 * 
 */
package com.hitech.dms.web.dao.inv.autolist;

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

import com.hitech.dms.web.model.inv.autolist.request.PoInvoiceAutoListRequestModel;
import com.hitech.dms.web.model.inv.autolist.response.PoInvoiceAutoListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class PoInvoiceAutoListDaoImpl implements PoInvoiceAutoListDao {
	private static final Logger logger = LoggerFactory.getLogger(PoInvoiceAutoListDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<PoInvoiceAutoListResponseModel> fetchPoListForInvoice(String userCode,
			PoInvoiceAutoListRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPoListForInvoice invoked.." + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<PoInvoiceAutoListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SA_INV_PO_LIST] :userCode, :dealerId, :branchId, :pcId, :toDealerId, :searchText, :isFor, :invoiceTypeId ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("toDealerId", requestModel.getToDealerId());
			query.setParameter("searchText", requestModel.getSearchText());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setParameter("invoiceTypeId", requestModel.getInvoiceTypeId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<PoInvoiceAutoListResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					PoInvoiceAutoListResponseModel responseModel = new PoInvoiceAutoListResponseModel();
					responseModel.setPoHdrId((BigInteger) row.get("PoHdrId"));
					responseModel.setPoNumber((String) row.get("PoNumber"));
					responseModel.setPoDate((String) row.get("PoDate"));
					responseModel.setDisplayValue((String) row.get("DisplayValue"));
					responseModel.setDealerId((BigInteger) row.get("DealerId"));
					responseModel.setPoStatus((String) row.get("PoStatus"));
					responseModel.setPoToDealerId((BigInteger) row.get("PoToDealerId"));

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
