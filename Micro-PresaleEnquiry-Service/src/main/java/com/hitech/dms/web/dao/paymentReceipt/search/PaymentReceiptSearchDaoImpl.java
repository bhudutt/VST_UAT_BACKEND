package com.hitech.dms.web.dao.paymentReceipt.search;

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
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.paymentReceipt.search.request.PaymentReceiptSearchRequestModel;
import com.hitech.dms.web.model.paymentReceipt.search.response.PaymentReceiptSearchMainResponseModel;
import com.hitech.dms.web.model.paymentReceipt.search.response.PaymentReceiptSearchResponseModel;

/**
 * @author vinay.gautam
 *
 */

@Repository
public class PaymentReceiptSearchDaoImpl implements PaymentReceiptSearchDao {

	private static final Logger logger = LoggerFactory.getLogger(PaymentReceiptSearchDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public PaymentReceiptSearchMainResponseModel fetchPaymentReceiptList(String userCode,
			PaymentReceiptSearchRequestModel paymentListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnquiryList invoked.." + paymentListRequestModel.toString());
		}

		NativeQuery<?> query = null;
		Session session = null;
		PaymentReceiptSearchResponseModel responseModel = null;
		List<PaymentReceiptSearchResponseModel> responseModelList = null;
		PaymentReceiptSearchMainResponseModel paymentMainSearch = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_FM_PaymentReceipt_searchList] :dealerID, :branchID, :userCode,:enquiryId,:paymentId, :fromDate, :toDate,:includeInactive, :orgHierID, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerID", paymentListRequestModel.getDealerId());
			query.setParameter("branchID", paymentListRequestModel.getBranchId());
			query.setParameter("userCode", userCode);

			query.setParameter("enquiryId", paymentListRequestModel.getEnquiryId());
			query.setParameter("paymentId", paymentListRequestModel.getPaymentId());

			query.setParameter("fromDate", paymentListRequestModel.getFromDate());
			query.setParameter("toDate", paymentListRequestModel.getToDate());

			query.setParameter("includeInactive", paymentListRequestModel.getIncludeInActive());
			query.setParameter("orgHierID", paymentListRequestModel.getOrgHierId());

			query.setParameter("page", paymentListRequestModel.getPage());
			query.setParameter("size", paymentListRequestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<PaymentReceiptSearchResponseModel>();
				paymentMainSearch = new PaymentReceiptSearchMainResponseModel();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PaymentReceiptSearchResponseModel();
					responseModel.setId((BigInteger) row.get("id"));
					//responseModel.setSrNo((BigInteger) row.get("Row#"));
					responseModel.setEnquiryId((BigInteger) row.get("enquiry_id"));
					responseModel.setEnquiryNumber((String) row.get("enquiryNumber"));
					responseModel.setBranchId((BigInteger) row.get("branch_id"));
					responseModel.setReceiptNo((String) row.get("receipt_no"));
					responseModel.setReceiptDate((String) row.get("receipt_date"));
					responseModel.setReceiptAmount((BigDecimal) row.get("receipt_amt_input"));
//					responseModel.setEnquiryAmount((BigDecimal) row.get("receipt_amount"));
					responseModel.setReceiptType((String) row.get("receipt_type"));
					
					responseModel.setReceiptMode((String) row.get("receipt_mode"));
					responseModel.setEnquiryDate((String) row.get("enquiryDate"));
					responseModel.setEnquiryFrom((String) row.get("enquiry_from"));
					responseModel.setProfitCenter((String) row.get("profit_center"));
					responseModel.setSourceOfenquiry((String) row.get("source_of_enq"));
					responseModel.setEnquiryStage((String) row.get("enq_stage"));
					responseModel.setProfitCenter((String) row.get("profit_center"));
					responseModel.setSeriesName((String) row.get("series_name"));
					responseModel.setModelName((String) row.get("model_name"));
					responseModel.setVariant((String) row.get("variant"));
					responseModel.setItemNo((String) row.get("item_no"));
					responseModel.setItemDesc((String) row.get("item_description"));
//					responseModel.setValidatedBy((String) row.get("validated_by"));
					responseModel.setCustomerType((String) row.get("customer_type"));
					responseModel.setCustomerCode((String) row.get("customer_code"));
					responseModel.setCustomerName((String) row.get("customer_name"));
					responseModel.setMobileMo((String) row.get("mobile_no"));
					Integer val = (Integer) row.get("receipt_type_id");
					if (val != null) {
					    if (val==5) {
					        responseModel.setEnquiryAmount((BigDecimal) row.get("subsidy_estimated_amount"));
					    } else if (val==1) {
					        responseModel.setEnquiryAmount((BigDecimal) row.get("margin_amount"));
					    }
					}
				
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					responseModelList.add(responseModel);
				}
				paymentMainSearch.setPaymentSearch(responseModelList);
				paymentMainSearch.setRecordCount(recordCount);
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session.isOpen())
				session.close();
		}
		return paymentMainSearch;
	}

}
