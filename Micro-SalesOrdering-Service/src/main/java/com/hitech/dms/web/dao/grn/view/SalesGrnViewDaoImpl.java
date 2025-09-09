/**
 * 
 */
package com.hitech.dms.web.dao.grn.view;

import java.math.BigDecimal;
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
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.grn.view.request.SalesGrnViewRequestModel;
import com.hitech.dms.web.model.grn.view.response.SalesGrnViewItemDtlResponseModel;
import com.hitech.dms.web.model.grn.view.response.SalesGrnViewMachineDtlResponseModel;
import com.hitech.dms.web.model.grn.view.response.SalesGrnViewResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class SalesGrnViewDaoImpl implements SalesGrnViewDao {
	private static final Logger logger = LoggerFactory.getLogger(SalesGrnViewDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("deprecation")
	public SalesGrnViewResponseModel fetchSalesGrnDetail(String userCode, SalesGrnViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchSalesGrnDetail invoked.." + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		SalesGrnViewResponseModel responseModel = null;
		List<SalesGrnViewMachineDtlResponseModel> salesMachineGrnDtlList = null;
		List<SalesGrnViewItemDtlResponseModel> salesMachineGrnImplDtlList = null;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchSalesGrnDetail(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new SalesGrnViewResponseModel();
					responseModel.setPcId((Integer) row.get("PcId"));
					responseModel.setPcDesc((String) row.get("PcDesc"));
					responseModel.setDealerId((BigInteger) row.get("DealerId"));
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setDealerName((String) row.get("DealerName"));
					responseModel.setGrnNumber((String) row.get("GrnNumber"));
					responseModel.setGrnDate((String) row.get("GrnDate"));
					
					responseModel.setInvoiceId((BigInteger) row.get("InvoiceId"));
					responseModel.setInvoiceNumber((String) row.get("InvoiceNumber"));
					responseModel.setInvoiceDate((String) row.get("InvoiceDate"));
					responseModel.setGrnTypeId((Integer) row.get("GrnTypeId"));
					responseModel.setGrnType((String) row.get("GrnType"));
					responseModel.setPartyCode((String) row.get("PartyCode"));
					responseModel.setPartyName((String) row.get("PartyName"));
					responseModel.setPlantCode((String) row.get("plant_code"));
					responseModel.setTransporterName((String) row.get("TransporterName"));
					responseModel.setTransporterVehicleNo((String) row.get("TransporterVehicleNo"));
					responseModel.setDriverMobileNo((String) row.get("DriverMobileNo"));
					responseModel.setDriverName((String) row.get("DriverName"));
					responseModel.setGrossTotalValue((BigDecimal) row.get("GrossTotalValue"));
				}
				requestModel.setFlag(2);
				data = fetchSalesGrnDetail(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					salesMachineGrnDtlList = new ArrayList<SalesGrnViewMachineDtlResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						SalesGrnViewMachineDtlResponseModel dtlForMachineResponseModel = new SalesGrnViewMachineDtlResponseModel();
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
				data = fetchSalesGrnDetail(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					salesMachineGrnImplDtlList = new ArrayList<SalesGrnViewItemDtlResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						SalesGrnViewItemDtlResponseModel dtlForItemResponseModel = new SalesGrnViewItemDtlResponseModel();
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
	public List fetchSalesGrnDetail(Session session, String userCode, SalesGrnViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchSalesGrnDetail invoked..");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_GRN_Dtl] :userCode, :grnId, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("grnId", requestModel.getGrnId());
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
