package com.hitech.dms.web.model.jobcard.billing.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class JobBillingPLORequestModel {

	private Integer branchId;
	private String discountFor;
	private Integer partORLbrID;
	private Integer partybranchId;
	private Integer customerId;
	private Integer stateId;
	private BigDecimal saleamount;
	private BigDecimal discount;
	
}
