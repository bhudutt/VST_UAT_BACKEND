/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.search;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.enquiry.export.EnquiryListResultResponseModel;
import com.hitech.dms.web.model.enquiry.list.request.EnquiryListRequestModel;
import com.hitech.dms.web.model.enquiry.list.response.EnquiryListResponseModel;
import com.hitech.dms.web.model.enquiry.list.response.ServiceBookingModelDTLResponseModel;
import com.hitech.dms.web.model.enquiry.request.EnquiryReportRequest;
import com.hitech.dms.web.model.enquiry.response.EnquiryReportResponse;
import com.hitech.dms.web.model.enquiry.response.EnquiryReportWithDealerWiseResponse;
import com.hitech.dms.web.model.enquiry.response.EnquriyReportByStateResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Repository
public class EnquirySearchReportDaoImpl implements EnquirySearchReportDao {
	private static final Logger logger = LoggerFactory.getLogger(EnquirySearchReportDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	@Value("${file.upload-dir.EnquiryReport}")
	private String downloadPath;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public EnquiryListResultResponseModel fetchEnquiryList(Session session, String userCode,
			EnquiryListRequestModel enquiryListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnquiryList invoked.." + enquiryListRequestModel.toString());
		}
		System.out.println();
		Query query = null;
		EnquiryListResultResponseModel enquiryListResultResponseModel = null;
		List<EnquiryListResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [sp_ENQ_searchEnquiryList] :dealerID, :branchID, :userCode, :pcID, :enquiryNo, :enquiryFrom, :enquiryStage, :enquiryStatus, :enquiryFromDate, :enquiryToDate, :series, :segmant, :variant, :modelID, :salesPerson, :enquirySourceID, :prospectType, :orgHierID, :includeInactive, :page, :size, :enqFlpFromDate, :enqFlpToDate";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerID", enquiryListRequestModel.getDealerID());
			query.setParameter("branchID", enquiryListRequestModel.getBranchID());
			query.setParameter("userCode", userCode);
			query.setParameter("pcID", enquiryListRequestModel.getPcID());
			query.setParameter("enquiryNo", enquiryListRequestModel.getEnqNumber());
			query.setParameter("enquiryFrom", enquiryListRequestModel.getEnqFrom());
			query.setParameter("enquiryStage", enquiryListRequestModel.getEnqStageId());
			query.setParameter("enquiryStatus", enquiryListRequestModel.getEnqStatus());
			query.setParameter("enquiryFromDate", (enquiryListRequestModel.getEnqFromDate() == null ? null
					: DateToStringParserUtils.addStratTimeOFTheDay(enquiryListRequestModel.getEnqFromDate())));
			query.setParameter("enquiryToDate", (enquiryListRequestModel.getEnqToDate() == null ? null
					: DateToStringParserUtils.addEndTimeOFTheDay(enquiryListRequestModel.getEnqToDate())));
			query.setParameter("series", enquiryListRequestModel.getSeries());
			query.setParameter("segmant", enquiryListRequestModel.getSegmant());
			query.setParameter("variant", enquiryListRequestModel.getVariant());
			query.setParameter("segmant", enquiryListRequestModel.getSegmant());
			query.setParameter("modelID", enquiryListRequestModel.getModelID());
			query.setParameter("salesPerson", enquiryListRequestModel.getSalesPerson());
			query.setParameter("enquirySourceID", enquiryListRequestModel.getEnqSourceID());
			query.setParameter("prospectType", enquiryListRequestModel.getProspectType());
			query.setParameter("orgHierID", enquiryListRequestModel.getOrgHierID());
			query.setParameter("includeInactive", enquiryListRequestModel.getIncludeInActive());
			query.setParameter("page", enquiryListRequestModel.getPage());
			query.setParameter("size", enquiryListRequestModel.getSize());
			query.setParameter("enqFlpFromDate", (enquiryListRequestModel.getEnqFlpFromDate() == null ? null
					: DateToStringParserUtils.addStratTimeOFTheDay(enquiryListRequestModel.getEnqFlpFromDate())));
			query.setParameter("enqFlpToDate", (enquiryListRequestModel.getEnqFlpToDate() == null ? null
					: DateToStringParserUtils.addEndTimeOFTheDay(enquiryListRequestModel.getEnqFlpToDate())));
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				enquiryListResultResponseModel = new EnquiryListResultResponseModel();
				responseModelList = new ArrayList<EnquiryListResponseModel>();
				EnquiryListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new EnquiryListResponseModel();
					responseModel.setSrNo((BigInteger) row.get("Row#"));
					responseModel.setEnquiryId((BigInteger) row.get("enquiry_id"));
					responseModel.setEnqFrom((String) row.get("enquiry_from"));
					responseModel.setAction((String) row.get("action"));
					responseModel.setIsEnqValidated((String) row.get("Isenq_Validated"));
					responseModel.setDealership((String) row.get("Dealership"));
					responseModel.setBranchCode((String) row.get("BranchCode"));
					responseModel.setBranchName((String) row.get("BranchName"));
					responseModel.setBranchLocation((String) row.get("BranchLocation"));
					responseModel.setEnqNumber((String) row.get("enquiryNumber"));
					responseModel.setVstExecutive((String) row.get("vst_executive"));
					responseModel.setSalesman((String) row.get("dsp_name"));
					responseModel.setEnqStage((String) row.get("enq_stage"));
					responseModel.setEnqStatus((String) row.get("enquiry_status"));
					responseModel.setProspectType((String) row.get("prospect_type"));
					responseModel.setSourceOfEnq((String) row.get("source_of_enq"));
					responseModel.setProfitCenter((String) row.get("profit_center"));
					responseModel.setSeries((String) row.get("series_name"));
					responseModel.setSegment((String) row.get("Segmant"));
					responseModel.setModelName((String) row.get("model_name"));
					responseModel.setVariant((String) row.get("variant"));
					responseModel.setItemDesc((String) row.get("item_description"));
					responseModel.setItemNo((String) row.get("item_no"));
					responseModel.setFieldActivityType((String) row.get("field_Activity_Type"));
					responseModel.setActivityPlanDate((String) row.get("activity_plan_Date"));
					responseModel.setActivityPlanNumber((String) row.get("activity_Plan_Number"));
					responseModel.setDigitalPlatForm((String) row.get("DIGITAL_PLATFORM"));
					responseModel.setEnqDate((String) row.get("enquiryDate"));
					responseModel.setDigitalValidatedBy((String) row.get("validated_by"));

