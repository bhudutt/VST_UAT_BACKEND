package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SpareInvoiceReturnHdrModel {
	
	@JsonProperty(value = "grnDate")
	private String grnDate;
	@JsonProperty(value = "grnFrom")
	private String grnFrom;
	private Integer id;
	private BigInteger branchId;
	@JsonProperty(value = "grnNo")
	private String grnNumber;
	private String spareInvoiceReturnNo;
	@JsonProperty(value = "claimTypeId")
	private String claimId;
	
	private Integer recieptQuantity;
	@JsonProperty(value = "grnId")
	private String grnId;
	@JsonProperty(value = "invoiceDate")
	private String invoiceDate;
	@JsonProperty(value = "invoiceNo")
	private String invoiceNo;
	@JsonProperty(value = "spareSaleReturnInvoiceNo")
	private String spareSaleReturnInvoiceNo;
	@JsonProperty(value = "spareSaleReturnInvoiceDate")
	private String spareSaleReturnInvoiceDate;
	@JsonProperty(value = "dealerId")
	private int dealerId;
	@JsonProperty(value = "returnType")
	private String returnTypeId;
	private String dealerCode;
	private BigInteger partyId;
	//private String claimId;
	//private String grnId;
	@JsonProperty(value = "basicAmount")
	private BigDecimal BasicAmount;
	
	@JsonProperty(value = "gstAmount")
	private BigDecimal gstAmount;
	
	@JsonProperty(value = "toatlAmount")
	private BigDecimal totalAmount;
	private BigInteger stockBinId;
	private Integer  branchStoreId;
	private String createdBy;
	private String createdDate;
	private String modifiedBy;
	private String modifiedDate;
	private String invoiceReturnStatus;
	
	//added for VST without Reference
	
	private String vstType;
	
	

}
