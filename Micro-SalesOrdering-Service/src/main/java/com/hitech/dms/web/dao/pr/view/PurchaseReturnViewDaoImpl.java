/**
 * 
 */
package com.hitech.dms.web.dao.pr.view;

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

import com.hitech.dms.web.model.pr.view.request.PurchaseReturnViewRequestModel;
import com.hitech.dms.web.model.pr.view.response.PurchaseReturnItemDtlViewResponseModel;
import com.hitech.dms.web.model.pr.view.response.PurchaseReturnMachDtlViewResponseModel;
import com.hitech.dms.web.model.pr.view.response.PurchaseReturnViewAppResponseModel;
import com.hitech.dms.web.model.pr.view.response.PurchaseReturnViewResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class PurchaseReturnViewDaoImpl implements PurchaseReturnViewDao {
	private static final Logger logger = LoggerFactory.getLogger(PurchaseReturnViewDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public PurchaseReturnViewResponseModel fetchpurchaseReturnDtlView(String userCode,
			PurchaseReturnViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchpurchaseReturnDtlView invoked.." + requestModel.toString());
		}
		Session session = null;
		PurchaseReturnViewResponseModel responseModel = null;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchpurchaseReturnDtlView(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PurchaseReturnViewResponseModel();
					responseModel.setPcId((Integer) row.get("PcId"));
					responseModel.setPcDesc((String) row.get("PcDesc"));
					responseModel.setDealerId((BigInteger) row.get("DealerId"));
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setDealerName((String) row.get("DealerName"));
					responseModel.setPurchaseReturnId((BigInteger) row.get("PurchaseReturnId"));
					responseModel.setPurchaseReturnNumber((String) row.get("PurchaseReturnNumber"));
					responseModel.setPurchaseReturnDate((String) row.get("PurchaseReturnDate"));
					responseModel.setPurchaseReturnStatus((String) row.get("PurchaseReturnStatus"));
					responseModel.setGrnNumber((String) row.get("GrnNumber"));
					responseModel.setGrnId((BigInteger) row.get("GrnId"));
					responseModel.setGrnDate((String) row.get("GrnDate"));
					responseModel.setGrnStatus((String) row.get("GrnStatus"));
					responseModel.setInvoiceId((BigInteger) row.get("InvoiceId"));
					responseModel.setInvoiceNumber((String) row.get("InvoiceNumber"));
					responseModel.setInvoiceDate((String) row.get("InvoiceDate"));
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
				data = fetchpurchaseReturnDtlView(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<PurchaseReturnMachDtlViewResponseModel> salesMachineGrnDtlList = new ArrayList<PurchaseReturnMachDtlViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						PurchaseReturnMachDtlViewResponseModel dtlForMachineResponseModel = new PurchaseReturnMachDtlViewResponseModel();
						dtlForMachineResponseModel.setPurchaseReturnDtlId((BigInteger) row.get("PurchaseReturnDtlId"));
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
				data = fetchpurchaseReturnDtlView(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<PurchaseReturnItemDtlViewResponseModel> salesMachineGrnImplDtlList = new ArrayList<PurchaseReturnItemDtlViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						PurchaseReturnItemDtlViewResponseModel dtlForItemResponseModel = new PurchaseReturnItemDtlViewResponseModel();
						dtlForItemResponseModel.setPurchaseReturnItemId((BigInteger) row.get("PurchaseReturnItemId"));
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
				requestModel.setFlag(4);
				data = fetchpurchaseReturnDtlView(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					List<PurchaseReturnViewAppResponseModel> salesMachineAppDtlList = new ArrayList<PurchaseReturnViewAppResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						PurchaseReturnViewAppResponseModel appResponseModel = new PurchaseReturnViewAppResponseModel();
						appResponseModel.setPurchaseReturnAppId((BigInteger) row.get("PurchaseReturnAppId"));
						if(row.get("RejectedFlag") != null) {
							appResponseModel.setRejectedFlag((Character) row.get("RejectedFlag"));
						}
						appResponseModel.setIsFinalApprovalStatus((Character) row.get("IsFinalApprovalStatus"));
						appResponseModel.setHoUserId((BigInteger) row.get("HoUserId"));
						appResponseModel.setHoUser((String) row.get("HoUser"));
						appResponseModel.setGrpSeqNo((Integer) row.get("GrpSeqNo"));
						appResponseModel.setDesignationLevelId((Integer) row.get("DesignationLevelId"));
						appResponseModel.setApproverLevelSeq((Integer) row.get("ApproverLevelSeq"));
						appResponseModel.setApprovedDate((String) row.get("ApprovedDate"));
						appResponseModel.setApprovedAmount((BigDecimal) row.get("ApprovedAmount"));
						appResponseModel.setApprovalStatus((String) row.get("ApprovalStatus"));
						appResponseModel.setRemarks((String) row.get("Remarks"));

						salesMachineAppDtlList.add(appResponseModel);
					}
					responseModel.setSalesMachinePurchaseReturnAppList(salesMachineAppDtlList);
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
	public List fetchpurchaseReturnDtlView(Session session, String userCode,
			PurchaseReturnViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchGrnDtlForPurchaseReturn invoked..");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "[SP_SA_GRN_PUR_RET_GRN_VIEW] :userCode, :prId, :prNo, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("prId", requestModel.getPurchaseReturnId());
			query.setParameter("prNo", requestModel.getPurchaseReturnNumber());
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
