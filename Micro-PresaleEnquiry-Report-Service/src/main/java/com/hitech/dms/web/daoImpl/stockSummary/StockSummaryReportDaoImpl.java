package com.hitech.dms.web.daoImpl.stockSummary;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.web.dao.stockSummary.StockSummaryDao;
import com.hitech.dms.web.model.enquiry.export.EnquiryListRequestModel;
import com.hitech.dms.web.model.stockSummary.DeliverySummaryRequest;
import com.hitech.dms.web.model.stockSummary.DeliverySummaryResponse;
import com.hitech.dms.web.model.stockSummary.StockReportResponse;
import com.hitech.dms.web.model.stockSummary.StockSummaryResponse;
import com.hitech.dms.web.model.stockSummary.deliveryReportResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Repository
public class StockSummaryReportDaoImpl implements StockSummaryDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy/MM/dd");
	}
	
	private static final Logger logger=LoggerFactory.getLogger(StockSummaryReportDaoImpl.class);
	@Override
	public DeliverySummaryResponse deliverySummaryReportDao(DeliverySummaryRequest request, String userCode) {
		SimpleDateFormat smdf = getSimpleDateFormat();
//		String asOnDate = smdf.format(request.getAsOnDate());
//		System.out.println("asOnDate "+asOnDate);
		DeliverySummaryResponse response = new DeliverySummaryResponse();
		List<deliveryReportResponse> listResponse= null;
		logger.info("stockSummaryReportDao {{}"+userCode +request);
		
		Session session = null;
		Integer totalRecords=0;
		Integer totalDeliveryMtd=0;
		Integer totalDeliveryYtd=0;
		Query query = null;
		String sqlQuery=null;
		try
		{
			session = sessionFactory.openSession();
			sqlQuery="exec [SP_Delivery_Summary_Report] :userCode,:stateId,:dealerId,"
					+ ":branchId,:asOnDate,:profitCenterId,:orgHierID,:modelId,:itemNumber,:includeInactive,"
					+ ":page,:size";
			
			 query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode",userCode);
			query.setParameter("stateId", request.getStateId());
			query.setParameter("dealerId", request.getDealerId());
			query.setParameter("branchId", request.getBranchId());
			query.setParameter("asOnDate", request.getAsOnDate());
			query.setParameter("profitCenterId", request.getPcId());
			query.setParameter("orgHierID", request.getOrgHierId());
			query.setParameter("modelId", request.getModelId());
			query.setParameter("itemNumber", request.getItemNo());
			query.setParameter("includeInactive", "N");
			query.setParameter("page", request.getPage());
			query.setParameter("size", request.getSize());


			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			listResponse= new ArrayList<>();
			if (data != null && !data.isEmpty()) {
				for (Object object1 : data) {
					Map row = (Map) object1;
					deliveryReportResponse res= new deliveryReportResponse();
					res.setZone((String)row.get("ZONE"));
					res.setState((String)row.get("state"));
					res.setTerritory((String)row.get("territory"));
					String dealerShip=(String)row.get("dealership");
					String parentDealerLocation=(String)row.get("ParentDealerLocation");
					String  displayName=dealerShip+"-"+parentDealerLocation;
					
					res.setDealerShip(displayName);

					
					res.setDealerCode((String)row.get("ParentDealerCode"));
					//res.setParentDealerLocation((String)row.get("ParentDealerLocation"));

					res.setModel((String)row.get("model"));
					res.setItemNo((String)row.get("item_no"));
					res.setItemDescription((String)row.get("item_description"));
					res.setProfitCenter((String)row.get("Profitcenter"));
					///res.setStockQuantity((Integer)row.get("stock_qty"));
					res.setDeliveryMtd((Integer) row.get("Delivery_MTD"));
					res.setDeliveryYtd((Integer) row.get("Delivery_YTD"));

					totalRecords=(Integer)row.get("totalRecords");
					
					totalDeliveryMtd=(Integer)row.get("MONTHDELIVERY");
					totalDeliveryYtd=(Integer)row.get("DELIVERYCOUNT");
					
					
					
					listResponse.add(res);
					
					
					}
				response.setTotalRecord(totalRecords);
				response.setTotalDeliveryMtd(totalDeliveryMtd);
				response.setTotalDeliveryYtd(totalDeliveryYtd);
			}
			
		} catch (SQLGrammarException exp) {
			response.setStatusCode(304);
			response.setStatusMessage(exp.getMessage());
			
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			response.setStatusCode(304);
			response.setStatusMessage(exp.getMessage());
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			response.setStatusCode(304);
			response.setStatusMessage(exp.getMessage());
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		response.setReportList(listResponse);
		
		return response;
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
	public StockSummaryResponse stockSummaryReportDao(DeliverySummaryRequest request, String userCode) {
		SimpleDateFormat smdf = getSimpleDateFormat();
//		String asOnDate = smdf.format(request.getAsOnDate());
//		System.out.println("asOnDate "+asOnDate);
		StockSummaryResponse response = new StockSummaryResponse();
		List<StockReportResponse> listResponse= null;
		logger.info("stockSummaryReportDao {{}"+userCode +request);
		
		Session session = null;
		Query query = null;
		Integer totalRecord=0;
		Integer totalStockQnty=0;
		String sqlQuery=null;
		try
		{
			
			session = sessionFactory.openSession();
			sqlQuery="exec [SP_Stock_Summary_Report] :userCode,:stateId,:dealerId,"
					+ ":branchId,:asOnDate,:profitCenterId,:orgHierID,:modelId,:itemNumber,:includeInactive,"
					+ ":page,:size";
			
			 query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode",userCode);
			query.setParameter("stateId", request.getStateId());
			query.setParameter("dealerId", request.getDealerId());
			query.setParameter("branchId", request.getBranchId());
			query.setParameter("asOnDate", request.getAsOnDate());
			query.setParameter("profitCenterId", request.getPcId());
			query.setParameter("orgHierID", request.getOrgHierId());
			query.setParameter("modelId", request.getModelId());
			query.setParameter("itemNumber", request.getItemNo());
			query.setParameter("includeInactive", "N");
			query.setParameter("page", request.getPage());
			query.setParameter("size", request.getSize());


			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			listResponse= new ArrayList<>();
			if (data != null && !data.isEmpty()) {
				for (Object object1 : data) {
					Map row = (Map) object1;
					StockReportResponse res= new StockReportResponse();
					res.setZone((String)row.get("ZONE"));
					res.setState((String)row.get("state"));
					res.setTerritory((String)row.get("territory"));
					String dealerShip=(String)row.get("dealership");
					
					
					res.setDealerCode((String)row.get("ParentDealerCode"));
					String location=(String)row.get("ParentDealerLocation");
					String dealerWithLocation=dealerShip+"-"+location;
					res.setDealerShip(dealerWithLocation);
					//res.setParentDealerLocation((String)row.get("ParentDealerLocation"));

					res.setModel((String)row.get("model"));
					res.setItemNo((String)row.get("item_no"));
					res.setItemDescription((String)row.get("item_description"));
					res.setProfitCenter((String)row.get("Profitcenter"));
					res.setStockQuantity((Integer)row.get("stock_qty"));
					

					totalRecord=(Integer)row.get("totalRecords");
					totalStockQnty=(Integer)row.get("totalStockQnty");
					
					listResponse.add(res);
					
					
					}
				
				response.setTotalRecord(totalRecord);
				response.setTotalStockQnty(totalStockQnty);
			}
			
		} catch (SQLGrammarException exp) {
			response.setStatusCode(304);
			response.setStatusMessage(exp.getMessage());
			
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			response.setStatusCode(304);
			response.setStatusMessage(exp.getMessage());
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			response.setStatusCode(304);
			response.setStatusMessage(exp.getMessage());
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		response.setStockSummaryList(listResponse);
		
		return response;

	}
	@Override
	public JasperPrint ExcelGeneratorReportStockSummary(HttpServletRequest request, String jsaperName,
			HashMap<String, Object> jasperParameter, String filePath) {
		
		
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {

			String filePaths = filePath + jsaperName;

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
	public void printReportStockSummary(JasperPrint jasperPrint, String format, String printStatus,
			OutputStream outputStream, String reportName) throws JRException {
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

	
	

	

}
