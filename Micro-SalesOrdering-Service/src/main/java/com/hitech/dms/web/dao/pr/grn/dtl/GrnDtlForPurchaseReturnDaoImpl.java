/**
 * 
 */
package com.hitech.dms.web.dao.pr.grn.dtl;

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

import com.hitech.dms.web.model.pr.grn.dtl.request.GrnDtlForPurchaseReturnRequestModel;
import com.hitech.dms.web.model.pr.grn.dtl.response.GrnDtlForPurchaseReturnResponseModel;
import com.hitech.dms.web.model.pr.grn.dtl.response.GrnItemDtlForPurchaseReturnResponseModel;
import com.hitech.dms.web.model.pr.grn.dtl.response.GrnMachDtlForPurchaseReturnResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class GrnDtlForPurchaseReturnDaoImpl implements GrnDtlForPurchaseReturnDao {
	private static final Logger logger = LoggerFactory.getLogger(GrnDtlForPurchaseReturnDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public GrnDtlForPurchaseReturnResponseModel fetchGrnDtlForPurchaseReturn(String userCode,
			GrnDtlForPurchaseReturnRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchGrnDtlForPurchaseReturn invoked.." + requestModel.toString());
		}
		Session session = null;
		GrnDtlForPurchaseReturnResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchGrnDtlForPurchaseReturn(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new GrnDtlForPurchaseReturnResponseModel();
					responseModel.setPcId((Integer) row.get("PcId"));
					responseModel.setPcDesc((String) row.get("PcDesc"));
					responseModel.setDealerId((BigInteger) row.get("DealerId"));
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setDealerName((String) row.get("DealerName"));
					responseModel.setGrnNumber((String) row.get("GrnNumber"));
					responseModel.setGrnStatus((String) row.get("GrnStatus"));
					responseModel.setGrnDate((String) row.get("GrnDate"));
					responseModel.setInvoiceId((BigInteger) row.get("InvoiceId"));
					responseModel.setInvoiceNumber((String) row.get("InvoiceNumber"));
					responseModel.setInvoiceStatus((String) row.get("InvoiceStatus"));
					responseModel.setInvoiceDate((String) row.get("InvoiceDate"));
					responseModel.setGrnId((BigInteger) row.get("GrnId"));
					responseModel.setGrnTypeId((Integer) row.get("GrnTypeId"));
					responseModel.setGrnType((String) row.get("GrnType"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setTransporterName((String) row.get("TransporterName"));
					responseModel.setTransporterVehicleNo((String) row.get("TransporterVehicleNo"));
					responseModel.setDriverMobileNo((String) row.get("DriverMobileNo"));
					responseModel.setDriverName((String) row.get("DriverName"));
					responseModel.setGrossTotalValue((BigDecimal) row.get("GrossTotalValue"));
				}
				requestModel.setFlag(2);
				data = fetchGrnDtlForPurchaseReturn(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<GrnMachDtlForPurchaseReturnResponseModel> salesMachineGrnDtlList = new ArrayList<GrnMachDtlForPurchaseReturnResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						GrnMachDtlForPurchaseReturnResponseModel dtlForMachineResponseModel = new GrnMachDtlForPurchaseReturnResponseModel();
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

						salesMachineGrnDtlList.add(dtlForMachineResponseModel);
					}

					responseModel.setSalesMachineGrnDtlList(salesMachineGrnDtlList);
				}
				requestModel.setFlag(3);
				data = fetchGrnDtlForPurchaseReturn(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<GrnItemDtlForPurchaseReturnResponseModel> salesMachineGrnImplDtlList = new ArrayList<GrnItemDtlForPurchaseReturnResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						GrnItemDtlForPurchaseReturnResponseModel dtlForItemResponseModel = new GrnItemDtlForPurchaseReturnResponseModel();
						dtlForItemResponseModel.setGrnItemDtlId((BigInteger) row.get("GrnItemDtlId"));
						dtlForItemResponseModel.setMachineItemId((BigInteger) row.get("MachineItemId"));
						dtlForItemResponseModel.setInvoiceQty((Integer) row.get("InvoiceQty"));
						dtlForItemResponseModel.setItemNo((String) row.get("ItemNo"));
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
	public List fetchGrnDtlForPurchaseReturn(Session session, String userCode,
			GrnDtlForPurchaseReturnRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchGrnDtlForPurchaseReturn invoked..");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "[SP_SA_GRN_PUR_RET_GRN_DTL] :userCode, :pcId, :dealerId, :grnNo, :grnId, :flag, :includeInactive";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("grnNo", requestModel.getGrnNumber());
			query.setParameter("grnId", requestModel.getGrnId());
			query.setParameter("flag", requestModel.getFlag());
			query.setParameter("includeInactive", requestModel.getIncludeInactive());
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
