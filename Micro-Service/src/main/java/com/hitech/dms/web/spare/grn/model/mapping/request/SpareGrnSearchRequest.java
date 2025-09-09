package com.hitech.dms.web.spare.grn.model.mapping.request;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestParam;

import lombok.Data;

@Data
public class SpareGrnSearchRequest {

	private String grnNumber;
	private String invoiceNo;
	private String fromDate; 
	private String toDate; 
}
