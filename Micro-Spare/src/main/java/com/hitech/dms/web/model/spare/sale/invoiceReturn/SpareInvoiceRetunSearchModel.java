package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({"action","invoiceReturnNo","grnId","partyCode","partyName","status","branchName","claimId","claimDate","claimType","mrnDate","invoiceNo","invoiceDate","claimValue","branchId",})
@JsonIgnoreProperties({"statusMessage","statusCode"})
public class SpareInvoiceRetunSearchModel {
	
	private int Id;
//	private String partyCode;
	private String partyName;
	private String branchName;
	private Integer branchId;
	private String claimId;
	private String grnId;
	private String invoiceReturnNo;
	private String mrnDate;
	private String invoiceNo;
	private String invoiceDate;
	private String grnType;
	private String claimType;
	private String invoiceReturnDate;
	private String claimDate;
	private BigDecimal claimValue;
	private String claimRemarks;
	private String action;
	private String Status;
	private String agreeStatus;
	private String disagreeImage;
	private String statusMessage;
	private BigInteger partyId;
	private int statusCode;
	private String returnType;
	private BigInteger stockBinId;
	private Integer branchStoreId;
	private BigDecimal netAmountSum;
	private BigDecimal totalgstSum;
	private BigDecimal totalVorCharge;
	private BigDecimal totalAmount;
	
	

}
