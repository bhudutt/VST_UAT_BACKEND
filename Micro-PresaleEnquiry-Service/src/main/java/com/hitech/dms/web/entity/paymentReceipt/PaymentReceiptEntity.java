package com.hitech.dms.web.entity.paymentReceipt;



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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */

@Entity
@Table(name = "FM_PAYMENT_RECEIPT")
@Data
public class PaymentReceiptEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6487849789203854481L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	
	@Column(name = "payment_receipt_id")
	private BigInteger paymentReceiptId;

	@JsonProperty(value = "branchId", required = true)
	@NotNull(message = "Branch Is Required")
	@Column(name = "branch_id")
	private BigInteger branchId;
	
	@Column(name = "enquiry_id")
	private BigInteger enquiryId;
	
	@Column(name = "receipt_no")
	private String receiptNo;
	
	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value = "receiptDate", required = true)
	@NotNull(message = "Receipt Date Is Required")
	@Column(name = "receipt_date")
	private Date receiptDate;
	
	@JsonProperty(value = "receiptAmount")
	@Column(name = "receipt_amount")
	private  BigDecimal receiptAmount;
	
	@JsonProperty(value = "receiptAmountInput", required = true)
	@Column(name = "receipt_amt_input")
	private  BigDecimal receiptAmountInput;
	
	
	@JsonProperty(value = "receiptModeId", required = true)
	@NotNull(message = "Receipt Mode Is Required")
	@Column(name = "receipt_mode_id")
	private BigInteger receiptModeId;
	
	@JsonProperty(value = "receiptTypeId", required = true)
	@NotNull(message = "Receipt Type Is Required")
	@Column(name = "receipt_type_id")
	private BigInteger receiptTypeId;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "party_master_id")
	private BigInteger partyMasterId;
	
	@Column(name = "customer_master_id")
	private BigInteger customerMasterId;
	
	@Column(name = "co_dealer_id")
	private BigInteger coDealerId;
	
	@Column(name = "customer_name")
	private String customerName;
	
	@Column(name = "contact_number")
	private String contactNumber;
	
	@Column(name = "customer_balance")
	private Integer customerBalance;
	
	@Column(name = "service_provider")
	private String serviceProvider;
	
	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value = "transactionDate", required = true)
	@Column(name = "transaction_date")
	private Date transactionDate;
	
	@Column(name = "transaction_no")
	private String transactionNo;
	
	@Column(name = "card_name")
	private String cardName;
	
	@Column(name = "card_no")
	private String cardNo;
	
	@Column(name = "card_type")
	private String cardType;
	
	@Column(name = "cheque_dd_bank")
	private String chequeDdBank;
	
	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value = "chequeDdDate", required = true)
	@Column(name = "cheque_dd_date")
	private Date chequeDdDate;
	
	@Column(name = "cheque_dd_no")
	private String chequeDdNo;
	
	@Column(name = "last_modified_by")
	private BigInteger lastModifiedBy;
	
	@Column(name = "last_modified_date")
	private Date lastModifiedDate;

}
