/**
 * 
 */
package com.hitech.dms.web.dao.quotation.view;

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

import com.hitech.dms.web.model.quotation.view.request.VehQuoViewRequestModel;
import com.hitech.dms.web.model.quotation.view.response.VehQuoDTLViewResponseModel;
import com.hitech.dms.web.model.quotation.view.response.VehQuoImplementViewResponseModel;
import com.hitech.dms.web.model.quotation.view.response.VehQuoViewResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class VehQuoViewDaoImpl implements VehQuoViewDao {
	private static final Logger logger = LoggerFactory.getLogger(VehQuoViewDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public VehQuoViewResponseModel fetchQuotationDTL(String userCode, VehQuoViewRequestModel quoViewRequestModel,
			Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchQuotationDTL invoked.." + userCode);
			logger.debug(quoViewRequestModel.toString());
		}
		Session session = null;
		Query query = null;
		VehQuoViewResponseModel responseModel = null;
		String sqlQuery = "exec [SP_SA_QUO_View] :quotationId, :flag";
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("quotationId", quoViewRequestModel.getQuotationId());
//			query.setParameter("branchId", quoViewRequestModel.getBranchhId());
			query.setParameter("flag", (quoViewRequestModel.getFlag() == null ? 1 : quoViewRequestModel.getFlag()));
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new VehQuoViewResponseModel();
					responseModel.setQuotationId((BigInteger) row.get("Quotation_id"));
					responseModel.setQuotationNumber((String) row.get("quotation_number"));
					responseModel.setQuotationDate((String) row.get("quotation_date"));
					responseModel.setCustomerContactTitle((String) row.get("Cust_ContactTitle"));
					responseModel.setCustomerCode((String) row.get("CustomerCode"));
					responseModel.setProspectType((String) row.get("Customer_Type"));
					responseModel.setCustomerMobileNumber((String) row.get("Cust_Mobile_No"));
					responseModel.setCustomerCompanyName((String) row.get("Cust_Company_Name"));
					responseModel.setFirstName((String) row.get("FirstName"));
					responseModel.setMiddleName((String) row.get("MiddleName"));
					responseModel.setLastName((String) row.get("LastName"));
					responseModel.setWhatsappNumber((String) row.get("WhatsappNo"));
					responseModel.setAlternateNumber((String) row.get("Alternate_No"));
					responseModel.setStdPhoneNumber((String) row.get("STD_PhoneNumber"));
					responseModel.setEmailId((String) row.get("Email_id"));
					responseModel.setAddress1((String) row.get("address1"));
					responseModel.setAddress2((String) row.get("address2"));
					responseModel.setAddress3((String) row.get("address3"));
					responseModel.setDistrict((String) row.get("District"));
					responseModel.setTehsil((String) row.get("Tehsil"));
					responseModel.setVillage((String) row.get("Village"));
					responseModel.setPinCode((String) row.get("PinCode"));
					responseModel.setState((String) row.get("State"));
					responseModel.setCountry((String) row.get("Country"));
					responseModel.setDateOfBirth((String) row.get("DateOfBirth"));
					responseModel.setAnniversaryDate((String) row.get("AnniversaryDate"));
					responseModel.setPanNumber((String) row.get("PAN_NO"));
					responseModel.setGstNumber((String) row.get("GST_NO"));
					responseModel.setTotalBasicValue((BigDecimal) row.get("Total_Basic_Value"));
					responseModel.setTotalDiscount((BigDecimal) row.get("Total_Discount"));
					responseModel.setTotalTaxableValue((BigDecimal) row.get("Total_Taxable_amount"));
					responseModel.setTotalGstAmount((BigDecimal) row.get("Total_GST_Amount"));
					responseModel.setTotalCharges((BigDecimal) row.get("Total_Charges"));
					responseModel.setTotalAmount((BigDecimal) row.get("Total_Amount"));
					responseModel.setInsurance((BigDecimal) row.get("INSURANCE"));
					responseModel.setRto((BigDecimal) row.get("RTO"));
					responseModel.setHsrp((BigDecimal) row.get("HSRP"));
					responseModel.setCharges((BigDecimal) row.get("CHARGES"));
				}

				quoViewRequestModel.setFlag(2);
				List<VehQuoDTLViewResponseModel> vehQuoDTLList = fetchQuoDTLList(session, userCode, quoViewRequestModel,
						device);
				responseModel.setVehQuoDTLList(vehQuoDTLList);
				quoViewRequestModel.setFlag(3);
				List<VehQuoImplementViewResponseModel> vehQuoImplementList = fetchQuoImplementDTLList(session, userCode,
						quoViewRequestModel, device);
				responseModel.setVehQuoImplementList(vehQuoImplementList);

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
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	private List<VehQuoDTLViewResponseModel> fetchQuoDTLList(Session session, String userCode,
			VehQuoViewRequestModel quoViewRequestModel, Device device) {

		Query query = null;
		List<VehQuoDTLViewResponseModel> responseList = null;
		VehQuoDTLViewResponseModel quoDTLViewResponseModel = null;
		String sqlQuery = "exec [SP_SA_QUO_View] :quotationId, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("quotationId", quoViewRequestModel.getQuotationId());
//			query.setParameter("branchId", quoViewRequestModel.getBranchhId());
			query.setParameter("flag", quoViewRequestModel.getFlag());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<VehQuoDTLViewResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					quoDTLViewResponseModel = new VehQuoDTLViewResponseModel();
					quoDTLViewResponseModel.setEnquiryId((BigInteger) row.get("enquiry_id"));
					quoDTLViewResponseModel.setEnquiryNumber((String) row.get("enquiry_number"));
					quoDTLViewResponseModel.setEnquiryDate((String) row.get("enquiry_date"));
					quoDTLViewResponseModel.setModelName((String) row.get("model_name"));
					quoDTLViewResponseModel.setSourceOfEnquiry((String) row.get("SourceOfEnquiry"));
					quoDTLViewResponseModel.setDspName((String) row.get("DSP_name"));
					quoDTLViewResponseModel.setExpectedPurchaseDate((String) row.get("expected_purchase_date"));
					quoDTLViewResponseModel.setEnquiryType((String) row.get("ProspectType"));
					quoDTLViewResponseModel.setEnquiryStage((String) row.get("EnquiryStage"));
					quoDTLViewResponseModel.setQuotationDtlId((BigInteger) row.get("Quotation_Dtl_id"));
					quoDTLViewResponseModel.setItemNumber((String) row.get("item_no"));
					quoDTLViewResponseModel.setItemDescription((String) row.get("item_description"));
					quoDTLViewResponseModel.setQty((Integer) row.get("qty"));
					quoDTLViewResponseModel.setUnitRate((BigDecimal) row.get("Rate"));
					quoDTLViewResponseModel.setBasicValue((BigDecimal) row.get("basic_value"));
					quoDTLViewResponseModel.setGrossAmount((BigDecimal) row.get("gross_discount"));
					quoDTLViewResponseModel.setAmountAfterDiscount((BigDecimal) row.get("amount_after_discount"));
					quoDTLViewResponseModel.setIgstPer((Double) row.get("igst_per"));
					quoDTLViewResponseModel.setIgstAmnt((BigDecimal) row.get("igst_amount"));
					quoDTLViewResponseModel.setCgstPer((Double) row.get("cgst_per"));
					quoDTLViewResponseModel.setCgstAmnt((BigDecimal) row.get("cgst_amount"));
					quoDTLViewResponseModel.setSgstPer((Double) row.get("sgst_per"));
					quoDTLViewResponseModel.setSgstAmnt((BigDecimal) row.get("sgst_amount"));
					quoDTLViewResponseModel.setTotalGstAmnt((BigDecimal) row.get("Total_gst_amount"));
					quoDTLViewResponseModel.setTotalItemAmnt((BigDecimal) row.get("Total_Item_Amount"));

					responseList.add(quoDTLViewResponseModel);
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return responseList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	private List<VehQuoImplementViewResponseModel> fetchQuoImplementDTLList(Session session, String userCode,
			VehQuoViewRequestModel quoViewRequestModel, Device device) {

		Query query = null;
		List<VehQuoImplementViewResponseModel> responseList = null;
		VehQuoImplementViewResponseModel quoDTLViewResponseModel = null;
		String sqlQuery = "exec [SP_SA_QUO_View] :quotationId, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("quotationId", quoViewRequestModel.getQuotationId());
//			query.setParameter("branchId", quoViewRequestModel.getBranchhId());
			query.setParameter("flag", quoViewRequestModel.getFlag());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<VehQuoImplementViewResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					quoDTLViewResponseModel = new VehQuoImplementViewResponseModel();
					quoDTLViewResponseModel.setQuotationImplementId((BigInteger) row.get("quotation_Implement_id"));
					quoDTLViewResponseModel.setMachineItemId((BigInteger) row.get("machine_item__id"));
					quoDTLViewResponseModel.setItemNumber((String) row.get("item_no"));
					quoDTLViewResponseModel.setItemDescription((String) row.get("item_description"));
					quoDTLViewResponseModel.setQty((Integer) row.get("qty"));
					quoDTLViewResponseModel.setUnitRate((BigDecimal) row.get("Rate"));
					quoDTLViewResponseModel.setBasicValue((BigDecimal) row.get("basic_value"));
					quoDTLViewResponseModel.setGrossAmount((BigDecimal) row.get("gross_discount"));
					quoDTLViewResponseModel.setAmountAfterDiscount((BigDecimal) row.get("amount_after_discount"));
					quoDTLViewResponseModel.setIgstPer((Double) row.get("igst_per"));
					quoDTLViewResponseModel.setIgstAmnt((BigDecimal) row.get("igst_amount"));
					quoDTLViewResponseModel.setCgstPer((Double) row.get("cgst_per"));
					quoDTLViewResponseModel.setCgstAmnt((BigDecimal) row.get("cgst_amount"));
					quoDTLViewResponseModel.setSgstPer((Double) row.get("sgst_per"));
					quoDTLViewResponseModel.setSgstAmnt((BigDecimal) row.get("sgst_amount"));
					quoDTLViewResponseModel.setTotalGstAmnt((BigDecimal) row.get("Total_gst_amount"));
					quoDTLViewResponseModel.setTotalItemAmnt((BigDecimal) row.get("Total_Item_Amount"));

					responseList.add(quoDTLViewResponseModel);
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return responseList;
	}
}
