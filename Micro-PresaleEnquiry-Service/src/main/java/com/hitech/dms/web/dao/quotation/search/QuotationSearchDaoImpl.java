package com.hitech.dms.web.dao.quotation.search;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.quotation.search.request.EnquiryDTLForQUORequestModel;
import com.hitech.dms.web.model.quotation.search.request.VehQuoSearchRequestModel;
import com.hitech.dms.web.model.quotation.search.response.EnquiryDTLForQUOResponseModel;
import com.hitech.dms.web.model.quotation.search.response.VehQuoSearchMainResponseModel;
import com.hitech.dms.web.model.quotation.search.response.VehQuoSearchResponse;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Repository
public class QuotationSearchDaoImpl implements QuotationSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(QuotationSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	DozerBeanMapper mapper;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<EnquiryDTLForQUOResponseModel> fetchEnquiryListForQuotation(Session session, String userCode,
			EnquiryDTLForQUORequestModel enquiryListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnquiryListForQuotation invoked.." + enquiryListRequestModel.toString());
		}
		Query query = null;
		List<EnquiryDTLForQUOResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_ENQ_For_Quotation] :userCode, :customerId, :pcID, :branchID";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("branchID", enquiryListRequestModel.getBranchId());
			query.setParameter("userCode", userCode);
			query.setParameter("customerId", enquiryListRequestModel.getCustomerId());
			query.setParameter("pcID", enquiryListRequestModel.getPcId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<EnquiryDTLForQUOResponseModel>();
				EnquiryDTLForQUOResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new EnquiryDTLForQUOResponseModel();
					responseModel.setEnquiryId((BigInteger) row.get("enquiry_id"));
					responseModel.setEnqNumber((String) row.get("enquiry_number"));
					responseModel.setSalesman((String) row.get("dsp_name"));
					responseModel.setEnqStage((String) row.get("enq_stage"));
					responseModel.setProspectType((String) row.get("prospect_type"));
					responseModel.setSourceOfEnq((String) row.get("source_of_enq"));
					responseModel.setModelName((String) row.get("model_name"));
					responseModel.setItemDesc((String) row.get("item_no"));
					responseModel.setItemNo((String) row.get("item_description"));
					responseModel.setEnqDate((String) row.get("enquiry_date"));

					responseModel.setExpectedPurchaseDate((String) row.get("expected_date_of_purchase"));

					responseModel.setBasicPrice((BigDecimal) row.get("basic_price"));
					responseModel.setCgstAmount((BigDecimal) row.get("cgst_amount"));
					responseModel.setCgstPercent((Double) row.get("cgst_percent"));
					responseModel.setDiscount((BigDecimal) row.get("discount"));
					responseModel.setGstAmount((BigDecimal) row.get("gst_amount"));
					responseModel.setIgstAmount((BigDecimal) row.get("igst_amount"));
					responseModel.setIgstPercent((Double) row.get("igst_percent"));
					responseModel.setMachineItemId((BigInteger) row.get("machine_item_id"));
					responseModel.setQty((Integer) row.get("qty"));
					responseModel.setRate((BigDecimal) row.get("Rate"));
					responseModel.setSgstAmount((BigDecimal) row.get("sgst_amount"));
					responseModel.setSgstPercent((Double) row.get("sgst_percent"));
					responseModel.setTaxableValue((BigDecimal) row.get("taxable_value"));
					responseModel.setTotalAmount((BigDecimal) row.get("total_amount"));

					responseModelList.add(responseModel);
				}

			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}

	@Override
	public List<EnquiryDTLForQUOResponseModel> fetchEnquiryListForQuotation(String userCode,
			EnquiryDTLForQUORequestModel enquiryListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnquiryList invoked.." + enquiryListRequestModel.toString());
		}
		Session session = null;
		List<EnquiryDTLForQUOResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchEnquiryListForQuotation(session, userCode, enquiryListRequestModel);
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public VehQuoSearchMainResponseModel searchQuotationList(String userCode, VehQuoSearchRequestModel requestModel,
			Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchQuotationList invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Query query = null;
		VehQuoSearchMainResponseModel mainResponseModel = null;
		List<VehQuoSearchResponse> responseModelList = null;
		VehQuoSearchResponse responseModel = null;
		String sqlQuery = "exec [SP_SA_QUO_Search] :userCode, :pcId, :orgHierID, :dealerId, :branchId, :quoNo, :enquiryNo, :enquiryFrom, :enquiryStage, "
				+ " :enquiryFromDate, :enquiryToDate, :prospectType, :includeInactive, :series, :segmant, :variant, :modelID, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcID());
			query.setParameter("orgHierID", requestModel.getOrgHierID());
			query.setParameter("dealerId", requestModel.getDealerID());
			query.setParameter("branchId", requestModel.getBranchID());
			query.setParameter("quoNo", requestModel.getQuoNumber());
			query.setParameter("enquiryNo", requestModel.getEnqNumber());
			query.setParameter("enquiryFrom", requestModel.getEnqFrom());
			query.setParameter("enquiryStage", requestModel.getEnqStageId());
			query.setParameter("enquiryFromDate", (requestModel.getEnqFromDate() == null ? null
					: DateToStringParserUtils.addStratTimeOFTheDay(requestModel.getEnqFromDate())));
			query.setParameter("enquiryToDate", (requestModel.getEnqToDate() == null ? null
					: DateToStringParserUtils.addEndTimeOFTheDay(requestModel.getEnqToDate())));
			query.setParameter("prospectType", requestModel.getProspectType());
			query.setParameter("includeInactive", requestModel.getIncludeInActive());
			query.setParameter("series", requestModel.getSeries());
			query.setParameter("segmant", requestModel.getSegmant());
			query.setParameter("variant", requestModel.getVariant());
			query.setParameter("modelID", requestModel.getModelID());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			System.out.println(requestModel.toString());
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				mainResponseModel = new VehQuoSearchMainResponseModel();
				responseModelList = new ArrayList<VehQuoSearchResponse>();
				Integer totalRecords = 0;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new VehQuoSearchResponse();
					responseModel.setQuotationId((BigInteger) row.get("Quotation_id"));
					responseModel.setQuotationNumber((String) row.get("quotation_number"));
					responseModel.setQuotationDate((String) row.get("quotation_date"));
					responseModel.setCustomerCode((String) row.get("CustomerCode"));
					responseModel.setCustomerName((String) row.get("CustomerName"));
					responseModel.setCustomerMobileNumber((String) row.get("CustomerPhoneNumber"));
					responseModel.setAddress1((String) row.get("address1"));
					responseModel.setAddress2((String) row.get("address2"));
					responseModel.setAddress3((String) row.get("address3"));
					responseModel.setDistrict((String) row.get("District"));
					responseModel.setTehsil((String) row.get("Tehsil"));
					responseModel.setVillage((String) row.get("Village"));
					responseModel.setPinCode((String) row.get("PinCode"));
					responseModel.setState((String) row.get("State"));
					responseModel.setCountry((String) row.get("Country"));
					responseModel.setTotalBasicValue((BigDecimal) row.get("Total_Basic_Value"));
					responseModel.setTotalDiscount((BigDecimal) row.get("Total_Discount"));
					responseModel.setTotalTaxableValue((BigDecimal) row.get("Total_Taxable_amount"));
					responseModel.setTotalGstAmount((BigDecimal) row.get("Total_GST_Amount"));
					responseModel.setTotalCharges((BigDecimal) row.get("Total_Charges"));
					responseModel.setTotalAmount((BigDecimal) row.get("Total_Amount"));

					if (totalRecords == 0) {
						totalRecords = (Integer) row.get("totalRecords");
					}
					responseModelList.add(responseModel);
				}

				mainResponseModel.setResponseModelList(responseModelList);
				mainResponseModel.setTotalRecords(totalRecords);
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mainResponseModel;
	}
}
