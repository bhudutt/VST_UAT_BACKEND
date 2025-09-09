/**
 * 
 */
package com.hitech.dms.web.service.activity.gstclaim;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.activity.gstclaim.ActivityGstClaimDao;
import com.hitech.dms.web.entity.activityGstClaim.ActivityGstClaimInvoiceApprovalEntity;
import com.hitech.dms.web.entity.activityGstClaim.ActivityGstClaimInvoiceHdrEntity;
import com.hitech.dms.web.model.activity.claim.response.ActivityClaimCreateResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimGstInvSearchRequest;
import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimInvApprovalRequestModel;
import com.hitech.dms.web.model.activity.gstclaim.request.ActivityClaimInvoiceRequestModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimGstInvSearchResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimGstInvoiceResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityClaimInvApprovalResponseModel;
import com.hitech.dms.web.model.activity.gstclaim.response.ActivityCreditNoteResponseModel;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * @author santosh.kumar
 *
 */
@Service
public class ActivityGstClaimServiceImpl implements ActivityGstClaimService{
	private static final Logger logger = LoggerFactory.getLogger(ActivityGstClaimServiceImpl.class);
	
	@Autowired
	private ActivityGstClaimDao activityGstClaimDao;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private ConnectionConfiguration dataSourceConnection;

	@Override
	public Map<String, Object> getActivityClaimNoList(String userCode, Device device) {
		return activityGstClaimDao.fetchActivityGstClaimList(userCode) ;
	}

	@Override
	public Map<String, Object> getActivityClaimDetailsById(String userCode, Device device, Integer activityClaimId) {
		Map<String, Object> finalResponse = new HashMap<>();
		Map<String, Object> headerResponse = activityGstClaimDao.fetchActivityHeader(userCode, activityClaimId);
		Map<String, Object> detailsResponse = activityGstClaimDao.fetchActivityClaimDetails(userCode, activityClaimId);
		finalResponse.put("header", headerResponse.get("Header"));
		finalResponse.put("details", detailsResponse.get("Details"));
		return finalResponse;
	}

	@Override
	public ActivityClaimGstInvoiceResponseModel createActivityGstClaimInvoice(String userCode,
			@Valid ActivityClaimInvoiceRequestModel requestedData, Device device) {
		
		ActivityClaimGstInvoiceResponseModel response = new ActivityClaimGstInvoiceResponseModel();
		try {
			System.out.println("requestedData.getActivityPlanHdrId()"+requestedData.getActivityPlanHdrId());
		  if(requestedData.getFinalSubmitFlag() !=null && requestedData.getFinalSubmitFlag().equalsIgnoreCase("Y")) {
			  return activityGstClaimDao.updateActivityGstClaimInvoice(userCode,requestedData,device);
			}else {
				Map<String, Object> activityClaimGstInvoiceMap = activityGstClaimDao.fetchActivityClaimGstInvByPlanId(requestedData.getActivityPlanHdrId());
				if (activityClaimGstInvoiceMap == null || activityClaimGstInvoiceMap.isEmpty()) {
		        	return activityGstClaimDao.createActivityGstClaimInvoice(userCode,requestedData,device);	 
		        } else {
		        	 Object activityGstClaiminvNumber = activityClaimGstInvoiceMap.values().iterator().next(); 
		                response.setActivityClaimGstInvoiceNumber(activityGstClaiminvNumber.toString());
		                response.setStatusCode(500);
		                response.setMsg("claim gst invoice already generated for this plan number");
			            
		        }
			}
	         
		}catch(Exception e) {
			e.printStackTrace();
		}
			return response;
	}

	@Override
	public List<ActivityClaimGstInvSearchResponseModel> fetchActivityClaimGstInvSearchList(String userCode,
			ActivityClaimGstInvSearchRequest requestModel) {
		return activityGstClaimDao.fetchActivityClaimGstInvSearchList(userCode,requestModel);
	}

	@Override
	public Map<String, Object> getActivityGstClaimInvoiceDetailsById(String userCode, Device device,
			Integer activityClaimGstinvoiceId) {
		Map<String, Object> finalResponse = new HashMap<>();
		Map<String, Object> headerResponse = activityGstClaimDao.fetchActivityClaimGstHeader(userCode, activityClaimGstinvoiceId);
		Map<String, Object> detailsResponse = activityGstClaimDao.fetchActivityClaimGstDetails(userCode, activityClaimGstinvoiceId);
		finalResponse.put("header", headerResponse.get("Header"));
		finalResponse.put("details", detailsResponse.get("Details"));
		return finalResponse;
	}

	@Override
	public ActivityClaimInvApprovalResponseModel approveRejectActivityClaimGstInvoice(String userCode,
			ActivityClaimInvApprovalRequestModel requestModel, Device device) {
		return activityGstClaimDao.approveRejectActivityClaimGstInvoice(userCode,requestModel);
	}

	@Override
	public List<ActivityCreditNoteResponseModel> fetchActivityCreditNoteSearchList(String userCode,
			ActivityClaimGstInvSearchRequest requestModel) {
		return activityGstClaimDao.fetchActivityCreditNoteSearchList(userCode,requestModel);
	}

	@Override
	public Map<String, Object> getActivityClaimGstInvoiceList(String userCode, Device device) {
		return activityGstClaimDao.fetchActivityClaimGstInvoiceList(userCode) ;
	}

	@Override
	public JasperPrint PdfGeneratorReport(HttpServletRequest request, String jasperName,
			HashMap<String, Object> jasperParameter, String filePath) {
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
			// String filePath =
			// ResourceUtils.getFile("classpath:reports/"+jaspername).getAbsolutePath();

			String filePathsVariable = filePath + jasperName;
			System.out.println("filePath  " + filePathsVariable);
			connection = dataSourceConnection.getConnection();

			if (connection != null) {
				jasperPrint = JasperFillManager.fillReport(filePathsVariable, jasperParameter, connection);
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
			String reportName) throws Exception {

		if (format != null && format.equalsIgnoreCase("pdf")) {
			JRPdfExporter exporter = new JRPdfExporter();

			exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			if (printStatus != null && printStatus.equals("true")) {
				configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
				configuration.setPdfJavaScript("this.print();");
			}
			exporter.setConfiguration(configuration);
			exporter.exportReport();

		} else if (format != null && format.equalsIgnoreCase("xls")) {

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
	public Map<String, Object> getPlantCodeList(String userCode, Device device) {
		return activityGstClaimDao.getPlantCodeList(userCode) ;
	}


	
}
