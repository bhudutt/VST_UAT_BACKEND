package com.hitech.dms.web.model.activitycreditnote.search.response;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonPropertyOrder({ "ActivityNumber", "ActivityCreationDate", "ActivityMonth", "ActivityYear", "TotalBudget", "ApprovedBudget",
	"ReimbursementClaim", "Description", "CreditNoteNo", "CreditNoteAmount", "CreditNoteDate", "ActivityInvoiceNo",
	"ActivityInvoiceDate", "vendorinvoiceno"})
public class SearchActivityCreditNoteResponseModel {

   
	
	@JsonProperty("ActivityNumber")
    private String ActivityNumber;
    
	@JsonProperty("ActivityCreationDate")
    private String ActivityCreationDate;
    
	@JsonProperty("ActivityMonth")
    private Integer ActivityMonth;
    
	@JsonProperty("ActivityYear")
    private Integer ActivityYear;
    
	@JsonProperty("TotalBudget")
    private BigDecimal TotalBudget;
    
	@JsonProperty("ApprovedBudget")
    private BigDecimal ApprovedBudget;
    
	@JsonProperty("ReimbursementClaim")
    private BigDecimal ReimbursementClaim;
    
	@JsonProperty("Description")
    private String Description;
    
	@JsonProperty("CreditNoteNo")
    private String CreditNoteNo;
    
	@JsonProperty("CreditNoteAmount")
    private BigDecimal  CreditNoteAmount;
    
	@JsonProperty("CreditNoteDate")
    private Date CreditNoteDate;
    
	@JsonProperty("ActivityInvoiceNo")
    private String  ActivityInvoiceNo;
    
	@JsonProperty("ActivityInvoiceDate")
    private Date ActivityInvoiceDate;
    
	@JsonProperty("vendorinvoiceno")
    private String vendorinvoiceno;  
//    private String VendorCode;
    


	
	










	

	


}
