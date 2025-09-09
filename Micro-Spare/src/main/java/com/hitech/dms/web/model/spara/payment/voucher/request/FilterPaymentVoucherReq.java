package com.hitech.dms.web.model.spara.payment.voucher.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class FilterPaymentVoucherReq {
	
    private String profitCenter;
 	
	private String dealership;
	
	private Integer dealerId; 
	
	private Integer branchId;
	
	private Integer stateId;
	
	private Integer pcId;
	
	private Integer zoneId;
	
	private Integer territoryId;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date fromDate;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date toDate;
	
	private Integer voucherTypeId;
	
	private String paymentVoucherNo;
	
	private String pvDate;
	
	private Integer partyTypeId;
	
	private Integer partyCodeId;
	
	private Integer page;
	
	private Integer size;
	
}
