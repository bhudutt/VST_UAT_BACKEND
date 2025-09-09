package com.hitech.dms.web.model.retailFollowUp.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;
import com.hitech.dms.web.model.retailFollowUp.create.response.RetailStageModelResponse;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class RetailFollowUpSubmitRequestModel {
	private BigInteger enquiryId;
	
	@JsonProperty(value = "cashOrLoan", required = true)
	@NotNull(message = "Cash/Loan is Required")
	private String  cashOrLoan;
	
	private Integer financierPartyId;
	private String  financierStatus;
	

	private String loanStatus;
	
	@JsonDeserialize(using = DateHandler.class)
	private Date disbursementDate;
	
	private char isLatest;
	
	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value = "currentFollowupDate", required = true)
	@NotNull(message = "Current Followup Date Is Required")
	private Date currentFollowupDate;
	
	@JsonDeserialize(using = DateHandler.class)
	@JsonProperty(value = "expectedRetailDate", required = true)
	@NotNull(message = "Expected Retail Date Is Required")
	private Date expectedRetailDate;
	
	
	
	@JsonProperty(value = "followupById", required = true)
	@NotNull(message = "FollowUp By is Required")
	private Integer followupById;
	
//	@JsonProperty(value = "retailStageId", required = true)
//	@NotNull(message = "Retail Stage is Required")
	private Integer retailStageId;
	
	@JsonProperty(value = "remarks", required = true)
	@NotNull(message = "Remarks is Required")
	private String remarks;
	

	private String reasonForRejection;
	
	
	private BigDecimal disbursementAmount;
	
	private BigInteger createdBy;
	private Date createdDate;
	

	private BigInteger retailFollowUpHdrId;
	
	List<RetailStageModelResponse> listR1 = new ArrayList<>();
	
	List<RetailStageModelResponse> listR2 = new ArrayList<>();
	
	List<RetailStageModelResponse> listR3 = new ArrayList<>();

}
