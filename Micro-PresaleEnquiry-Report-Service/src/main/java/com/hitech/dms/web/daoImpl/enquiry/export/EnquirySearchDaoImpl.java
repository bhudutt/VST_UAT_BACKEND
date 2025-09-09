package com.hitech.dms.web.daoImpl.enquiry.export;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.web.dao.enquiry.export.EnquirySearchDao;
import com.hitech.dms.web.model.enquiry.export.EnquiryListRequestModel;
import com.hitech.dms.web.model.enquiry.export.EnquiryReport2Response;
import com.hitech.dms.web.model.enquiry.export.EnquiryResponse;
import com.hitech.dms.web.model.enquiry.export.SalesManModel;
import com.hitech.dms.web.model.enquiry.list.request.SalesEnquiryReportRequest;
import com.hitech.dms.web.model.enquiry.list.response.EnquiryListResponseModel;
import com.hitech.dms.web.model.enquiry.list.response.SalesEnquiryReportResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Repository
public class EnquirySearchDaoImpl implements EnquirySearchDao {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	@Value("${file.upload-dir.EnquiryReport}")
	private String downloadPath;

	private static final Logger logger = LoggerFactory.getLogger(EnquirySearchDaoImpl.class);

	@Override
	public EnquiryReport2Response fetchEnquiryList(String userCode, EnquiryListRequestModel enquiryListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnquiryList invoked.." + enquiryListRequestModel.toString());
		}
		Session session = null;
		EnquiryReport2Response response= new EnquiryReport2Response();
		List<EnquiryResponse> enquiryListResultResponseModel = null;
		try {
			session = sessionFactory.openSession();
			response = fetchEnquiryList(session, userCode, enquiryListRequestModel);
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
		return response;
	}

//	@JsonFormat(pattern = "yyyy/MM/dd")
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy/MM/dd");
	}

	private EnquiryReport2Response fetchEnquiryList(Session session, String userCode,
			EnquiryListRequestModel enquiryListRequestModel) {

		SimpleDateFormat smdf = getSimpleDateFormat();
		EnquiryReport2Response finalresponse= new EnquiryReport2Response();
		String fromDate = smdf.format(enquiryListRequestModel.getFromDate());
		String toDate = smdf.format(enquiryListRequestModel.getToDate());
		String prospectType = enquiryListRequestModel.getProspectType();

		if (prospectType.equalsIgnoreCase("0")) {
			prospectType = null;
		}
		if (enquiryListRequestModel.getEnquiryStatusId().equalsIgnoreCase("0")) {
			enquiryListRequestModel.setEnquiryStatusId(null);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEnquiryList invoked.." + enquiryListRequestModel.toString());
		}

		//System.out.println("prospectType is "+prospectType);
		Query query = null;
		List<EnquiryResponse> response = null;
		EnquiryResponse enquiry = null;
		List<EnquiryListResponseModel> responseModelList = null;
		String sqlQuery = " EXEC [GET_ENQUIRY_LIST_2] :Usercode,:PCID,:dealerId,:branchID,:enquiryNo,"
				+ ":enquiryStage,:enquiryStatus,:enquiryFromDate,:enquiryToDate,:modelID,"
				+ ":salesmanID,:enquirySourceID,:prospectType,:stateId,"
				+ ":orgHierID,:page,:size,:zone,:territory ";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setParameter("Usercode", userCode);
			query.setParameter("PCID",
					enquiryListRequestModel.getProfitCenterId() != null ? enquiryListRequestModel.getProfitCenterId()
							: null);
			query.setParameter("dealerId",
					enquiryListRequestModel.getDealerId() != null ? enquiryListRequestModel.getDealerId() : null);
			query.setParameter("branchID",
					enquiryListRequestModel.getBranchId() != null ? enquiryListRequestModel.getBranchId() : null);
			query.setParameter("enquiryNo",null);
			query.setParameter("enquiryStage",
					enquiryListRequestModel.getEnquiryStage() != null ? enquiryListRequestModel.getEnquiryStage()
							: null);
			query.setParameter("enquiryStatus",
					enquiryListRequestModel.getEnquiryStatusId() != null ? enquiryListRequestModel.getEnquiryStatusId()
							: null);
			query.setParameter("enquiryFromDate", fromDate != null ? fromDate : null);
			query.setParameter("enquiryToDate", toDate != null ? toDate : null);
			query.setParameter("modelID",
					enquiryListRequestModel.getModelId() != null ? enquiryListRequestModel.getModelId() : null);
			query.setParameter("salesmanID",
					enquiryListRequestModel.getSalesManId() != null ? enquiryListRequestModel.getSalesManId() : null);
			query.setParameter("enquirySourceID",
					enquiryListRequestModel.getEnquirySourceId() != null ? enquiryListRequestModel.getEnquirySourceId()
							: null);
			query.setParameter("prospectType",prospectType);
			query.setParameter("stateId",
					enquiryListRequestModel.getStateId() != null ? enquiryListRequestModel.getStateId() : null);
			query.setParameter("orgHierID",
					enquiryListRequestModel.getOrgHierarchyId() != null ? enquiryListRequestModel.getOrgHierarchyId()
							: null);

			query.setParameter("page",
					enquiryListRequestModel.getPage() != null ? enquiryListRequestModel.getPage() : null);
			query.setParameter("size",
					enquiryListRequestModel.getSize() != null ? enquiryListRequestModel.getSize() : null);
			query.setParameter("zone",
					enquiryListRequestModel.getZone() != null ? enquiryListRequestModel.getZone() : null);
			query.setParameter("territory",
					enquiryListRequestModel.getTerritory() != null ? enquiryListRequestModel.getTerritory() : null);
			//query.setParameter("hoId", enquiryListRequestModel.getHoLevelId()!=null ? enquiryListRequestModel.getHoLevelId():null);
	 
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			
			
			
			

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				response = new ArrayList<EnquiryResponse>();
				EnquiryResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					enquiry = new EnquiryResponse();
					String branchCode = (String) row.get("BranchCode");
					// System.out.println("bbb"+branchCode);

					enquiry.setDealerCode(branchCode != null ? branchCode : null);
					enquiry.setDealerName(row.get("BranchName") != null ? (String) row.get("BranchName") : null);
					enquiry.setDealerLocation(
							row.get("BranchLocation") != null ? (String) row.get("BranchLocation") : null);
					// System.out.println("bbb"+branchCode);

					enquiry.setDealerCode(branchCode != null ? branchCode : null);
					enquiry.setDealerName(row.get("BranchName") != null ? (String) row.get("BranchName") : null);
					enquiry.setDealerLocation(
							row.get("BranchLocation") != null ? (String) row.get("BranchLocation") : null);
					enquiry.setEnquiryDate(row.get("enquiryDate") != null ? (String) row.get("enquiryDate") : null);
					enquiry.setEnquiryNumber(
							row.get("enquiryNumber") != null ? (String) row.get("enquiryNumber") : null);
					enquiry.setModelName(row.get("model_name") != null ? (String) row.get("model_name") : null);
					enquiry.setIsenqValidated(
							row.get("Isenq_Validated") != null ? (String) row.get("Isenq_Validated") : null);
					enquiry.setExpectedDateOfPurchase(
							row.get("expected_date_of_purchase") != null ? (String) row.get("expected_date_of_purchase")
									: null);
					enquiry.setEnquiryStatus(
							row.get("enquiry_status") != null ? (String) row.get("enquiry_status") : null);
					enquiry.setSourceOfEnquiry(
							row.get("source_of_enq") != null ? (String) row.get("source_of_enq") : null);
					enquiry.setIsenqValidated(
							row.get("Isenq_Validated") != null ? (String) row.get("Isenq_Validated") : null);
					enquiry.setExpectedDateOfPurchase(
							row.get("expected_date_of_purchase") != null ? (String) row.get("expected_date_of_purchase")
									: null);

					String prospecType = (String) row.get("prospect_type");
//					if(prospecType!=null)
//					{
//						if(prospecType.equalsIgnoreCase("PROSPECT"))
//						{
//							prospecType="COLD";
//						}
//					}

					enquiry.setProspectType(prospecType != null ? prospecType : null);
					enquiry.setEnquiryStatus(
							row.get("enquiry_status") != null ? (String) row.get("enquiry_status") : null);
					enquiry.setSourceOfEnquiry(
							row.get("source_of_enq") != null ? (String) row.get("source_of_enq") : null);

					enquiry.setSubSource(row.get("SubSource") != null ? (String) row.get("SubSource") : null);
					enquiry.setTerritoryManager(
							row.get("VstExecutive") != null ? (String) row.get("VstExecutive") : null);
					String salesMan = (String) row.get("Sales_name");
					// salesMan=salesMan+"(DSP)";
					enquiry.setSalesman(salesMan != null ? salesMan : null);
					enquiry.setCustomerName(
							row.get("customer_name") != null ? (String) row.get("customer_name") : null);
					enquiry.setMobileNumber(row.get("mobile_no") != null ? (String) row.get("mobile_no") : null);
					enquiry.setVillage(row.get("village") != null ? (String) row.get("village") : null);
					enquiry.setTahsil(row.get("tehsil") != null ? (String) row.get("tehsil") : null);
					enquiry.setDistrict(row.get("district") != null ? (String) row.get("district") : null);
					enquiry.setState(row.get("state") != null ? (String) row.get("state") : null);
					enquiry.setEnqStage(row.get("enq_stage") != null ? (String) row.get("enq_stage") : null);
					enquiry.setNextFollowUpDate(
							row.get("next_follow_up_date") != null ? (String) row.get("next_follow_up_date") : null);
					enquiry.setCash(row.get("Cash") != null ? (String) row.get("Cash") : null);
					enquiry.setRemarks(row.get("REMARKS") != null ? (String) row.get("REMARKS") : null);
					enquiry.setTotalRecords((Integer)row.get("totalRecords"));
					response.add(enquiry);
					finalresponse.setTotalRecords((Integer) row.get("totalRecords"));
					
					

				}
				//EnquiryReport2Response finalresponse;
				finalresponse.setEnquiryList(response);
				
			}

		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		}
		catch(HibernateException e)
		{
			
			e.printStackTrace();
			//System.out.println("message we got "+e.getMessage());
		}
		finally {

			if (session != null) {
				session.close();
			}
		}
		//System.out.println("before send main response "+response +prospectType);
		
		return finalresponse;

	}

	@Override
	public JasperPrint ExcelGeneratorReport(HttpServletRequest request, String jaspername,
			HashMap<String, Object> jasperParameter, String filePath) {

		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {

			String filePaths = filePath + jaspername;

			connection = dataSourceConnection.getConnection();

			if (connection != null) {
				jasperPrint = JasperFillManager.fillReport(filePaths, jasperParameter, connection);

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

	@Override
	public List<SalesManModel> fetchSalesManList(String userCode,String dealerId) {

		Session session = null;
		SQLQuery query = null;
		
		if ("null".equals(dealerId)) {
	        dealerId = null;
	    }
		

		
		List<SalesManModel> responseList = null;
		SalesManModel response = null;
		String sqlQuery = "exec [SP_getSalesmanDealerWise] :userCode,:dealerId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", dealerId);


			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SalesManModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SalesManModel();
					BigInteger empId = (BigInteger) row.get("emp_id");
					String salesManName = (String) row.get("fullName");
					response.setEmpId(empId != null ? empId : null);
					response.setSalesManName(salesManName != null ? salesManName : null);
					//logger.info("response "+response );
					responseList.add(response);

				}
			}
		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return responseList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<SalesEnquiryReportResponse> fetchSalesEnquiryList(String userCode,
			SalesEnquiryReportRequest requestModel) {

		if (logger.isDebugEnabled()) {
			logger.debug(
					"fetchSalesEnquiryList invoked.." + userCode + "::::::::::requestModel::::::::::" + requestModel);
		}
		Query query = null;
		Session session = null;
		SalesEnquiryReportResponse enquiry = null;

		List<SalesEnquiryReportResponse> responseModelList = null;

		String sqlQuery = " EXEC SP_GET_SALE_RPEORT :USERCODE,:FROMDATE,:TODATE,:DEALERID, :BRANCHID, :MODEL,:STATEID, :PCID, :CLUSTER";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("USERCODE", userCode);
			query.setParameter("FROMDATE", requestModel.getFromDate() != null ? requestModel.getFromDate() : null);
			query.setParameter("TODATE", requestModel.getToDate() != null ? requestModel.getToDate() : null);
			query.setParameter("DEALERID", requestModel.getDealerId() != null ? requestModel.getDealerId() : null);
			query.setParameter("BRANCHID", requestModel.getBranchId() != null ? requestModel.getBranchId() : null);
			query.setParameter("MODEL", requestModel.getModelIds() != null ? requestModel.getModelIds() : null);
			query.setParameter("STATEID", requestModel.getStateId() != null ? requestModel.getStateId() : null);
			query.setParameter("PCID", requestModel.getProfitCenterId() != null ? requestModel.getProfitCenterId() : null);
			query.setParameter("CLUSTER", requestModel.getClusterId() != null ? requestModel.getClusterId() : null);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List data = query.list();
			if (data != null && !data.isEmpty()) {
				enquiry = new SalesEnquiryReportResponse();
				responseModelList = new ArrayList<>();
				SalesEnquiryReportResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					enquiry = new SalesEnquiryReportResponse();
					enquiry.setStateId((BigInteger) row.get("state_id"));
					enquiry.setDealerLocation((String) row.get("DealerName"));
					enquiry.setDealerLocation((String) row.get("DealerLocation"));
					enquiry.setState((String) row.get("StateDesc"));
					enquiry.setCluster((String) row.get("CLUSTER"));
					enquiry.setTm((String) row.get("TM"));
					enquiry.setDealerdship((String) row.get("ParentDealerName"));
					enquiry.setDealerLocation((String) row.get("ParentDealerLocation"));
					enquiry.setModel((String) row.get("MODEL_NAME"));
					enquiry.setItemNumber((String) row.get("ITEMNUMBER"));
					enquiry.setMonthOpeningStock((Integer) row.get("Month_Opening_Stock"));
					enquiry.setMonthBilling((Integer) row.get("MONTH_BILLING"));
					enquiry.setMonthDelivery((Integer) row.get("MONTH_DELIVERY"));
					enquiry.setMonthClosingStock((BigDecimal) row.get("MONTH_CLOSING_STOCK"));
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
				// session.flush();
			}
		}
		return responseModelList;
	}

	@Override
	public List<String> searchDocumentNo(String enquiryNo, String userCode) {
		List<String> searchList = null;
		Session session = null;
		String SqlQuery = null;
		Query query = null;
		String sqlQuery = "exec [ENQUIRY_NO_SEARCH] :SearchText,:UserCode";

		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("SearchText", enquiryNo);
			query.setParameter("UserCode", userCode);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				searchList = new ArrayList<>();
				for (Object object : data) {
					Map row = (Map) object;
					String documentNo = (String) row.get("enquiry_number");
//					model.setDocumentNo(documentNo!=null?documentNo:"");
					searchList.add(documentNo);

				}
			}

		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);

		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);

		} catch (Exception exp) {

		} finally {
			if (session != null) {
				session.close();
			}
		}

		return searchList;

	}

}