					responseModel.setDigitalValidatedDate((String) row.get("validation_date"));
					responseModel.setCustomerType((String) row.get("customer_type"));
					responseModel.setCustomerCode((String) row.get("customer_code"));
					responseModel.setCustomerMobileNo((String) row.get("mobile_no"));
					responseModel.setCustomerName((String) row.get("customer_name"));
					responseModel.setAddress1((String) row.get("address_1"));
					responseModel.setAddress2((String) row.get("address_2"));
					responseModel.setAddress3((String) row.get("address_3"));
					responseModel.setCashLoan((String) row.get("CASH_LOAN"));
					responseModel.setCompanyName((String) row.get("company_name"));
					responseModel.setCountry((String) row.get("country"));
					responseModel.setPincode((String) row.get("pincode"));
					responseModel.setVillage((String) row.get("village"));
					responseModel.setTehsil((String) row.get("tehsil"));
					responseModel.setDistrict((String) row.get("district"));
					responseModel.setState((String) row.get("state"));

					responseModel.setExpectedPurchaseDate((String) row.get("expected_date_of_purchase"));
					responseModel.setNextFollowupDate((String) row.get("next_follow_up_date"));
					responseModel.setNextFollowupActivity((String) row.get("NEXT_FOLLOW_UP_ACTIVITY"));

					responseModel.setFinancierLoggedDate((String) row.get("FINANCE_LOGGED_IN_DATE"));
					responseModel.setFinancierName((String) row.get("FINANCIER_NAME"));
					responseModel.setLoanAmountApplied((BigDecimal) row.get("LOAN_AMOUNT_APPLIED"));
					responseModel.setTenureMonth((Integer) row.get("TENURE"));
					responseModel.setRateOfInterest((Double) row.get("RATE_OF_INTEREST"));

					responseModel.setDealValue((BigDecimal) row.get("DEAL_VALUE"));
					responseModel.setDisbursedAmount((BigDecimal) row.get("DISBURSED_AMOUNT"));
					responseModel.setDisbursedDate((String) row.get("DISBURSED_DATE"));
					responseModel.setEmiAmount((BigDecimal) row.get("EMI_AMOUNT"));
					responseModel.setRetailFinanceStatus((String) row.get("RETAIL_FINANCE_STATUS"));

