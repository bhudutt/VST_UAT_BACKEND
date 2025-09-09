package com.hitech.dms.web.model.spare.sale.invoice.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;

import lombok.Data;

@Data
public class SalesInvoiceReportRequest {

	private String invoiceNumber;
	private String fromDate; 
	private String toDate;
	private Integer page;
	private Integer size;
	private Integer pcId;
	private Integer hoId;
	private Integer zoneId;
	private Integer stateId;
	private Integer territoryId;

}
