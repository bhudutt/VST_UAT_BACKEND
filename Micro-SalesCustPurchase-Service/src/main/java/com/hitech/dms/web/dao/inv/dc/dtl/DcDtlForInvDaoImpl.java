/**
 * 
 */
package com.hitech.dms.web.dao.inv.dc.dtl;

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

import com.hitech.dms.web.model.inv.dc.dtl.request.DcDtlForInvRequestModel;
import com.hitech.dms.web.model.inv.dc.dtl.response.DcDtlForInvResponseModel;
import com.hitech.dms.web.model.inv.dc.dtl.response.DcItemDtlForInvResponseModel;
import com.hitech.dms.web.model.inv.dc.dtl.response.DcMachDtlForInvResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class DcDtlForInvDaoImpl implements DcDtlForInvDao {
	private static final Logger logger = LoggerFactory.getLogger(DcDtlForInvDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public DcDtlForInvResponseModel fetchDcDetailForInvoice(String userCode, DcDtlForInvRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDcDetailForInvoice invoked.." + requestModel.toString());
		}
		logger.info("fetchDcDetailForInvoice : " + requestModel.toString());
		Session session = null;
		DcDtlForInvResponseModel responseModel = null;
		List<DcMachDtlForInvResponseModel> dcMachineList;
		List<DcItemDtlForInvResponseModel> dcItemList;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchDcDetailForInvoice(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				responseModel = new DcDtlForInvResponseModel();
				responseModel.setDcId(requestModel.getDcId());
				dcMachineList = new ArrayList<DcMachDtlForInvResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					DcMachDtlForInvResponseModel machDtlForInvResponseModel = new DcMachDtlForInvResponseModel();
					machDtlForInvResponseModel.setDcId((BigInteger) row.get("DcId"));
					machDtlForInvResponseModel.setMachineDcDtlId((BigInteger) row.get("MachineDcDtlId"));
					machDtlForInvResponseModel.setMachineInventoryId((BigInteger) row.get("MachineInventoryId"));
					machDtlForInvResponseModel.setMachineItemId((BigInteger) row.get("MachineItemId"));
					machDtlForInvResponseModel.setItemNo((String) row.get("ItemNo"));
					machDtlForInvResponseModel.setItemDesc((String) row.get("ItemDesc"));
					machDtlForInvResponseModel.setModel((String) row.get("Model"));
					machDtlForInvResponseModel.setChassisNo((String) row.get("ChassisNo"));
					machDtlForInvResponseModel.setVinId((BigInteger) row.get("VinId"));
					machDtlForInvResponseModel.setVinNo((String) row.get("VinNo"));
					machDtlForInvResponseModel.setEngineNo((String) row.get("EngineNo"));
					machDtlForInvResponseModel.setHsnCode((String) row.get("HsnCode"));
					machDtlForInvResponseModel.setQty((Integer) row.get("Qty"));
					machDtlForInvResponseModel.setUnitPrice((BigDecimal) row.get("UnitPrice"));
					machDtlForInvResponseModel.setDiscountAmnt((BigDecimal) row.get("DiscountAmnt"));
					machDtlForInvResponseModel.setIgst_per((BigDecimal) row.get("Igst_per"));
					machDtlForInvResponseModel.setIgst_amount((BigDecimal) row.get("Igst_amount"));
					machDtlForInvResponseModel.setCgst_per((BigDecimal) row.get("Cgst_per"));
					machDtlForInvResponseModel.setCgst_amount((BigDecimal) row.get("Cgst_amount"));
					machDtlForInvResponseModel.setSgst_per((BigDecimal) row.get("Sgst_per"));
					machDtlForInvResponseModel.setSgst_amount((BigDecimal) row.get("Sgst_amount"));
					machDtlForInvResponseModel.setTotal_gst_per((BigDecimal) row.get("Total_gst_per"));
					machDtlForInvResponseModel.setTotal_gst_amount((BigDecimal) row.get("Total_gst_amount"));
					machDtlForInvResponseModel.setAssessableAmnt((BigDecimal) row.get("AssessableAmnt"));
					machDtlForInvResponseModel.setTotalAmnt((BigDecimal) row.get("TotalAmnt"));
					machDtlForInvResponseModel.setRemarks((String) row.get("Remarks"));

					dcMachineList.add(machDtlForInvResponseModel);
				}
				responseModel.setDcMachineList(dcMachineList);
				
				requestModel.setFlag(2);
				data = fetchDcDetailForInvoice(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					dcItemList = new ArrayList<DcItemDtlForInvResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						DcItemDtlForInvResponseModel itemDtlForInvResposeModel = new DcItemDtlForInvResponseModel();
						itemDtlForInvResposeModel.setDcId((BigInteger) row.get("DcId"));
						itemDtlForInvResposeModel.setDcItemId((BigInteger) row.get("DcItemId"));
						itemDtlForInvResposeModel.setMachineItemId((BigInteger) row.get("MachineItemId"));
						itemDtlForInvResposeModel.setItemNo((String) row.get("ItemNo"));
						itemDtlForInvResposeModel.setItemDesc((String) row.get("ItemDesc"));
						itemDtlForInvResposeModel.setChassisNo((String) row.get("ChassisNo"));
						itemDtlForInvResposeModel.setVinNo((String) row.get("VinNo"));
						itemDtlForInvResposeModel.setEngineNo((String) row.get("EngineNo"));
						itemDtlForInvResposeModel.setHsnCode((String) row.get("HsnCode"));
						itemDtlForInvResposeModel.setQty((Integer) row.get("Qty"));
						itemDtlForInvResposeModel.setUnitPrice((BigDecimal) row.get("UnitPrice"));
						itemDtlForInvResposeModel.setDiscountAmnt((BigDecimal) row.get("DiscountAmnt"));
						itemDtlForInvResposeModel.setIgst_per((BigDecimal) row.get("Igst_per"));
						itemDtlForInvResposeModel.setIgst_amount((BigDecimal) row.get("Igst_amount"));
						itemDtlForInvResposeModel.setCgst_per((BigDecimal) row.get("Cgst_per"));
						itemDtlForInvResposeModel.setCgst_amount((BigDecimal) row.get("Cgst_amount"));
						itemDtlForInvResposeModel.setSgst_per((BigDecimal) row.get("Sgst_per"));
						itemDtlForInvResposeModel.setSgst_amount((BigDecimal) row.get("Sgst_amount"));
						itemDtlForInvResposeModel.setTotal_gst_per((BigDecimal) row.get("Total_gst_per"));
						itemDtlForInvResposeModel.setTotal_gst_amount((BigDecimal) row.get("Total_gst_amount"));
						itemDtlForInvResposeModel.setAssessableAmnt((BigDecimal) row.get("AssessableAmnt"));
						itemDtlForInvResposeModel.setTotalAmnt((BigDecimal) row.get("TotalAmnt"));
						itemDtlForInvResposeModel.setRemarks((String) row.get("Remarks"));

						dcItemList.add(itemDtlForInvResposeModel);
					}
					responseModel.setDcItemList(dcItemList);

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
	public List fetchDcDetailForInvoice(Session session, String userCode, DcDtlForInvRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDcDetailForInvoice invoked...");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_INV_DC_DTL] :userCode, :dealerId, :branchId, :pcId, :customerId, :dcId, :invoiceTypeId, :isFor, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("customerId", requestModel.getCustomerId());
			query.setParameter("dcId", requestModel.getDcId());
			query.setParameter("invoiceTypeId", requestModel.getInvoiceTypeId());
			query.setParameter("isFor", requestModel.getIsFor());
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
