/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.view;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.enquiry.view.request.EnquiryViewRequestModel;
import com.hitech.dms.web.model.enquiry.view.response.EnquiryCropDTLResponseModel;
import com.hitech.dms.web.model.enquiry.view.response.EnquiryCustFleetDTLResponseModel;
import com.hitech.dms.web.model.enquiry.view.response.EnquirySoilTypeResponseModel;
import com.hitech.dms.web.model.enquiry.view.response.EnquiryViewExchangeResponseModel;
import com.hitech.dms.web.model.enquiry.view.response.EnquiryViewImgResponseModel;
import com.hitech.dms.web.model.enquiry.view.response.EnquiryViewMainResponseModel;
import com.hitech.dms.web.model.enquiry.view.response.EnquiryViewResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class EnquiryViewDaoImpl implements EnquiryViewDao {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryViewDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	public EnquiryViewMainResponseModel fetchEnqDTL(String userCode, BigInteger enquiryId, int flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqDTL invoked.." + enquiryId);
		}
		Session session = null;
		EnquiryViewMainResponseModel responseModel = new EnquiryViewMainResponseModel();
		try {
			session = sessionFactory.openSession();
			EnquiryViewResponseModel enquiryViewModel = fetchEnquiryDTL(session, userCode, enquiryId, 1);
			List<EnquiryCustFleetDTLResponseModel> custFleetDTLList = fetchEnquiryCustFleetList(session, userCode,
					enquiryId, 2);
			List<EnquiryCropDTLResponseModel> enquiryCropDTLList = fetchEnquiryCropList(session, userCode, enquiryId,
					3);
			List<EnquirySoilTypeResponseModel> enquirySoilTypeList = fetchEnquirySoilTypeList(session, userCode,
					enquiryId, 4);

			List<EnquiryViewImgResponseModel> enquiryAttachImgList = fetchEnquiryAttachImgList(session, userCode,
					enquiryId, 5);

			responseModel.setEnquiryHDRDTL(enquiryViewModel);
			if(enquiryViewModel != null && enquiryViewModel.getEnquiryExcDTLId() != null) {
				EnquiryViewExchangeResponseModel enquiryViewExchangeResponseModel = null;
				List<EnquiryViewExchangeResponseModel> enquiryViewExchangeResponseList = null;
				enquiryViewExchangeResponseList = new ArrayList<EnquiryViewExchangeResponseModel>();
				
				enquiryViewExchangeResponseModel = new EnquiryViewExchangeResponseModel();
				enquiryViewExchangeResponseModel.setBrandId(enquiryViewModel.getExchangeBrandId());
				enquiryViewExchangeResponseModel.setEnquiryExcDTLId(enquiryViewModel.getEnquiryExcDTLId());
				enquiryViewExchangeResponseModel.setEstimatedExchangePrice(enquiryViewModel.getEstimated_exchange_price());
				enquiryViewExchangeResponseModel.setModelName(enquiryViewModel.getExchangeModel());
				enquiryViewExchangeResponseModel.setModelYear(enquiryViewModel.getExchangeModel_year());
				enquiryViewExchangeResponseList.add(enquiryViewExchangeResponseModel);
				
				responseModel.setEnquiryViewExchangeList(enquiryViewExchangeResponseList);
			}
			
			responseModel.setEnquiryCustFleetDTLList(custFleetDTLList);
			responseModel.setEnquiryCropDTLList(enquiryCropDTLList);
			responseModel.setEnquirySoilTypeList(enquirySoilTypeList);
			responseModel.setEnquiryAttachImgList(enquiryAttachImgList);

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

	public EnquiryViewMainResponseModel fetchEnqDTL(String userCode, EnquiryViewRequestModel enquiryViewRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnqDTL invoked.." + enquiryViewRequestModel.toString());
		}
		Session session = null;
		EnquiryViewMainResponseModel responseModel = new EnquiryViewMainResponseModel();
		try {
			session = sessionFactory.openSession();
			EnquiryViewResponseModel enquiryViewModel = fetchEnquiryDTL(session, userCode,
					enquiryViewRequestModel.getEnquiryId(), 1);
			List<EnquiryCustFleetDTLResponseModel> custFleetDTLList = fetchEnquiryCustFleetList(session, userCode,
					enquiryViewRequestModel.getEnquiryId(), 2);
			List<EnquiryCropDTLResponseModel> enquiryCropDTLList = fetchEnquiryCropList(session, userCode,
					enquiryViewRequestModel.getEnquiryId(), 3);
			List<EnquirySoilTypeResponseModel> enquirySoilTypeList = fetchEnquirySoilTypeList(session, userCode,
					enquiryViewRequestModel.getEnquiryId(), 4);
			List<EnquiryViewImgResponseModel> enquiryAttachImgList = fetchEnquiryAttachImgList(session, userCode,
					enquiryViewRequestModel.getEnquiryId(), 5);

			responseModel.setEnquiryHDRDTL(enquiryViewModel);
			if(enquiryViewModel != null && enquiryViewModel.getEnquiryExcDTLId() != null) {
				EnquiryViewExchangeResponseModel enquiryViewExchangeResponseModel = null;
				List<EnquiryViewExchangeResponseModel> enquiryViewExchangeResponseList = null;
				enquiryViewExchangeResponseList = new ArrayList<EnquiryViewExchangeResponseModel>();
				
				enquiryViewExchangeResponseModel = new EnquiryViewExchangeResponseModel();
				enquiryViewExchangeResponseModel.setBrandId(enquiryViewModel.getExchangeBrandId());
				enquiryViewExchangeResponseModel.setEnquiryExcDTLId(enquiryViewModel.getEnquiryExcDTLId());
				enquiryViewExchangeResponseModel.setEstimatedExchangePrice(enquiryViewModel.getEstimated_exchange_price());
				enquiryViewExchangeResponseModel.setModelName(enquiryViewModel.getExchangeModel());
				enquiryViewExchangeResponseModel.setModelYear(enquiryViewModel.getExchangeModel_year());
				enquiryViewExchangeResponseList.add(enquiryViewExchangeResponseModel);
				
				responseModel.setEnquiryViewExchangeList(enquiryViewExchangeResponseList);
			}
			responseModel.setEnquiryCustFleetDTLList(custFleetDTLList);
			responseModel.setEnquiryCropDTLList(enquiryCropDTLList);
			responseModel.setEnquirySoilTypeList(enquirySoilTypeList);
			responseModel.setEnquiryAttachImgList(enquiryAttachImgList);

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
	public EnquiryViewResponseModel fetchEnquiryDTL(Session session, String userCode, BigInteger enquiryId, int flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnquiryDTL invoked.." + enquiryId);
		}
		Query query = null;
		EnquiryViewResponseModel responseModel = null;
		String sqlQuery = "exec [SP_ENQ_GetEnquiryDetails] :userCode, :enquiryId, :flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("enquiryId", enquiryId);
			query.setParameter("flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new EnquiryViewResponseModel();
					responseModel.setEnquiryId((BigInteger) row.get("enquiry_id"));
					responseModel.setBranchId((BigInteger) row.get("branch_id"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setEnqNumber((String) row.get("enquiry_number"));
					responseModel.setEnquiryDate((String) row.get("enquiry_date"));
					responseModel.setEnquiryStatus((String) row.get("enquiry_status"));
					responseModel.setSourceOfEnquiry((String) row.get("SourceOfEnquiry"));
					responseModel.setProfitCenter((String) row.get("Profit_Center"));
					responseModel.setPcId((Integer) row.get("pc_id"));
					responseModel.setActivitySourceID((Integer) row.get("Activity_Source_ID"));
					responseModel.setEnquirySourceId((Integer) row.get("Enq_Source_Id"));
					responseModel.setEnquiryTypeId((BigInteger) row.get("enquiry_type_id"));
					responseModel.setEnquiryType((String) row.get("enquiry_type"));
					responseModel.setEnquiryStageId((Integer) row.get("enquiry_stage_id"));
					responseModel.setExpectedPurchaseDate((String) row.get("expected_purchase_date"));
					responseModel.setNextFollowupDate((String) row.get("next_follow_up_date"));
					responseModel.setSalesmanId((BigInteger) row.get("salesman_id"));
					responseModel.setDspName((String) row.get("DSP_name"));
					responseModel.setEnquiryRemarks((String) row.get("EnquiryRemarks"));
					responseModel.setActivityDesc((String) row.get("ActivityType"));
					responseModel.setDigitalSourceName((String) row.get("DigitalSourceName"));
					responseModel.setCustomerName((String) row.get("customer_name"));
					responseModel.setCustomerId((BigInteger) row.get("customer_id"));
					
					responseModel.setProspectType((String) row.get("ProspectType"));
					responseModel.setCustomerCategoryId((BigInteger) row.get("CustomerCategory_Id"));
					responseModel.setCustomerCode((String) row.get("customer_code"));
					responseModel.setPincode((String) row.get("pincode"));
					responseModel.setVillage((String) row.get("village"));
					responseModel.setTehsil((String) row.get("tehsil"));
					responseModel.setDistrict((String) row.get("district"));
					responseModel.setState((String) row.get("state"));
					responseModel.setCountry((String) row.get("country"));
					responseModel.setPinId((BigInteger) row.get("pin_id"));
					responseModel.setCityId((BigInteger) row.get("city_id"));
					responseModel.setTehsilId((BigInteger) row.get("tehsil_id"));
					responseModel.setDistrictId((BigInteger) row.get("district_id"));
					responseModel.setStateId((BigInteger) row.get("state_id"));
					responseModel.setCountryId((BigInteger) row.get("country_id"));

					responseModel.setEnquiryStage((String) row.get("EnquiryStage"));
					responseModel.setFinancierName((String) row.get("FINANCIER_NAME"));
					responseModel.setNextFollowupActivity((String) row.get("NEXT_FOLLOW_UP_ACTIVITY"));
					responseModel.setValidationDate((String) row.get("Validation_date"));
					responseModel.setValidationRemarks((String) row.get("Validation_Remarks"));
					responseModel.setFieldActivityType((String) row.get("FieldActivityType"));
					responseModel.setNextFollowupActivityId((BigInteger) row.get("next_followup_activity_id"));
					responseModel.setActivityDate((String) row.get("Activity_Date"));
					responseModel.setActivityPlanNo((String) row.get("activity_Plan_No"));
					responseModel.setDigitalPlatform((String) row.get("DigitalPlatform"));
					responseModel.setDigitalEnqDate((String) row.get("Digital_Enq_Date"));
					responseModel.setDigitalSourceId((Integer) row.get("Digital_Source_ID"));
					Character digitalValidationStatus = (Character) row.get("Digital_Validation_Status");
					if (digitalValidationStatus != null && digitalValidationStatus.toString().equals("Y")) {
						responseModel.setDigitalValidationStatus(true);
					} else {
						responseModel.setDigitalValidationStatus(false);
					}
					responseModel.setDigitalValidationBy((String) row.get("Digital_Validation_By"));
					responseModel.setActivityPlanHDRID((BigInteger) row.get("Activity_Plan_HDR_ID"));
					responseModel.setMobileNo((String) row.get("Mobile_No"));
					responseModel.setProspectCode((String) row.get("ProspectCode"));
					responseModel.setProspectCategory((String) row.get("ProspectCategory"));
					responseModel.setCompanyName((String) row.get("CompanyName"));
					responseModel.setTitle((String) row.get("Title"));
					responseModel.setFirstName((String) row.get("FirstName"));
					responseModel.setMiddleName((String) row.get("MiddleName"));
					responseModel.setLastName((String) row.get("LastName"));
					responseModel.setWhatsappNo((String) row.get("WhatsappNo"));
					responseModel.setAlternateNo((String) row.get("Alternate_No"));
					responseModel.setPhoneNumber((String) row.get("PhoneNumber"));
					responseModel.setEmailId((String) row.get("Email_id"));
					responseModel.setAddress1((String) row.get("address1"));
					responseModel.setAddress2((String) row.get("address2"));
					responseModel.setAddress3((String) row.get("address3"));
					responseModel.setDateOfBirth((String) row.get("DateOfBirth"));
					responseModel.setAnniversaryDate((String) row.get("AnniversaryDate"));
					responseModel.setPanNo((String) row.get("PAN_NO"));
					responseModel.setGstIn((String) row.get("GSTIN"));
					responseModel.setMachine_item_id((BigInteger) row.get("machine_item_id"));
					responseModel.setItemNo((String) row.get("item_no"));
					responseModel.setItemDescription((String) row.get("item_description"));
					responseModel.setModelId((BigInteger) row.get("model_id"));
					responseModel.setModelName((String) row.get("model_name"));
					responseModel.setVariant((String) row.get("variant"));
					responseModel.setSeriesName((String) row.get("series_name"));
					responseModel.setSegmentName((String) row.get("segment_name"));
					Character exchangeRequired = (Character) row.get("exchange_required");
					if (exchangeRequired != null && exchangeRequired.toString().equals("Y")) {
						responseModel.setExchangeRequired(true);
					} else {
						responseModel.setExchangeRequired(false);
					}
					responseModel.setExchangeBrand((String) row.get("ExchangeBrand"));
					responseModel.setExchangeModel((String) row.get("ExchangeModel"));
					responseModel.setExchangeModel_year((Integer) row.get("ExchangeModel_year"));
					responseModel.setEstimated_exchange_price((BigDecimal) row.get("estimated_exchange_price"));
					responseModel.setEnquiryExcDTLId((BigInteger) row.get("enq_exc_dtl_id"));
					responseModel.setExchangeBrandId((BigInteger) row.get("exchangeBrandId"));
					Character machineRec = (Character) row.get("machine_received");
					if(machineRec != null && machineRec.toString().equals("Y")) {
						responseModel.setMachine_received("true");
					}else {
						responseModel.setMachine_received("false");
					}
					
					responseModel.setOccupationID((BigInteger) row.get("Occupation_ID"));
					responseModel.setOccupation((String) row.get("Occupation"));
					responseModel.setLandSize((Double) row.get("LandSize"));
					responseModel.setDealPrice((BigDecimal) row.get("DealPrice"));
					responseModel.setCashOrLoan((String) row.get("CashOrLoan"));
					responseModel.setFinancier_party_id((BigInteger) row.get("financier_party_id"));
					responseModel.setFinancier((String) row.get("Financier"));
					responseModel.setFinanceLogDate((String) row.get("FinanceLogDate"));
					responseModel.setLoanAmnt((BigDecimal) row.get("LoanAmt"));
					responseModel.setTenure((Integer) row.get("Tenure"));
					responseModel.setAnnualRate((Double) row.get("AnnualRate"));
					responseModel.setEmiAmnt((BigDecimal) row.get("EMIAmt"));
					responseModel.setRetailFinaceStatus((String) row.get("RetailFinaceStatus"));
					responseModel.setDisbursedDate((String) row.get("DisbursedDate"));
					responseModel.setDisbursedAmt((BigDecimal) row.get("DisbursedAmt"));
					responseModel.setActualExchangeAmt((BigDecimal) row.get("ActualExchangeAmt"));
					Character subsidy_required = (Character) row.get("subsidy_required");
					if (subsidy_required != null && subsidy_required.toString().equals("Y")) {
						responseModel.setSubsidy_required(true);
					} else {
						responseModel.setSubsidy_required(false);
					}
					responseModel.setSubsidyAmt((BigDecimal) row.get("SubsidyAmt"));
					responseModel.setActualSubsidyAmt((BigDecimal) row.get("ActualSubsidyAmt"));
					responseModel.setActualAmtReceived((BigDecimal) row.get("ActualAmtReceived"));
					responseModel.setMarginAmt((BigDecimal) row.get("MarginAmt"));
					responseModel.setInitialMarginAmt((BigDecimal) row.get("InitialMarginAmt"));
					responseModel.setRemainingMarginAmt((BigDecimal) row.get("RemainingMarginAmt"));
					responseModel.setPendingMarginAmt((BigDecimal) row.get("PendingMarginAmt"));
					responseModel.setEnq_close_brand_id((Integer) row.get("enq_close_brand_id"));
					responseModel.setDigital_Enq_DTL_ID((BigInteger) row.get("Digital_Enq_DTL_ID"));
					
					responseModel.setPendingSubsidyAmt((BigDecimal) row.get("pendingSubsidyAmt"));
					responseModel.setPendingMarginAmt((BigDecimal) row.get("PendingMarginAmt"));
					responseModel.setActualExchangeAmt((BigDecimal) row.get("actualExchangeAmt"));	
					//responseModel.setPendingExchangeAmt((BigDecimal) row.get("pendingExchangeAmt"));
					responseModel.setPendingLoanAmt((BigDecimal) row.get("pendingloanAmt"));
					//responseModel.setActualLoanAmt((BigDecimal) row.get("ActualLoanAmt"));
					responseModel.setExchangeStatus((String) row.get("exchangeStatus"));				
					
					
				}
			}

		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}

		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<EnquiryCustFleetDTLResponseModel> fetchEnquiryCustFleetList(Session session, String userCode,
			BigInteger enquiryId, int flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnquiryCustFleetList invoked.." + enquiryId);
		}
		Query query = null;
		List<EnquiryCustFleetDTLResponseModel> responseModelList = null;
		EnquiryCustFleetDTLResponseModel responseModel = null;
		String sqlQuery = "exec [SP_ENQ_GetEnquiryDetails] :userCode, :enquiryId, :flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("enquiryId", enquiryId);
			query.setParameter("flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<EnquiryCustFleetDTLResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new EnquiryCustFleetDTLResponseModel();
					responseModel.setEnq_machinery_id((BigInteger) row.get("enq_machinery_id"));
					responseModel.setBrandName((String) row.get("BrandName"));
					responseModel.setModel((String) row.get("Model"));
					responseModel.setYearOfPurchase(row.get("") == null ? 0 : (int) row.get("YearOfPurchase"));
					responseModel.setBrandId((BigInteger) row.get("brand_id"));
					;
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
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<EnquiryCropDTLResponseModel> fetchEnquiryCropList(Session session, String userCode,
			BigInteger enquiryId, int flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnquiryCropList invoked.." + enquiryId);
		}
		Query query = null;
		List<EnquiryCropDTLResponseModel> responseModelList = null;
		EnquiryCropDTLResponseModel responseModel = null;
		String sqlQuery = "exec [SP_ENQ_GetEnquiryDetails] :userCode, :enquiryId, :flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("enquiryId", enquiryId);
			query.setParameter("flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<EnquiryCropDTLResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new EnquiryCropDTLResponseModel();
					responseModel.setEnq_crop_id((BigInteger) row.get("enq_crop_id"));
					responseModel.setCrop_grown((String) row.get("crop_grown"));
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
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<EnquirySoilTypeResponseModel> fetchEnquirySoilTypeList(Session session, String userCode,
			BigInteger enquiryId, int flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnquirySoilTypeList invoked.." + enquiryId);
		}
		Query query = null;
		List<EnquirySoilTypeResponseModel> responseModelList = null;
		EnquirySoilTypeResponseModel responseModel = null;
		String sqlQuery = "exec [SP_ENQ_GetEnquiryDetails] :userCode, :enquiryId, :flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("enquiryId", enquiryId);
			query.setParameter("flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<EnquirySoilTypeResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new EnquirySoilTypeResponseModel();
					responseModel.setEnq_soil_type_id((BigInteger) row.get("enq_soil_type_id"));
					responseModel.setSoil_type((String) row.get("soil_type"));
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
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<EnquiryViewImgResponseModel> fetchEnquiryAttachImgList(Session session, String userCode,
			BigInteger enquiryId, int flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnquiryAttachImgList invoked.." + enquiryId);
		}
		Query query = null;
		List<EnquiryViewImgResponseModel> responseModelList = null;
		EnquiryViewImgResponseModel responseModel = null;
		String docPath = messageSource.getMessage("enq.file.upload.dir", new Object[] { enquiryId },
				LocaleContextHolder.getLocale());
		String sqlQuery = "exec [SP_ENQ_GetEnquiryDetails] :userCode, :enquiryId, :flag";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("enquiryId", enquiryId);
			query.setParameter("flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<EnquiryViewImgResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new EnquiryViewImgResponseModel();
					responseModel.setEnqAttachImgId((BigInteger) row.get("id"));
					responseModel.setFileName((String) row.get("file_name"));
					responseModel.setDocPath(docPath);
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
		}
		return responseModelList;
	}
}
