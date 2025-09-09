package com.hitech.dms.web.model.spara.customer.order.picklist.request;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class PicklistRequest {
	
	private BigInteger customerOrderId;
	private BigInteger counterSaleId;
	private BigInteger branchId;
	private String status;
	private BigInteger poHdrId;
	private BigInteger referenceDocumentId;
	private String customerName;
	private String mobileNo;
	private BigInteger pinId;
	private List<PartDetailRequest> partDetails;

}