					responseModel.setExchangeAmount((BigDecimal) row.get("ENCHANGE_AMOUNT"));
					responseModel.setExchangeBrand((String) row.get("EXCHANGE_BRAND"));
					responseModel.setExchangeModel((String) row.get("EXCHANGE_MODEL"));
					responseModel.setExchangeRequired((String) row.get("EXCHANGE_REQUIRED"));
					responseModel.setExchangeYear((String) row.get("ENCHANGE_YEAR"));

					responseModel.setRemarks((String) row.get("REMARKS"));

					responseModel.setSubsidy((String) row.get("SUBSIDY"));
					responseModel.setSubsidyAmount((BigDecimal) row.get("SUBSIDY_AMOUNT"));
					responseModel.setTotalAmountReceived((BigDecimal) row.get("TOTAL_AMOUNT_RECEIVED"));
					responseModel.setTotalRecords((Integer) row.get("totalRecords"));
					responseModel.setSubSource((String) row.get("DigitalSourceName"));
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					Character validationStatus = (Character) row.get("validated_status");
					if (validationStatus != null && validationStatus.toString().equals("Y")) {
						responseModel.setDigitalValidatedStatus(true);
					} else {
						responseModel.setDigitalValidatedStatus(false);
					}

					responseModelList.add(responseModel);
				}
				Collections.sort(responseModelList, new StatusCustomComparator());
				enquiryListResultResponseModel.setRecordCount(recordCount);
				enquiryListResultResponseModel.setEnquiryList(responseModelList);

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return enquiryListResultResponseModel;
	}

	@Override
	public EnquiryListResultResponseModel fetchEnquiryList(String userCode,
			EnquiryListRequestModel enquiryListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnquiryList invoked.." + enquiryListRequestModel.toString());
		}
		Session session = null;
		EnquiryListResultResponseModel enquiryListResultResponseModel = null;
		try {
			session = sessionFactory.openSession();
			enquiryListResultResponseModel = fetchEnquiryList(session, userCode, enquiryListRequestModel);
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
		return enquiryListResultResponseModel;
	}

	@Override
	public List<EnquiryListResponseModel> fetchENQList(String userCode,
			EnquiryListRequestModel enquiryListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchENQList invoked.." + enquiryListRequestModel.toString());
		}
		Session session = null;
		EnquiryListResultResponseModel enquiryListResultResponseModel = null;
		List<EnquiryListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			enquiryListResultResponseModel = fetchEnquiryList(session, userCode, enquiryListRequestModel);
			if (enquiryListResultResponseModel != null) {
				responseModelList = enquiryListResultResponseModel.getEnquiryList();
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

	@SuppressWarnings("deprecation")
	@Override
	public List<EnquiryReportResponse> getENQListForReport(String userCode,
			EnquiryReportRequest reportEnquiryListRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("getENQListForReport invoked.." + userCode + "::::::::::reportEnquiryListRequest::::::::::"
					+ reportEnquiryListRequest);
		}
		Query query = null;
		Session session = null;
		EnquiryReportResponse enquiry = null;
//		int itemNumber = 0;
//		int enquires = 0;
//		int deliveries = 0;
//		int warm = 0;
//		int hot = 0;
//		int cold = 0;
//		int lostEnquiries = 0;
//		int droppedEnquiries = 0;
//		int totalEnquiries = 0;
//		int currentWarm = 0;
//		int currentHot = 0;
//		int currentCold = 0;

		List<EnquiryReportResponse> responseModelList = null;
		// String sqlQuery = " EXEC
		// []:fromDate,:toDate,:profitCenterId,:stateId,:clusterId,:territoryManagerId,:dealerId,:modelId,:enquiryTypeId,:enquiryStatusId,:enquirySourceId";
		// String sqlQuery = " EXEC SP_GET_ENQUIRYLIST_1
		// :FROMDATE,:TODATE,:MODEL,:ENQUIRYTYPE,:ENQUIRYLIST,:ENQUIRYSOURCE,:CLUSTER ";
		String sqlQuery = " EXEC SP_GET_ALL_ENQUIRY_LIST :FROMDATE,:TODATE,:MODEL,:ENQUIRYTYPE,:ENQUIRYLIST,:ENQUIRYSOURCE,:CLUSTER,:STATEID,:PCID,:USERCODE,:orgHierID";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("FROMDATE", reportEnquiryListRequest.getFromDate());
			query.setParameter("TODATE", reportEnquiryListRequest.getToDate());
			query.setParameter("MODEL",
					reportEnquiryListRequest.getModelIds() != null ? reportEnquiryListRequest.getModelIds() : null);
			query.setParameter("ENQUIRYTYPE",
					reportEnquiryListRequest.getEnquiryTypeId() != 0 ? reportEnquiryListRequest.getEnquiryTypeId()
							: null);
			query.setParameter("ENQUIRYLIST",
					reportEnquiryListRequest.getEnquiryStatusId() != 0 ? reportEnquiryListRequest.getEnquiryStatusId()
							: null);
			query.setParameter("ENQUIRYSOURCE",
					reportEnquiryListRequest.getEnquirySourceId() != 0 ? reportEnquiryListRequest.getEnquirySourceId()
							: null);
			query.setParameter("CLUSTER",
					reportEnquiryListRequest.getClusterId() != null ? reportEnquiryListRequest.getClusterId() : null);
			query.setParameter("STATEID",
					reportEnquiryListRequest.getStateId() != 0 ? reportEnquiryListRequest.getStateId() : null);
			query.setParameter("PCID",
					reportEnquiryListRequest.getProfitCenterId() != 0 ? reportEnquiryListRequest.getProfitCenterId()
							: null);
			query.setParameter("USERCODE", userCode);
			query.setParameter("orgHierID",
					reportEnquiryListRequest.getOrgHierID() != null ? reportEnquiryListRequest.getOrgHierID() : null);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				enquiry = new EnquiryReportResponse();
				responseModelList = new ArrayList<EnquiryReportResponse>();
				EnquiryReportResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;

					enquiry = new EnquiryReportResponse();
//					enquiry.setStateDesc((String) row.get("StateDesc"));
//					enquiry.setCluster((String) row.get("Cluster"));
//					enquiry.setTerritoryManager((String) row.get("Territory_Manager"));
//					enquiry.setDealerName((String) row.get("ParentDealerName"));
//					enquiry.setLocation((String) row.get("ParentDealerLocation"));
//					enquiry.setDSP((String) row.get("DSP"));
//					enquiry.setModelName((String) row.get("model_name"));
//					enquiry.setItemNo((int) row.get("ItemNumber"));
//					enquiry.setEnqMonthToDate((int) row.get("Enquiries_Generated_Month_To_Date"));
//					enquiry.setDeliveriesForMonth((int) row.get("Deliveries_for_month"));
//					enquiry.setWarm((int) row.get("WARM"));
//					enquiry.setHot((int) row.get("HOT"));
//					enquiry.setCold((int) row.get("COLD"));
//					enquiry.setLostEnquiries((int) row.get("LostEnquiries"));
//					enquiry.setDroppedEnquiries((int) row.get("DroppedEnquiries"));
//					enquiry.setTotalCurrentEnquiries((int) row.get("Total_Current_Enquiries"));
//					enquiry.setCurrentWarm((int) row.get("CURRENT_WARM"));
//					enquiry.setCurrentHot((int) row.get("CURRENT_HOT"));
//					enquiry.setCurrentCold((int) row.get("CURRENT_COLD"));

					enquiry.setStateId((BigInteger) row.get("state_id"));
					enquiry.setState((String) row.get("StateDesc"));
					enquiry.setOpeningHot((Integer) row.get("TOTAL_HOT"));
					enquiry.setOpeningWarm((Integer) row.get("TOTAL_WARM"));
					enquiry.setOpeningCold((Integer) row.get("TOTAL_PROSPECT"));
					enquiry.setOpeningOverdue((Integer) row.get("TOTAL_OVERDUE"));
					enquiry.setTotalOpeningEnq((Integer) row.get("TOTAL_ENQUIRY"));
					enquiry.setCurrentHotEnq((Integer) row.get("Current_HOT"));
					enquiry.setCurrentWarmEnq((Integer) row.get("Current_WARM"));
					enquiry.setCurrentColdEnq((Integer) row.get("Current_PROSPECT"));
					enquiry.setCurrentOverdueEnq((Integer) row.get("Current_OVERDUE"));
					enquiry.setTotalCurrentEnq((Integer) row.get("TOTAL_CURRENT_ENQUIRY"));

					responseModelList.add(enquiry);
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

	@Override
	public JasperPrint ExcelGeneratorReport(HttpServletRequest request, String jaspername,
			HashMap<String, Object> jasperParameter, String filePath) {
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {

			String filePaths = filePath + jaspername;
			System.out.println("filePath  " + filePaths);
			connection = dataSourceConnection.getConnection();
			System.out.println("connection " + connection);
			if (connection != null) {
				System.out.println("jasperParameter" + jasperParameter);
				System.out.println("filePaths" + filePaths);
				jasperPrint = JasperFillManager.fillReport(filePaths, jasperParameter, connection);

				System.out.println(jasperPrint.getName());
			}
		} catch (Exception e) {
			jasperPrint = null;
			e.printStackTrace();
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				jasperPrint = null;
				e.printStackTrace();
			}
		}
		return jasperPrint;
	}

	@Override
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,
			String reportName) throws JRException {
		if (format != null && format.equalsIgnoreCase("xls")) {
			JRXlsxExporter exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
			reportConfigXLS.setSheetNames(new String[] { "sheet1" });
			exporter.setConfiguration(reportConfigXLS);
			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			exporter.exportReport();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public List<EnquiryReportWithDealerWiseResponse> fetchENQWithStateAndDealerWiseForReport(String userCode,
			EnquiryReportRequest reportEnquiryListRequest) {

		if (logger.isDebugEnabled()) {
			logger.debug("fetchENQWithStateAndDealerWiseForReport invoked.." + userCode
					+ "::::::::::reportEnquiryListRequest::::::::::" + reportEnquiryListRequest);
		}
		Query query = null;
		Session session = null;
		EnquiryReportWithDealerWiseResponse enquiry = null;

		List<EnquiryReportWithDealerWiseResponse> responseModelList = null;
		// String sqlQuery = " EXEC
		// []:fromDate,:toDate,:profitCenterId,:stateId,:clusterId,:territoryManagerId,:dealerId,:modelId,:enquiryTypeId,:enquiryStatusId,:enquirySourceId";
		String sqlQuery = " EXEC SP_GET_ENQUIRYLIST_1 :FROMDATE,:TODATE,:MODEL,:ENQUIRYTYPE,:ENQUIRYLIST,:ENQUIRYSOURCE,:CLUSTER,:STATEID,:DEALERID,:PCID,:USERCODE ";
		try {
			session = sessionFactory.openSession();
			// System.out.println("reportEnquiryListRequest::::::::::" +
			// reportEnquiryListRequest);
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("FROMDATE", reportEnquiryListRequest.getFromDate());
			query.setParameter("TODATE", reportEnquiryListRequest.getToDate());
			query.setParameter("MODEL",
					reportEnquiryListRequest.getModelIds() != null ? reportEnquiryListRequest.getModelIds() : null);
			query.setParameter("ENQUIRYTYPE",
					reportEnquiryListRequest.getEnquiryTypeId() != 0 ? reportEnquiryListRequest.getEnquiryTypeId()
							: null);
			query.setParameter("ENQUIRYLIST",
					reportEnquiryListRequest.getEnquiryStatusId() != 0 ? reportEnquiryListRequest.getEnquiryStatusId()
							: null);
			query.setParameter("ENQUIRYSOURCE",
					reportEnquiryListRequest.getEnquirySourceId() != 0 ? reportEnquiryListRequest.getEnquirySourceId()
							: null);
			query.setParameter("CLUSTER",
					reportEnquiryListRequest.getClusterId() != null ? reportEnquiryListRequest.getClusterId() : null);
			query.setParameter("STATEID",
					reportEnquiryListRequest.getStateId() != 0 ? reportEnquiryListRequest.getStateId() : null);
			query.setParameter("DEALERID",
					reportEnquiryListRequest.getDealerId() != null ? reportEnquiryListRequest.getDealerId() : null);
			query.setParameter("PCID",
					reportEnquiryListRequest.getProfitCenterId() != 0 ? reportEnquiryListRequest.getProfitCenterId()
							: null);
			query.setParameter("USERCODE", userCode);

			System.out.println(query);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				enquiry = new EnquiryReportWithDealerWiseResponse();
				responseModelList = new ArrayList<EnquiryReportWithDealerWiseResponse>();
				EnquiryReportWithDealerWiseResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					enquiry = new EnquiryReportWithDealerWiseResponse();
					enquiry.setStateId((BigInteger) row.get("state_id"));
					enquiry.setState((String) row.get("StateDesc"));
					enquiry.setCluster((String) row.get("Cluster"));
					enquiry.setTerritoryManager((String) row.get("Territory_Manager"));
					enquiry.setDealerName((String) row.get("DealerName"));
					enquiry.setDealerLocation((String) row.get("DealerLocation"));
					enquiry.setDsp((String) row.get("DSP"));
					enquiry.setModel((String) row.get("model_name"));
					enquiry.setItemNo((String) row.get("ItemNumber"));
					enquiry.setEnqMonthToDate((int) row.get("Enquiries_Generated_Month_To_Date"));
					enquiry.setDeliveriesForMonth((int) row.get("Deliveries_for_month"));
					enquiry.setOpeningWarm((int) row.get("TOTAL_WARM"));
					enquiry.setOpeningHot((int) row.get("TOTAL_HOT"));
					enquiry.setOpeningCold((int) row.get("TOTAL_PROSPECT"));
					enquiry.setOpeningOverdue((int) row.get("TOTAL_OVERDUE"));
					enquiry.setLostEnquiries((int) row.get("LostEnquiries"));
					enquiry.setDroppedEnq((int) row.get("DroppedEnquiries"));
					enquiry.setTotalOpeningEnq((int) row.get("TOTAL_ENQUIRY"));
					enquiry.setCurrentWarmEnq((int) row.get("Current_WARM"));
					enquiry.setCurrentHotEnq((int) row.get("Current_HOT"));
					enquiry.setCurrentColdEnq((int) row.get("Current_PROSPECT"));
					enquiry.setCurrentOverdueEnq((int) row.get("Current_OVERDUE"));
					enquiry.setTotalCurrentEnq((int) row.get("TOTAL_CURRENT_ENQUIRY"));
					responseModelList.add(enquiry);
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

	@SuppressWarnings("deprecation")
	@Override
	public List<EnquriyReportByStateResponse> fetchENQWithStateWiseReport(String userCode,
			EnquiryReportRequest reportEnquiryListRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchENQWithStateWiseReport invoked.." + userCode
					+ "::::::::::reportEnquiryListRequest::::::::::" + reportEnquiryListRequest);
		}
		Query query = null;
		Session session = null;
		EnquriyReportByStateResponse enquiryReport = null;

		List<EnquriyReportByStateResponse> responseModelList = null;
		// String sqlQuery = " EXEC
		// []:fromDate,:toDate,:profitCenterId,:stateId,:clusterId,:territoryManagerId,:dealerId,:modelId,:enquiryTypeId,:enquiryStatusId,:enquirySourceId";
		String sqlQuery = " EXEC SP_GET_ALL_ENQUIRY_LIST_DEALERWISE :FROMDATE,:TODATE,:MODEL,:ENQUIRYTYPE,:ENQUIRYLIST,:ENQUIRYSOURCE,:CLUSTER,:STATEID,:DEALERID,:PCID,:USERCODE,:orgHierID ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("FROMDATE", reportEnquiryListRequest.getFromDate());
			query.setParameter("TODATE", reportEnquiryListRequest.getToDate());
			query.setParameter("MODEL",
					reportEnquiryListRequest.getModelIds() != null ? reportEnquiryListRequest.getModelIds() : null);
			query.setParameter("ENQUIRYTYPE",
					reportEnquiryListRequest.getEnquiryTypeId() != 0 ? reportEnquiryListRequest.getEnquiryTypeId()
							: null);
			query.setParameter("ENQUIRYLIST",
					reportEnquiryListRequest.getEnquiryStatusId() != 0 ? reportEnquiryListRequest.getEnquiryStatusId()
							: null);
			query.setParameter("ENQUIRYSOURCE",
					reportEnquiryListRequest.getEnquirySourceId() != 0 ? reportEnquiryListRequest.getEnquirySourceId()
							: null);
			query.setParameter("CLUSTER",
					reportEnquiryListRequest.getClusterId() != null ? reportEnquiryListRequest.getClusterId() : null);

			query.setParameter("STATEID",
					reportEnquiryListRequest.getStateId() != 0 ? reportEnquiryListRequest.getStateId() : null);
			query.setParameter("DEALERID",
					reportEnquiryListRequest.getDealerId() != null ? reportEnquiryListRequest.getDealerId() : null);
			query.setParameter("PCID",
					reportEnquiryListRequest.getProfitCenterId() != 0 ? reportEnquiryListRequest.getProfitCenterId()
							: null);
			query.setParameter("USERCODE", userCode);
			query.setParameter("orgHierID",
					reportEnquiryListRequest.getOrgHierID() != null ? reportEnquiryListRequest.getOrgHierID() : null);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				enquiryReport = new EnquriyReportByStateResponse();
				responseModelList = new ArrayList<EnquriyReportByStateResponse>();
				EnquriyReportByStateResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					enquiryReport = new EnquriyReportByStateResponse();
					enquiryReport.setState((String) row.get("StateDesc"));
					enquiryReport.setStateId((BigInteger) row.get("state_id"));
					enquiryReport.setDealerName((String) row.get("DealerName"));
					enquiryReport.setBranchId((BigInteger) row.get("branch_id"));
					enquiryReport.setParentDealerId((BigInteger) row.get("parent_dealer_id"));
					enquiryReport.setOpeningHot((Integer) row.get("TOTAL_HOT"));
					enquiryReport.setOpeningWarm((Integer) row.get("TOTAL_WARM"));
					enquiryReport.setOpeningCold((Integer) row.get("TOTAL_PROSPECT"));
					enquiryReport.setOpeningOverdue((Integer) row.get("TOTAL_OVERDUE"));
					enquiryReport.setTotalOpeningEnq((Integer) row.get("TOTAL_ENQUIRY"));
					enquiryReport.setCurrentHotEnq((Integer) row.get("Current_HOT"));
					enquiryReport.setCurrentWarmEnq((Integer) row.get("Current_WARM"));
					enquiryReport.setCurrentColdEnq((Integer) row.get("Current_PROSPECT"));
					enquiryReport.setCurrentOverdueEnq((Integer) row.get("Current_OVERDUE"));
					enquiryReport.setTotalCurrentEnq((Integer) row.get("TOTAL_CURRENT_ENQUIRY"));
					responseModelList.add(enquiryReport);
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

	@SuppressWarnings("deprecation")
	@Override
	public List<EnquiryReportWithDealerWiseResponse> fetchENQWithStateAndDealerWiseForSearch(String userCode,
			EnquiryReportRequest reportEnquiryListRequest) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchENQWithStateAndDealerWiseForSearch invoked.." + userCode
					+ "::::::::::reportEnquiryListRequest::::::::::" + reportEnquiryListRequest);
		}
		Query query = null;
		Session session = null;
		EnquiryReportWithDealerWiseResponse enquiry = null;

		List<EnquiryReportWithDealerWiseResponse> responseModelList = null;
		// String sqlQuery = " EXEC
		// []:fromDate,:toDate,:profitCenterId,:stateId,:clusterId,:territoryManagerId,:dealerId,:modelId,:enquiryTypeId,:enquiryStatusId,:enquirySourceId";
		String sqlQuery = " EXEC SP_GET_ENQUIRYLIST_1 :FROMDATE,:TODATE,:MODEL,:ENQUIRYTYPE,:ENQUIRYLIST,:ENQUIRYSOURCE,:CLUSTER,:STATEID,:DEALERID,:PCID,:USERCODE,:orgHierID ";
		try {
			session = sessionFactory.openSession();
			System.out.println("reportEnquiryListRequest::::::::::" + reportEnquiryListRequest);
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("FROMDATE", reportEnquiryListRequest.getFromDate());
			query.setParameter("TODATE", reportEnquiryListRequest.getToDate());
			query.setParameter("MODEL", reportEnquiryListRequest.getModelIds());
			query.setParameter("ENQUIRYTYPE",
					reportEnquiryListRequest.getEnquiryTypeId() != 0 ? reportEnquiryListRequest.getEnquiryTypeId()
							: null);
			query.setParameter("ENQUIRYLIST",
					reportEnquiryListRequest.getEnquiryStatusId() != 0 ? reportEnquiryListRequest.getEnquiryStatusId()
							: null);
			query.setParameter("ENQUIRYSOURCE",
					reportEnquiryListRequest.getEnquirySourceId() != 0 ? reportEnquiryListRequest.getEnquirySourceId()
							: null);
			query.setParameter("CLUSTER",
					reportEnquiryListRequest.getClusterId() != null ? reportEnquiryListRequest.getClusterId() : null);
			query.setParameter("STATEID", reportEnquiryListRequest.getStateId());
			query.setParameter("DEALERID", reportEnquiryListRequest.getDealerId());
			query.setParameter("PCID", reportEnquiryListRequest.getProfitCenterId());
			query.setParameter("USERCODE", userCode);
			query.setParameter("orgHierID",
					reportEnquiryListRequest.getOrgHierID() != null ? reportEnquiryListRequest.getOrgHierID() : null);
			
			

			System.out.println(query);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				enquiry = new EnquiryReportWithDealerWiseResponse();
				responseModelList = new ArrayList<EnquiryReportWithDealerWiseResponse>();
				EnquiryReportWithDealerWiseResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					enquiry = new EnquiryReportWithDealerWiseResponse();
					enquiry.setStateId((BigInteger) row.get("state_id"));
					enquiry.setState((String) row.get("StateDesc"));
					enquiry.setCluster((String) row.get("Cluster"));
					enquiry.setTerritoryManager((String) row.get("Territory_Manager"));
					enquiry.setDealerName((String) row.get("DealerName"));
					enquiry.setDealerLocation((String) row.get("DealerLocation"));
					enquiry.setDsp((String) row.get("DSP"));
					enquiry.setModel((String) row.get("model_name"));
					enquiry.setItemNo((String) row.get("ItemNumber"));
					enquiry.setEnqMonthToDate((int) row.get("Enquiries_Generated_Month_To_Date"));
					enquiry.setDeliveriesForMonth((int) row.get("Deliveries_for_month"));
					enquiry.setOpeningWarm((int) row.get("TOTAL_WARM"));
					enquiry.setOpeningHot((int) row.get("TOTAL_HOT"));
					enquiry.setOpeningCold((int) row.get("TOTAL_PROSPECT"));
					enquiry.setLostEnquiries((int) row.get("LostEnquiries"));
					enquiry.setDroppedEnq((int) row.get("DroppedEnquiries"));
					enquiry.setTotalOpeningEnq((int) row.get("TOTAL_ENQUIRY"));
					enquiry.setCurrentWarmEnq((int) row.get("Current_WARM"));
					enquiry.setCurrentHotEnq((int) row.get("Current_HOT"));
					enquiry.setCurrentColdEnq((int) row.get("Current_PROSPECT"));
					enquiry.setTotalCurrentEnq((int) row.get("TOTAL_CURRENT_ENQUIRY"));
					responseModelList.add(enquiry);
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

	@Override
	public List<ServiceBookingModelDTLResponseModel> fetchModelAllList(String userCode, Device device) {
		Session session = null;
		Query query = null;
		List<ServiceBookingModelDTLResponseModel> responseModelList = null;
		ServiceBookingModelDTLResponseModel responseModel = null;
//		String sqlQuery = "select DISTINCT   mm.model_id, concat(mm.series_name, + ' | ' + mm.Model_name) as Model_name \r\n"
//				+ "from CM_MST_MODEL mm where mm.model_name <> '' order by Model_name asc ";
		String sqlQuery = " select DISTINCT mm.model_id, concat(mm.series_name, + ' | ' + mm.Model_name,+'|'+ mm.segment_name) as Model_name from CM_MST_MODEL mm where mm.model_name <> '' and isActive='Y' order by Model_name asc";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ServiceBookingModelDTLResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ServiceBookingModelDTLResponseModel();
					responseModel.setModelId((BigInteger) row.get("model_id"));
					responseModel.setModelName((String) row.get("Model_name"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	@Override
	public Map<String, Object> getStateIdFormStateDesc(String userCode, String stateDesc) {
		if (logger.isDebugEnabled()) {
			logger.debug("getStateIdFormStateDesc invoked.." + userCode + "::::::::::stateDesc::::::::::" + stateDesc);
		}
		Query query = null;
		Session session = null;
		Map<String, Object> responseListModel = new HashMap<>();
		String sqlQuery = "EXEC [SYS_GET_STATEID_FROM_STATE] :stateDesc ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("stateDesc", stateDesc);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseListModel.put("stateDetails", data);

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
		return responseListModel;
	}

}
