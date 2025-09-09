package com.hitech.dms.web.spare.grn.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.persistence.ConnectionConfiguration;
import com.hitech.dms.web.spare.grn.dao.mapping.SpareGRNDao;
import com.hitech.dms.web.spare.grn.model.mapping.response.InvoiceNumberResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.PartNumberDetailResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.PartyCodeDetailResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnDetailsResponse;
import com.hitech.dms.web.spare.grn.model.mapping.request.SpareGrnRequest;
import com.hitech.dms.web.spare.grn.model.mapping.request.SpareGrnSearchRequest;
import com.hitech.dms.web.spare.grn.model.mapping.response.BinLocationListResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnFromResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnPODetailsReponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnPONumberReponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.SpareGrnResponse;
import com.hitech.dms.web.spare.grn.model.mapping.response.StoreResponse;
import com.hitech.dms.web.spare.party.model.mapping.request.DealerDistributorMappingRequest;
import com.hitech.dms.web.spare.party.model.mapping.response.DealerCodeSearchResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DealerDistributorMappingResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.DealerMappingHeaderResponse;
import com.hitech.dms.web.spare.party.model.mapping.response.PartyCodeSearchResponse;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("/spare/grn")
public class SpareGRNController {

	@Value("${file.upload-dir.SpareGrnSearch:C:\\VST-DMS-APPS\\template\\spare template\\\\}")
    private String spareGrnSearchdownloadPath;
	
	@Autowired
	private ConnectionConfiguration dataSourceConnection;
	
