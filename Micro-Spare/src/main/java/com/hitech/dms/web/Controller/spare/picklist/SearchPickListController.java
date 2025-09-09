package com.hitech.dms.web.Controller.spare.picklist;

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
import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PicklistRequest;
import com.hitech.dms.web.model.spara.customer.order.picklist.response.SpareCustOrderForPickListResponse;
import com.hitech.dms.web.model.spara.customer.order.response.CustomerOrderNumberResponse;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.grn.mapping.request.SpareGrnSearchRequest;
import com.hitech.dms.web.model.spare.grn.mapping.response.InvoiceNumberResponse;
import com.hitech.dms.web.service.spare.picklist.PickListService;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("api/v1/pickList")
public class SearchPickListController {

	@Value("${file.upload-dir.PickList}")
    private String downloadPath;
	
	@Autowired
	PickListService pickListService;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@GetMapping("/searchPickListNumber")
	public ResponseEntity<?> searchPickListNumber(@RequestParam() String searchText, 
			@RequestParam() int counterSaleId,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		HashMap<BigInteger, String> pickList = pickListService
				.searchPickListNumber(searchText, counterSaleId, userCode);
		if (pickList != null && !pickList.isEmpty()) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Pick List Number on " + formatter.format(new Date()));
			codeResponse.setMessage("Pick List Number  List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Pick List Number  Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(pickList);
		return ResponseEntity.ok(userAuthResponse);
	}

	@GetMapping("/search")
	public ResponseEntity<?> fetchPickListDetails(@RequestParam() String picklistNumber,
			@RequestParam() String coNo, @RequestParam() String poNumber, @RequestParam() Date fromDate, 
			@RequestParam() Date toDate, @RequestParam() Integer page, 
			@RequestParam() Integer size, OAuth2Authentication authentication,
			Device device, HttpServletRequest request) throws ParseException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		ApiResponse<?> response = pickListService
				.fetchPickListDetails(picklistNumber, coNo, poNumber, fromDate, toDate, userCode, page, size);
		if (response != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Search List Successfully fetched");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Search List Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(response);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
	@GetMapping("/searchPickListNo")
	public ResponseEntity<?> searchPickList(@RequestParam() String searchText,
			@RequestParam() String searchFor, 
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) throws ParseException {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		List<String> pickLists=this.pickListService.getAllSearchPickListst(searchText,userCode);
		if (pickLists != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Search List on " + formatter.format(new Date()));
			codeResponse.setMessage("Search PickList Successfully ");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Search PickList Not Fetched or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(pickLists);
		return ResponseEntity.ok(userAuthResponse);
	}
	
	
	
}
