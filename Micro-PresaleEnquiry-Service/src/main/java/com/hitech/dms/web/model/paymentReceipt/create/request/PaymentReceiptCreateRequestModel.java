
package com.hitech.dms.web.model.paymentReceipt.create.request;

import java.math.BigInteger;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */

@Data
public class PaymentReceiptCreateRequestModel {
	
	private BigInteger paymentReceiptId;

	@JsonProperty(value = "branchId", required = true)
	@NotNull(message = "Branch Is Required")
	private BigInteger branchId;
	
//	@JsonProperty(value = "enquiryId", required = true)
//	@NotNull(message = "Enquiry Id Is Required")
	private BigInteger enquiryId;
	
	@JsonProperty(value = "receiptNo", required = true)
	@NotNull(message = "Receipt No Is Required")
	private String receiptNo;
	

	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value = "receiptDate", required = true)
	@NotNull(message = "Receipt Date Is Required")
	private Date receiptDate;
	
	@JsonProperty(value = "receiptAmount", required = true)
	@NotNull(message = "Receipt Amount Is Required")
	private  Integer receiptAmount;
	
	@JsonProperty(value = "receiptModeId", required = true)
	@NotNull(message = "Receipt Mode Is Required")
	private BigInteger receiptModeId;
	
	@JsonProperty(value = "receiptTypeId", required = true)
	@NotNull(message = "Receipt Type Is Required")
	private BigInteger receiptTypeId;
	
	private String remarks;
	private BigInteger partyMasterId;
	private BigInteger customerMasterId;
	private BigInteger coDealerId;
	private String customerName;
	private String contactNumber;
	
	private Integer customerBalance;
	private String serviceProvider;
	private Date transactionDate;
	private String transactionNo;
	private String cardName;
	private String cardNo;
	private String cardType;
	private String chequeDdBank;
	private Date chequeDdDate;
	private String chequeDdNo;
	private BigInteger lastModifiedBy;
	private Date lastModifiedDate;

}
