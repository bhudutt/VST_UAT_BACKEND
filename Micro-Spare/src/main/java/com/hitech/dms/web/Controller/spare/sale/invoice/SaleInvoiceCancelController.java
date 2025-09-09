package com.hitech.dms.web.Controller.spare.sale.invoice;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.api.response.MessageCodeResponse;
import com.hitech.dms.app.config.ConnectionConfiguration;
import com.hitech.dms.web.model.spare.sale.invoice.request.SaleInvoiceCancelRequest;
import com.hitech.dms.web.model.spare.sale.invoice.response.SpareSalesInvoiceResponse;
import com.hitech.dms.web.service.sale.invoice.SpareSaleInvoiceService;
import com.hitech.dms.web.service.sale.invoiceCancel.SpareSaleInvoiceCancelService;

@RestController
@RequestMapping("api/v1/spareSaleInvoice/cancel")
public class SaleInvoiceCancelController {
	@Value("${file.upload-dir.SaleInvoice}")
    private String downloadPath;
	
	@Autowired
	SpareSaleInvoiceCancelService spareSaleInvoiceCancelService;
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	}
	
	@PostMapping("/invoice")
	public ResponseEntity<?> cancelInvoice(@RequestBody() SaleInvoiceCancelRequest saleInvoiceCancelRequest,
			OAuth2Authentication authentication,
			Device device, HttpServletRequest request) {
		String userCode = null;
		if (authentication != null) {
			userCode = authentication.getUserAuthentication().getName();
		}
		HeaderResponse userAuthResponse = new HeaderResponse();
		MessageCodeResponse codeResponse = new MessageCodeResponse();
		SimpleDateFormat formatter = getSimpleDateFormat();
		SpareSalesInvoiceResponse spareSalesInvoiceResponse = spareSaleInvoiceCancelService
				.cancelInvoice(saleInvoiceCancelRequest, userCode);
		if (spareSalesInvoiceResponse != null) {
			codeResponse.setCode("EC200");
			codeResponse.setDescription("Invoice Number Cancelled on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Number Cancelled Successfully");
		} else {
			codeResponse.setCode("EC500");
			codeResponse.setDescription("Unsuccessful on " + formatter.format(new Date()));
			codeResponse.setMessage("Invoice Number Cancelled or server side error.");
		}
		userAuthResponse.setResponseCode(codeResponse);
		userAuthResponse.setResponseData(spareSalesInvoiceResponse);
		return ResponseEntity.ok(userAuthResponse);
	}
}
