package com.hitech.dms.web.model.invoice;

import java.util.Date;

import lombok.Data;

@Data
public class InvoiceSearchRequestDto {

	private String invoiceNo;
	private String wcrNo;
	private String fromDate;
	private String toDate;
	private Integer page;
	private Integer size;
}
