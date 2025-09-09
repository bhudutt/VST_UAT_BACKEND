package com.hitech.dms.web.model.activitycreditnote.search.response;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class SearchActivityCreditNoteResponseModel {

    private String vendorinvoiceno;
	
    private String ActivityNumber;
    
    private String ActivityCreationDate;
    
    private Integer ActivityMonth;
    
    private Integer ActivityYear;
    
    private BigDecimal TotalBudget;
    
    private BigDecimal ApprovedBudget;
    
    private BigDecimal ReimbursementClaim;
    
    private String Description;
    
    private String CreditNoteNo;
    
    private BigDecimal  CreditNoteAmount;
    
    private Date CreditNoteDate;
    
    private String  ActivityInvoiceNo;
    
    private Date ActivityInvoiceDate;
    
    private String VendorCode;
    


	
	










	

	


}
