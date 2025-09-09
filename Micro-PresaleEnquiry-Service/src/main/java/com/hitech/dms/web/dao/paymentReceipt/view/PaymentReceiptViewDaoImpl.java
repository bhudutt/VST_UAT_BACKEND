package com.hitech.dms.web.dao.paymentReceipt.view;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.paymentReceipt.view.request.PaymentReceiptViewRequestModel;
import com.hitech.dms.web.model.paymentReceipt.view.response.PaymentReceiptDetailsViewResponse;
import com.hitech.dms.web.model.paymentReceipt.view.response.PaymentReceiptEnqAndProspectViewResponseModel;
import com.hitech.dms.web.model.paymentReceipt.view.response.PaymentReceiptViewMainResponse;

/**
 * @author vinay.gautam
 *
 */

@Repository
public class PaymentReceiptViewDaoImpl implements PaymentReceiptViewDao {
	private static final Logger logger = LoggerFactory.getLogger(PaymentReceiptViewDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public PaymentReceiptViewMainResponse fetchPaymentReceiptViewList(String userCode,
			PaymentReceiptViewRequestModel paymentListRequestModel) {

		NativeQuery<?> query = null;
		Map<String, List<?>> mapData = null;
		PaymentReceiptViewMainResponse responseModelList = new PaymentReceiptViewMainResponse();

		PaymentReceiptEnqAndProspectViewResponseModel enqPros = null;
		mapData = callProcedureForView(paymentListRequestModel, 1);
		if (mapData.get("data") != null) {
			for (Object object : mapData.get("data")) {
				Map row = (Map) object;
				enqPros = new PaymentReceiptEnqAndProspectViewResponseModel();
				enqPros.setEnquiryNumber((String) row.get("enquiry_number"));
				enqPros.setEnquiryStatus((String) row.get("enquiry_status"));
				enqPros.setEnquiryDate((String) row.get("enquiry_date"));
				enqPros.setSourceOfEnquiry((String) row.get("SourceOfEnquiry"));
				enqPros.setExpectedPurchaseDate((String) row.get("expected_purchase_date"));
				enqPros.setEnquiryStage((String) row.get("EnquiryStage"));
				enqPros.setModelId((BigInteger) row.get("model_id"));;
				enqPros.setModelName((String) row.get("model_name"));
				enqPros.setModelDesc((String) row.get("model_desc"));
				enqPros.setItemNo((String) row.get("item_no"));
				enqPros.setMobileNo((String) row.get("Mobile_No"));
				enqPros.setProspectCode((String) row.get("ProspectCode"));
				enqPros.setProspectCategory((String) row.get("ProspectCategory"));
				enqPros.setTitle((String) row.get("Title"));
				enqPros.setCompanyName((String) row.get("CompanyName"));
				enqPros.setFirstName((String) row.get("FirstName"));
				enqPros.setMiddleName((String) row.get("MiddleName"));
				enqPros.setLastName((String) row.get("LastName"));
				enqPros.setWhatsappNo((String) row.get("WhatsappNo"));
				enqPros.setAlternateNo((String) row.get("Alternate_No"));
				enqPros.setPhoneNumber((String) row.get("PhoneNumber"));
				enqPros.setEmailid((String) row.get("Email_id"));
				enqPros.setAddress1((String) row.get("address1"));
				enqPros.setAddress2((String) row.get("address2"));
				enqPros.setAddress3((String) row.get("address3"));
				enqPros.setPincode((String) row.get("pincode"));
				enqPros.setVillage((String) row.get("village"));
				enqPros.setTehsil((String) row.get("tehsil"));
				enqPros.setDistrict((String) row.get("district"));
				enqPros.setState((String) row.get("state"));
				enqPros.setCountry((String) row.get("country"));
				enqPros.setDateOfBirth((String) row.get("DateOfBirth"));
				enqPros.setAnniversaryDate((String) row.get("AnniversaryDate"));
				enqPros.setPanNo((String) row.get("PAN_NO"));
				enqPros.setGstin((String) row.get("GSTIN"));
				enqPros.setCustomerBalance((String) row.get("Customer_Balance"));
				responseModelList.setEnqProspect(enqPros);

			}
		}
		PaymentReceiptDetailsViewResponse recepitDtl = new PaymentReceiptDetailsViewResponse();
		List<PaymentReceiptDetailsViewResponse> recepitDtlList = null;
		mapData = callProcedureForView(paymentListRequestModel, 2);
		if (mapData.get("data") != null) {
			recepitDtlList = new ArrayList<PaymentReceiptDetailsViewResponse>();
			for (Object object : mapData.get("data")) {
				Map row = (Map) object;
				recepitDtl = new PaymentReceiptDetailsViewResponse();
				recepitDtl.setReceiptNo((String) row.get("receipt_no"));
				recepitDtl.setReceiptDate((String) row.get("receipt_date"));
				Integer val = (Integer) row.get("receipt_type_id");
				if (val != null) {
				    if (val==5) {
				    	recepitDtl.setEnquiryAmount((BigDecimal) row.get("subsidy_estimated_amount"));
				    } else if (val==1) {
				    	recepitDtl.setEnquiryAmount((BigDecimal) row.get("margin_amount"));
				    }
				}
				recepitDtl.setCustomerName((String) row.get("customer_name"));
//				recepitDtl.setEnquiryAmount((BigDecimal) row.get("receipt_amount"));
				recepitDtl.setReceiptAmount((BigDecimal) row.get("receipt_amt_input"));
				recepitDtl.setRemarks((String) row.get("remarks"));
				recepitDtl.setContactNumber((String) row.get("contact_number"));
				
				
				recepitDtl.setCustomerBalance((String) row.get("customer_balance"));
				recepitDtl.setServiceProvider((String) row.get("service_provider"));
				recepitDtl.setTransactionDate((String) row.get("transaction_date"));
				recepitDtl.setTransactionNo((String) row.get("transaction_no"));
				recepitDtl.setCardName((String) row.get("card_name"));
				recepitDtl.setCardNo((String) row.get("card_no"));
				recepitDtl.setCardType((String) row.get("card_type"));
				recepitDtl.setChequeDdBank((String) row.get("cheque_dd_bank"));
				recepitDtl.setChequeDdDate((String) row.get("cheque_dd_date"));
				recepitDtl.setChequeDdNo((String) row.get("cheque_dd_no"));
				recepitDtlList.add(recepitDtl);
				responseModelList.setViewDetails(recepitDtlList);
			}
		}

		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, List<?>> callProcedureForView(PaymentReceiptViewRequestModel paymentListRequestModel, int flag) {
		Map<String, List<?>> mapData = new HashMap<>();
		Query query = null;
		Session session = null;
		String sqlQuery = "exec [SP_FM_View_PaymentReceiptDtls] :enquiryId,:branchId, :flag";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("enquiryId", paymentListRequestModel.getId());
			query.setParameter("branchId", paymentListRequestModel.getBranchId());
			query.setParameter("flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				mapData.put("data", data);
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mapData;
	}

}
