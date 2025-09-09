package com.hitech.dms.web.model.spare.sale.invoiceReturn;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
@JsonPropertyOrder({"mrnHdrId","partNo","branchId","partDescription","invoiceQty","currentStock","claimQty","claimtypeId","unitPrice",
	"discount","accessableAmount","gstValue","vorCharge","totalValue","recieptQty","claimType","recieptQty","claimType","claimDate","remarks"})
@Data
public class PartDetailsModel {
	
	private BigInteger mrnHdrId;
	private String partNo;
	private Integer branchId;
	private String partDescription;
	private BigDecimal invoiceQty;
	private BigDecimal claimQty;
	private BigInteger claimtypeId;
	private BigDecimal unitPrice;
	private BigDecimal discount;
	private Integer currentStock;
	private BigDecimal accessableAmount;
	private BigDecimal gstValue;
	private BigDecimal vorCharge;
	private BigDecimal totalValue;
	private BigDecimal recieptQty;
	private String claimType;
	private String claimDate;
	private String remarks;
	private Integer partId;
	private BigDecimal totalNetValue;
	private BigDecimal totalDiscountValue;
	private BigDecimal totalTaxValue;

	private BigDecimal totalBasicSum;
	private BigDecimal totalGstSum;
	private BigDecimal totalAllValue;
	
	

}
