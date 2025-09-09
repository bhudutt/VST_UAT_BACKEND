/**
 * 
 */
package com.hitech.dms.web.dao.grn.invoice.dtl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.grn.invoice.dtl.request.InvoicesForGrnDtlRequestModel;
import com.hitech.dms.web.model.grn.invoice.dtl.response.InvoicesForGrnDtlForItemResponseModel;
import com.hitech.dms.web.model.grn.invoice.dtl.response.InvoicesForGrnDtlForMachineResponseModel;
import com.hitech.dms.web.model.grn.invoice.dtl.response.InvoicesForGrnDtlResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class InvoicesForGrnDtlDaoImpl implements InvoicesForGrnDtlDao {
	private static final Logger logger = LoggerFactory.getLogger(InvoicesForGrnDtlDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public InvoicesForGrnDtlResponseModel fetchInvoiceDtlForGrn(String userCode,
			InvoicesForGrnDtlRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchInvoiceDtlForGrn invoked.." + requestModel.toString());
		}
		Session session = null;
		InvoicesForGrnDtlResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchInvoiceDtlForGrn(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new InvoicesForGrnDtlResponseModel();
					responseModel.setInvoiceId((BigInteger) row.get("InvoiceId"));
					responseModel.setInvoiceNo((String) row.get("InvoiceNumber"));
					responseModel.setInvoiceType((String) row.get("InvoiceType"));
					responseModel.setInvoiceDate((String) row.get("InvoiceDate"));
					responseModel.setDisplayValue((String) row.get("DisplayValue"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setPlantCode((String) row.get("plant_code"));
					
				}
				requestModel.setFlag(2);
				data = fetchInvoiceDtlForGrn(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<InvoicesForGrnDtlForMachineResponseModel> salesMachineGrnDtlList = new ArrayList<InvoicesForGrnDtlForMachineResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						InvoicesForGrnDtlForMachineResponseModel dtlForMachineResponseModel = new InvoicesForGrnDtlForMachineResponseModel();
						dtlForMachineResponseModel.setGrnDtlId((BigInteger) row.get("GrnDtlId"));
						dtlForMachineResponseModel.setMachineItemId((BigInteger) row.get("MachineItemId"));
						dtlForMachineResponseModel.setVinId((BigInteger) row.get("VinId"));
						dtlForMachineResponseModel.setItemNo((String) row.get("ItemNo"));
						dtlForMachineResponseModel.setItemDesc((String) row.get("ItemDescription"));
						dtlForMachineResponseModel.setChassisNo((String) row.get("ChassisNo"));
						dtlForMachineResponseModel.setVinNo((String) row.get("VinNo"));
						dtlForMachineResponseModel.setEngineNo((String) row.get("EngineNo"));
						dtlForMachineResponseModel.setInvoiceQty((Integer) row.get("InvoiceQty"));
						dtlForMachineResponseModel.setUnitPrice((BigDecimal) row.get("UnitPrice"));
						dtlForMachineResponseModel.setDiscountAmnt((BigDecimal) row.get("DiscountAmnt"));
						dtlForMachineResponseModel.setGstAmnt((BigDecimal) row.get("GstAmnt"));
						dtlForMachineResponseModel.setAssessableAmnt((BigDecimal) row.get("AssessableAmnt"));
						dtlForMachineResponseModel.setTotalAmnt((BigDecimal) row.get("TotalAmnt"));
						dtlForMachineResponseModel.setReceiptQty((Integer) row.get("ReceiptQty"));
						dtlForMachineResponseModel.setRemarks((String) row.get("Remarks"));
						dtlForMachineResponseModel.setPlantCode((String) row.get("plant_code"));

						salesMachineGrnDtlList.add(dtlForMachineResponseModel);
					}

					responseModel.setSalesMachineGrnDtlList(salesMachineGrnDtlList);
				}
				requestModel.setFlag(3);
				data = fetchInvoiceDtlForGrn(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<InvoicesForGrnDtlForItemResponseModel> salesMachineGrnImplDtlList = new ArrayList<InvoicesForGrnDtlForItemResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						InvoicesForGrnDtlForItemResponseModel dtlForItemResponseModel = new InvoicesForGrnDtlForItemResponseModel();
						dtlForItemResponseModel.setGrnItemDtlId((BigInteger) row.get("GrnItemDtlId"));
						dtlForItemResponseModel.setMachineItemId((BigInteger) row.get("MachineItemId"));
						dtlForItemResponseModel.setInvoiceQty((Integer) row.get("InvoiceQty"));
						dtlForItemResponseModel.setItemNo((String) row.get("ItemNo"));  //
						dtlForItemResponseModel.setItemDesc((String) row.get("ItemDescription"));
						dtlForItemResponseModel.setChassisNo((String) row.get("ChassisNo"));
						dtlForItemResponseModel.setVinNo((String) row.get("VinNo"));
						dtlForItemResponseModel.setEngineNo((String) row.get("EngineNo"));
						dtlForItemResponseModel.setUnitPrice((BigDecimal) row.get("UnitPrice"));
						dtlForItemResponseModel.setDiscountAmnt((BigDecimal) row.get("DiscountAmnt"));
						dtlForItemResponseModel.setGstAmnt((BigDecimal) row.get("GstAmnt"));
						dtlForItemResponseModel.setAssessableAmnt((BigDecimal) row.get("AssessableAmnt"));
						dtlForItemResponseModel.setTotalAmnt((BigDecimal) row.get("TotalAmnt"));
						dtlForItemResponseModel.setReceiptQty((Integer) row.get("ReceiptQty"));
						dtlForItemResponseModel.setRemarks((String) row.get("Remarks"));
						dtlForItemResponseModel.setPlantCode((String) row.get("plant_code"));

						salesMachineGrnImplDtlList.add(dtlForItemResponseModel);
					}
					responseModel.setSalesMachineGrnImplDtlList(salesMachineGrnImplDtlList);
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List fetchInvoiceDtlForGrn(Session session, String userCode, InvoicesForGrnDtlRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchInvoiceDtlForGrn invoked..");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "[SP_SA_GRN_Invoice_Dtl] :userCode, :pcId, :dealerId, :grnTypeId, :invoiceId, :invoiceNo, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("grnTypeId", requestModel.getGrnTypeId());
			query.setParameter("invoiceId", requestModel.getInvoiceId());
			query.setParameter("invoiceNo", requestModel.getInvoiceNo());
			query.setParameter("flag", requestModel.getFlag());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			data = query.list();
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return data;
	}
}
