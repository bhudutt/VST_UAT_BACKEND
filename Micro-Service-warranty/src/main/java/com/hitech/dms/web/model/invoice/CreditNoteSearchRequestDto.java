package com.hitech.dms.web.model.invoice;

import lombok.Data;

@Data
public class CreditNoteSearchRequestDto {

	private String invoiceNo;
	private String wcrNo;
	private String fromDate;
	private String toDate;
	private Integer page;
	private Integer size;
}
