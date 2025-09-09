package com.hitech.dms.web.entity.spare.sale.aprReturn;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "APR_RETURN_DTL")
@Data
public class AprReturnDtlEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APR_DETAIL_ID")
	private BigInteger aprDetailId;
	
	
	@ManyToOne
	@JoinColumn(name = "APR_RETURN_ID")
	private AprReturnHrdEntity aprReturnHrdEntity;
	
	@Column(name = "INVOICE_SALE_DETAIL_ID")
	private BigInteger invoiceSaleDetailId;
	
	@Column(name = "PART_BRANCH_ID")
	private BigInteger partBranchId;
	
	@Column(name = "PART_ID")
	private BigInteger partId;
	
	@Column(name = "STOCK_BIN_ID")
	private BigInteger stockBinId;
	
	@Column(name = "MRP")
	private BigDecimal mrp;
	
	@Column(name = "APR_RETURNED_QTY")
	private Integer aprReturnedQty;
	
	@Column(name = "APR_RETURN_QTY")
	private Integer aprReturnQty;
	
	@Column(name = "STORE")
	private String store;
	
	@Column(name = "INVOICE_QTY")
	private Integer invoiceQty;
	
	@Column(name = "DISCOUNT_RATE")
	private BigDecimal discountRate;
	
	@Column(name = "DISCOUNT_VALUE")
	private BigDecimal discountValue;
	
	@Column(name = "CGST_PER")
	private BigDecimal cgstPer;
	
	@Column(name = "IGST_PER")
	private BigDecimal igstPer;
	
	@Column(name = "SGST_PER")
	private BigDecimal sgstPer;
	
	@Column(name = "CGST_AMOUNT")
	private BigDecimal cgstAmount;
	
	@Column(name = "IGST_AMOUNT")
	private BigDecimal igstAmount;
	
	@Column(name = "SGST_AMOUNT")
	private BigDecimal sgstAmount;
	
	@Column(name = "ADDITIONAL_DISCOUNT_TYPE")
	private String additionalDiscountType;
	 
	@Column(name = "ADDITIONAL_DISCOUNT_RATE")
	private BigDecimal additionalDiscountRate;
	
	@Column(name = "ADDITIONAL_DISCOUNT_AMOUNT")
	private BigDecimal additionalDiscountAmount;
	
	@Column(name = "TAXABLE_AMOUNT")
	private BigDecimal taxableAmount;
	

}
