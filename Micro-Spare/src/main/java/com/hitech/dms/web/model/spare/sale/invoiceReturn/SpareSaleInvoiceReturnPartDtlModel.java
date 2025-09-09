package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hitech.dms.web.model.spara.delivery.challan.request.BinDetailRequest;

import lombok.Data;

@Data
public class SpareSaleInvoiceReturnPartDtlModel {
	
	private BigDecimal accessableAmount;
	private String claimDate;
	private Integer claimQty;
	private String claimType;
	private BigDecimal discount;
	private BigDecimal gstValue;
	private Integer invoiceQty;
	private String partDescription;
	private String partNo;
	@JsonProperty(value="recieptQty")
	private BigDecimal recieptQty;
	private String remarks;
	private BigInteger partBranchId;
	private BigInteger stockBinId;
	private Integer branchStoreId;
	private BigDecimal totalValue;
	private BigDecimal unitPrice;
	private Integer invoiceReturnHdrId;
	private Integer partId;
	private String createdBy;
	private String createdDate;
	private String modifiedBy;
	private String modifiedDate;
	private BigDecimal vorCharge;
	
	
	//added for VST without Reference
	private String stockBinListId;
	private Integer returnQty;
	private BigDecimal gstAmount;
	private BigDecimal gstPercentage;
	private BigInteger branchId;
	
	private BigDecimal basicUnitPrice;
	private BigDecimal totalAmount;
	@JsonProperty(value="binPayload")
	private Map<String, BinDetailRequest> binDetailList = new HashMap<>();
	
	
	
	
	
	
	

}