	@Autowired
	SpareGRNDao spareGRNDao;

	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}

	@GetMapping("/searchPoNumber")
	public ResponseEntity<?> searchPoNumber(@RequestParam() String searchText, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SpareGrnPONumberReponse> spareGrnPONumberReponseList = spareGRNDao.searchPONumber(searchText, userCode);
		if (spareGrnPONumberReponseList != null && !spareGrnPONumberReponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("PO Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("PO Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("PO Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnPONumberReponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchPoDetails")
	public ResponseEntity<?> fetchPoDetails(@RequestParam() BigInteger poHdrId, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		SpareGrnPODetailsReponse spareGrnPODetailsReponse = spareGRNDao.fetchPODetails(poHdrId);
		if (spareGrnPODetailsReponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Invoice Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Details Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnPODetailsReponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/getGrnFrom")
	public ResponseEntity<?> fetchGrnFrom(@RequestParam() int dealerId, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<SpareGrnFromResponse> spareGrnFromResponseList = spareGRNDao.fetchGrnFromList(dealerId, userCode);
		if (spareGrnFromResponseList != null && !spareGrnFromResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Header List on " + formatter.format(new Date()));
			codeResponse.setMessage("Header List Successfully fetched");
		} else {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Header List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnFromResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/searchPartyCode")
	public ResponseEntity<?> searchPartyCode(@RequestParam() String searchText, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<PartyCodeSearchResponse> partyCodeSearchResponseList = spareGRNDao.searchPartyCode(searchText, userCode);
		if (partyCodeSearchResponseList != null && !partyCodeSearchResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Code List on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Code List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Code List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partyCodeSearchResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchPartyCodeDetails")
	public ResponseEntity<?> fetchPartyCodeDetails(@RequestParam() BigInteger partyCategoryId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		PartyCodeDetailResponse partyCodeDetailResponse = spareGRNDao.fetchPartyDetails(partyCategoryId);
		if (partyCodeDetailResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Party Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Details Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Party Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partyCodeDetailResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/searchInvoiceNumber")
	public ResponseEntity<?> searchInvoiceNumber(@RequestParam() String searchText, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		List<InvoiceNumberResponse> invoiceNumberResponseList = spareGRNDao.searchInvoiceNumber(searchText, userCode);
		if (invoiceNumberResponseList != null && !invoiceNumberResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Invoice Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(invoiceNumberResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchInvoiceDetails")
	public ResponseEntity<?> fetchInvoiceDetails(@RequestParam() String InvoiceNo, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		InvoiceNumberResponse invoiceNumberResponse = spareGRNDao.fetchInvoiceDetails(InvoiceNo);
		if (invoiceNumberResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Invoice Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Details Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(invoiceNumberResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchStoreList")
	public ResponseEntity<?> fetchStoreList(OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		List<StoreResponse> storeResponseList = spareGRNDao.fetchStoreList(userCode);
		if (storeResponseList != null && !storeResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Header List on " + formatter.format(new Date()));
			codeResponse.setMessage("Header List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Header List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(storeResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/searchBinLocation")
	public ResponseEntity<?> searchBinLocation(@RequestParam() String searchText, @RequestParam() int branchStoreId,
			@RequestParam() String invoiceNo, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		List<BinLocationListResponse> binLocationListResponseList = spareGRNDao.searchBinLocation(searchText, userCode,
				branchStoreId, invoiceNo);
		if (binLocationListResponseList != null && !binLocationListResponseList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setMessage("Part Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(binLocationListResponseList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/getBinDetails")
	public ResponseEntity<?> fetchBinDetails(@RequestParam() String invoiceNo, int grnTypeId,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		List<PartNumberDetailResponse> partNumberDetailResponse = spareGRNDao.fetchBinDetails(invoiceNo, grnTypeId);
		if (partNumberDetailResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Part Details on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Details Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Part Details Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(partNumberDetailResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/createSpareGrn")
	public ResponseEntity<?> createSpareGrn(
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			@RequestBody SpareGrnRequest spareGrnRequest, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareGrnResponse spareGrnResponse = spareGRNDao.createSpareGrn(userCode, spareGrnRequest);
		if (spareGrnResponse.getStatusCode() == 201) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Spare GRN added on " + formatter.format(new Date()));
			codeResponse.setMessage(spareGrnResponse.getMsg());
		} else if (spareGrnResponse.getStatusCode() == 500) {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Spare GRN Added or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/generateGrnNumber")
	public ResponseEntity<?> generateGrnNumber(@RequestParam() Integer branchId,
			@RequestHeader(value = "Authorization", required = true) String authorizationHeader,
			OAuth2Authentication authentication, Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}

		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareGrnResponse spareGrnResponse = spareGRNDao.generateGrnNumber(branchId);
		if (spareGrnResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("GRN number generated on " + formatter.format(new Date()));
			codeResponse.setMessage(spareGrnResponse.getMsg());
		} else if (spareGrnResponse.getStatusCode() == 500) {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("GRN number generated or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/searchGrnOrInvoiceNumber")
	public ResponseEntity<?> searchGrnOrInvoiceNumber(@RequestParam() String searchType,
			@RequestParam() String searchText, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		System.out.println("searchType or searchtext "+searchType +" "+searchText);
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		List<String> grnNumberList = spareGRNDao.searchGrnOrInvoiceNumber(searchType, searchText);
		if (grnNumberList != null && !grnNumberList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Grn Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(grnNumberList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/searchInvoiceNumberByGrn")
	public ResponseEntity<?> searchInvoiceNumberByGrn(@RequestParam() String grnNumber,
			@RequestParam() String searchText, OAuth2Authentication authentication, Device device,
			HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		List<String> invoiceNumberList = spareGRNDao.searchInvoiceNumberByGrn(grnNumber, searchText);
		if (grnNumber != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Grn Number List on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Number List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Grn Number List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(invoiceNumberList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/fetchGrnDetails")
	public ResponseEntity<?> fetchGrnDetails(@RequestParam() String grnNumber, @RequestParam() String invoiceNo,
			@RequestParam() Date fromDate, @RequestParam() Date toDate, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) throws ParseException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		String lookupTypeCode = "GRN";
		List<SpareGrnDetailsResponse> spareGrnDetailsResponse = spareGRNDao.fetchGrnDetails(grnNumber, invoiceNo,
				fromDate, toDate);
		if (spareGrnDetailsResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareGrnDetailsResponse);
		return ResponseEntity.ok(userAuthResponse);
	}

	@PostMapping("/exportSpareGrnSearchDetail")
	public void exportSpareGrnSearchDetail(@RequestBody SpareGrnSearchRequest spareGrnSearchRequest,
			@RequestParam(defaultValue = "xls") String format, @RequestParam(required = false) String printStatus,
			OAuth2Authentication authentication, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		OutputStream outputStream = null;
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		String reportName = "Spare GRN Report";
		HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
		jasperParameter.put("grnNumber", spareGrnSearchRequest.getGrnNumber());
		jasperParameter.put("invoiceNo", spareGrnSearchRequest.getInvoiceNo());
		jasperParameter.put("FromDate", spareGrnSearchRequest.getFromDate());
		jasperParameter.put("ToDate", spareGrnSearchRequest.getToDate());

		String filePath = "";
		String property = System.getProperty("os.name");
		if (property.contains("Windows")) {
			filePath = spareGrnSearchdownloadPath;
		} else {
			filePath = "/var/VST-DMS-APPS/FILES/REPORTS/spare grn detail/";
		}
	

		JasperPrint jasperPrint = pdfGeneratorReport(request, 
				"SpareGrnDetail.jasper", jasperParameter, filePath);
		try {

			if (format != null && format.equalsIgnoreCase("pdf")) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=" + reportName + ".pdf");
			} else if (format != null && format.equalsIgnoreCase("xls")) {
				// response.setContentType("application/xlsx");
				response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xlsx");
				response.setContentType("application/vnd.ms-excel");
			}

			response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
			outputStream = response.getOutputStream();

			printReport(jasperPrint, format, printStatus, outputStream, reportName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				outputStream.flush();
				outputStream.close();
			}
		}
	}
	
	public JasperPrint pdfGeneratorReport(HttpServletRequest request, String jaspername,
			HashMap<String, Object> jasperParameter,String downloadPath) {
		//String filePath = request.getServletContext().getRealPath("/reports/" + jaspername);
		JasperPrint jasperPrint = null;
		Connection connection = null;
		try {
			//String filePath = ResourceUtils.getFile("classpath:reports/"+jaspername).getAbsolutePath();
			
			String filePath=downloadPath+jaspername;
			System.out.println("filePath  "+filePath);
			connection = dataSourceConnection.getConnection();

			if (connection != null) {
				jasperPrint = JasperFillManager.fillReport(filePath, jasperParameter, connection);
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
	
	public void printReport(JasperPrint jasperPrint, String format, String printStatus, OutputStream outputStream,String reportName)
			throws Exception {

		
		if(format !=null && format.equalsIgnoreCase("pdf")) {
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
		}else if(format != null && format.equalsIgnoreCase("xls")) {
			
			
			
			JRXlsxExporter exporter = new JRXlsxExporter();
			SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
	        reportConfigXLS.setSheetNames(new String[] { "sheet1" });
	        exporter.setConfiguration(reportConfigXLS);
	        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
	       
	        exporter.exportReport();
			
			
			
			/*
			 * JRXlsExporter xlsExporter = new JRXlsExporter();
			 * jasperPrint.setProperty("net.sf.jasperreports.export.xls.ignore.graphics",
			 * "false"); //
			 * jasperPrint.setProperty("net.sf.jasperreports.export.xls.sheet.name", //
			 * "true");
			 * 
			 * File file = new File(reportName + ".xls");
			 * 
			 * xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			 * 
			 * SimpleXlsReportConfiguration configuration = new
			 * SimpleXlsReportConfiguration(); configuration.setOnePagePerSheet(true);
			 * configuration.setDetectCellType(true);
			 * configuration.setCollapseRowSpan(false);
			 * configuration.setWhitePageBackground(false);
			 * configuration.setRemoveEmptySpaceBetweenColumns(true);
			 * configuration.setRemoveEmptySpaceBetweenRows(true);
			 * configuration.setIgnorePageMargins(true);
			 * 
			 * String sheetName[] = { reportName }; configuration.setSheetNames(sheetName);
			 * 
			 * xlsExporter.setConfiguration(configuration);
			 * xlsExporter.setExporterOutput(new
			 * SimpleOutputStreamExporterOutput(outputStream)); xlsExporter.exportReport();
			 */
		}
	}
}
