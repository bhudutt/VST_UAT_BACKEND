/**
 * 
 */
package com.hitech.dms.web.dao.inv.calculation;

import java.math.BigDecimal;
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

import com.hitech.dms.web.model.inv.calculatAmnt.request.CaltemAmntForInvoiceRequestModel;
import com.hitech.dms.web.model.inv.calculatAmnt.response.CaltemAmntForInvoiceResponseModel;;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class CaltemAmntForInvoiceDaoImpl implements CaltemAmntForInvoiceDao {
	private static final Logger logger = LoggerFactory.getLogger(CaltemAmntForInvoiceDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public CaltemAmntForInvoiceResponseModel calculateAmount(String userCode,
			CaltemAmntForInvoiceRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("calculateAmount invoked.." + userCode);
		}
		logger.info(requestModel.toString());
		Session session = null;
		Query query = null;
		CaltemAmntForInvoiceResponseModel responseModel = null;
		String sqlQuery = "exec [SP_SA_INV_CALCULATEItemAmnt] :userCode, :dealerId, :branchId, :pcId, :toDealerId, :poHdrId, :poDtlId, :dcId, "
				+ ":machineDcDtlId, :dcItemId, :rate, :qty, :discount, :invoiceTypeId ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("toDealerId", requestModel.getToDealerId());
			query.setParameter("poHdrId", requestModel.getPoHdrId());
			query.setParameter("poDtlId", requestModel.getPoDtlId());
			query.setParameter("dcId", requestModel.getDcId());
			query.setParameter("machineDcDtlId", requestModel.getMachineDcDtlId());
			query.setParameter("dcItemId", requestModel.getDcItemId());
			query.setParameter("qty", requestModel.getQty());
			query.setParameter("rate", requestModel.getRate());
			query.setParameter("discount", requestModel.getDiscount());
			query.setParameter("invoiceTypeId", requestModel.getInvoiceTypeId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				logger.info(data.toString());
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new CaltemAmntForInvoiceResponseModel();
					responseModel.setBasicPrice((BigDecimal) row.get("BasicPrice"));
					responseModel.setCgst_per((BigDecimal) row.get("Cgst_per"));
					responseModel.setSgst_per((BigDecimal) row.get("Sgst_per"));
					responseModel.setIgst_per((BigDecimal) row.get("Igst_per"));
					responseModel.setCgst_amnt((BigDecimal) row.get("Cgst_amount"));
					responseModel.setSgst_amnt((BigDecimal) row.get("Sgst_amount"));
					responseModel.setIgst_amnt((BigDecimal) row.get("Igst_amount"));
					responseModel.setTotal_gst_per((BigDecimal) row.get("Total_gst_per"));
					responseModel.setTotal_gst_amnt((BigDecimal) row.get("Total_gst_amount"));
					responseModel.setAssessableAmnt((BigDecimal) row.get("AssessableAmnt"));
					responseModel.setTotalAmnt((BigDecimal) row.get("TotalAmnt"));

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
		return responseModel;
	}
}
