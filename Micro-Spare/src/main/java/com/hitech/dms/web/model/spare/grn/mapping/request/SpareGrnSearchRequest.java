package com.hitech.dms.web.model.spare.grn.mapping.request;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestParam;

import lombok.Data;

@Data
public class SpareGrnSearchRequest {

	private String grnNumber;
	private String invoiceNo;
	private String claimNumber;
	private String claimType;
	private String inventoryNumber;
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
