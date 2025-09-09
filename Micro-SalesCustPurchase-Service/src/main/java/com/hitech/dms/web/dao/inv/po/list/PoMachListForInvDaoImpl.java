/**
 * 
 */
package com.hitech.dms.web.dao.inv.po.list;

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

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.inv.po.list.request.PoMachListForInvRequestModel;
import com.hitech.dms.web.model.inv.po.list.response.PoMachListForInvResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class PoMachListForInvDaoImpl implements PoMachListForInvDao {
	private static final Logger logger = LoggerFactory.getLogger(PoMachListForInvDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public List<PoMachListForInvResponseModel> fetchPoMachineDtlList(String userCode,
			PoMachListForInvRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPoMachineDtlList invoked.." + requestModel.toString());
		}
		String msg = null;
		Session session = null;
		List<PoMachListForInvResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			List data = fetchPoMachineDtlList(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<PoMachListForInvResponseModel>();
				PoMachListForInvResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PoMachListForInvResponseModel();

					msg = (String) row.get("Message");
					if (msg != null) {
						responseModel.setMsg(msg);
						responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					} else {
					responseModel.setMachineItemId((BigInteger) row.get("MachineItemId"));
					responseModel.setMachineInventoryId((BigInteger) row.get("MachineInventoryId"));
					responseModel.setPoDtlId((BigInteger) row.get("PoDtlId"));
					responseModel.setItemNo((String) row.get("ItemNo"));
					responseModel.setItemDesc((String) row.get("ItemDesc"));
					responseModel.setModel((String) row.get("Model"));
					responseModel.setChassisNo((String) row.get("ChassisNo"));
					responseModel.setVinId((BigInteger) row.get("VinId"));
					responseModel.setVinNo((String) row.get("VinNo"));
					responseModel.setEngineNo((String) row.get("EngineNo"));
					responseModel.setHsnCode((String) row.get("HsnCode"));
					responseModel.setQty((Integer) row.get("Qty"));
					responseModel.setInvoiceQty((Integer) row.get("InvoiceQty"));
					responseModel.setPendingQty((Integer) row.get("PendingQty"));
					responseModel.setUnitPrice((BigDecimal) row.get("UnitPrice"));
					responseModel.setDiscountAmnt((BigDecimal) row.get("DiscountAmnt"));
					responseModel.setIgst_per((BigDecimal) row.get("Igst_per"));
					responseModel.setIgst_amount((BigDecimal) row.get("Igst_amount"));
					responseModel.setCgst_per((BigDecimal) row.get("Cgst_per"));
					responseModel.setCgst_amount((BigDecimal) row.get("Cgst_amount"));
					responseModel.setSgst_per((BigDecimal) row.get("Sgst_per"));
					responseModel.setSgst_amount((BigDecimal) row.get("Sgst_amount"));
					responseModel.setTotal_gst_per((BigDecimal) row.get("Total_gst_per"));
					responseModel.setTotal_gst_amount((BigDecimal) row.get("Total_gst_amount"));
					responseModel.setAssessableAmnt((BigDecimal) row.get("AssessableAmnt"));
					responseModel.setTotalAmnt((BigDecimal) row.get("TotalAmnt"));
					responseModel.setRemarks((String) row.get("Remarks"));
					responseModel.setTotalInvoiceQuantity((Integer) row.get("Quantity"));
				}
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List fetchPoMachineDtlList(Session session, String userCode, PoMachListForInvRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPoMachineDtlList invoked...");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_INV_PO_MACHINE_LIST] :userCode, :dealerId, :branchId, :pcId, :toDealerId, :poId, :poNumber";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("toDealerId", requestModel.getToDealerId());
			query.setParameter("poId", requestModel.getPoHdrId());
			query.setParameter("poNumber", requestModel.getPoNumber());
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
