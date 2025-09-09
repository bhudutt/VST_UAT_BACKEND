package com.hitech.dms.web.entity.jobcard.billing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "SV_RO_BILL_HDR")
@Data
public class JobCardBillingHDREntity implements Serializable {
	private static final long serialVersionUID = 4101876059474057634L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ro_bill_id")
	private BigInteger roBillingId;
	@Column(name = "branch_id")
	private BigInteger branchId;
	@Column(name = "ro_id")
	private BigInteger roId;
	@Column(name = "DocType")
	private String docType;
	@Column(name = "DocNumber")
	private String docNumber;
	@Column(name = "DocDate")
	private Date docDate = new Date();
	@Column(name = "DocStatus")
	private String docStatus;
	@Column(name = "Insurance_Party_Id")
	private Integer insurancePartyId;
	@Column(name = "CategoryCode")
	private String categoryCode;
	@Column(name = "PartyCode")
	private String partyCode;
	@Column(name = "CustomerName")
	private String customerName;
	@Column(name = "PartyAddLine1")
	private String partyAddLine1;
	@Column(name = "PartyAddLine2")
	private String partyAddLine2;
	@Column(name = "PartyAddLine3")
	private String partyAddLine3;
	@Column(name = "city_id")
	private BigInteger cityId;
	@Column(name = "pin_id")
	private BigInteger pinId;
	@Column(name = "SaleType")
	private String saleType;
	@Column(name = "taxctgry_branch_id")
	private BigInteger taxctgrybranchid;
	@Column(name = "CCType")
	private String ccType;
	@Column(name = "CCNumber")
	private String ccNumber;
	@Column(name = "CCChargeType")
	private String ccChargeType;
	@Column(name = "CCChargeAmount")
	private BigDecimal ccChargeAmount;
	@Column(name = "P_DiscountType")
	private String pdiscountType;
	@Column(name = "P_DiscountRate")
	private BigDecimal ppiscountRate;
	@Column(name = "P_DiscountValue")
	private BigDecimal pdiscountValue;
	@Column(name = "P_BasicValue")
	private BigDecimal pbasicValue;
	@Column(name = "P_TaxValue")
	private BigDecimal ptaxValue;
	@Column(name = "P_BillValue")
	private BigDecimal pbillValue;
	@Column(name = "L_DiscountType")
	private String ldiscountType;
	@Column(name = "L_DiscountRate")
	private BigDecimal ldiscountRate;
	@Column(name = "L_DiscountValue")
	private BigDecimal ldiscountValue;
	@Column(name = "L_BasicValue")
	private BigDecimal lbasicValue;
	@Column(name = "L_TaxValue")
	private BigDecimal ltaxValue;
	@Column(name = "L_BillValue")
	private BigDecimal lbillValue;
	@Column(name = "RoundOffAmount")
	private BigDecimal roundOffAmount;
	@Column(name = "T_BasicValue")
	private BigDecimal tbasicValue;
	@Column(name = "T_DiscountValue")
	private BigDecimal tdiscountValue;
	@Column(name = "T_TaxValue")
	private BigDecimal ttaxValue;
	@Column(name = "T_BillValue")
	private BigDecimal tbillValue;
	@Column(name = "ReceivedAmount")
	private BigDecimal receivedAmount;
	@Column(name = "WriteOffAmount")
	private BigDecimal writeOffAmount;
	@Column(name = "Remarks")
	private String remarks;
	@Column(name = "BillTitle")
	private String billTitle;
	@Column(name = "voucher_id")
	private BigInteger voucherid;
	@Column(name = "VoucherDate")
	private Date voucherDate = new Date();
	@Column(name = "IsCancelled")
	private Character isCancelled;
	@Column(name = "CancelDate")
	private Date cancelDate = new Date();
	@Column(name = "CancelReason")
	private String cancelReason;
	@Column(name = "CancelVoucher_id")
	private BigInteger cancelVoucherid;
	@Column(name = "CancelVoucherDate")
	private Date cancelVoucherDate = new Date();
	@Column(name = "PreInvoiceExplained")
	private Character preInvoiceExplained;
	@Column(name = "IsPrimaryBill")
	private Character isPrimaryBill;
	@Column(name = "Cheque_Number")
	private String chequeNumber;
	@Column(name = "Cheque_Date")
	private Date chequeDate = new Date();
	@Column(name = "Cheque_Amount")
	private BigDecimal chequeAmount;
	@Column(name = "Bank_Name")
	private String bankName;
	@Column(name = "Bank_Branch")
	private String bankBranch;
	@Column(name = "CreatedDate")
	private Date createdDate = new Date();
	@Column(name = "CreatedBy")
	private String createdBy;
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	@Column(name = "ModifiedBy")
	private String modifiedBy;
	@Column(name = "gp_hdr_id")
	private BigInteger ghdrId;
	@Column(name = "View_Doc_No")
	private String viewDocNo;
	@Column(name = "Version_No")
	private Integer versionNo;
	@Column(name = "paymentMode")
	private String paymentMode;
	@Column(name = "T_OUTSID_LB_ANMNT")
	private BigDecimal toutsIdLbanmnt;
	@Column(name = "Total_GST")
	private BigInteger totalGSTAMT;
	
}
