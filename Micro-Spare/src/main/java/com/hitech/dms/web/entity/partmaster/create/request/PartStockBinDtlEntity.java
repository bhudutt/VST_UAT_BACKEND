package com.hitech.dms.web.entity.partmaster.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Table(name = "PA_STOCK_BIN_DTL")
@Data
public class PartStockBinDtlEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_bin_dtl_id")
	private BigInteger stockBinDtlId;

	@Column(name = "branch_id")
	private BigInteger branchId;

	@Column(name = "partBranch_id")
	private BigInteger partBranchId;

	@Column(name = "stock_store_id")
	private BigInteger stockStoreId;

	@Column(name = "stock_bin_id")
	private BigInteger stockBinId;
	
	@Column(name = "document_id")
	private BigInteger documentId;

	@Column(name = "TABLE_NAME")
	private String tableName;

	@Column(name = "ref_doc_hdr_id")
	private BigInteger refDocHdrId;

	@Column(name = "TransactionDate")
	private Date transactionDate;

	@Column(name = "IssueQty")
	private BigDecimal issueQty;

	@Column(name = "ReceiptQty")
	private BigDecimal receiptQty;

	@Column(name = "AvlbQty")
	private BigDecimal avlbQty;
	
	@Column(name = "NDP")
	private BigDecimal ndp;

	@Column(name = "MRP")
	private BigDecimal mrp;

	@Column(name = "GNDP")
	private BigDecimal gndp;

	@Column(name = "DiscountValue")
	private BigDecimal discountValue;
	
	@Column(name = "ExciseValue")
	private BigDecimal exciseValue;
	
	@Column(name = "TaxValue")
	private BigDecimal taxValue;
	
	@Column(name = "GrossValue")
	private BigDecimal grossValue;
	
	@Column(name = "LandedCost")
	private BigDecimal landedCost;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Column(name = "CGST")
	private BigDecimal cgst;
	
	@Column(name = "SGST")
	private BigDecimal sgst;
	
	@Column(name = "IGST")
	private BigDecimal igst;

	@Column(name = "Version_No")
	private Integer versionNo;
}
