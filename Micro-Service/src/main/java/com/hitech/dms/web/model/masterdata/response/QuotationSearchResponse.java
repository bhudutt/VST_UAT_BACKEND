package com.hitech.dms.web.model.masterdata.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties({"vin_Id","customer_Id"})
@JsonPropertyOrder({"action","quotationNumber","QuotationDate","Status","Source","ServiceCategory","ServiceType"})
public interface QuotationSearchResponse {

	
    Long getQuotation_Id();
    Long getRecordCount();
    int getTotalRecords();
   // @JsonProperty("QuotationNumber")
    String getQuotationNumber();
    @JsonProperty("QuotationDate")
    String getQuotation_Date();
    @JsonProperty("Status")
    String getStatus();
    @JsonProperty("Source")
    String getSource();
    @JsonProperty("ServiceCategory")
     String getCategoryDesc();
    @JsonProperty("ServiceType")
      String getSrvTypeDesc();
    
    String getAction();
    //Integer getCustomer_Id();
    //Integer getVin_Id();
   // String getCurrentHours();
   // String gettotalHours();
   // String getRemarksCustomer();
   // String getRepairCatgDesc();
    //String getPartyName();
    
    //BigDecimal getChargeAmtPart();
    //BigDecimal getChargeAmtLabour();
    //BigDecimal getChargeAmtOutsideLabour();
    
 //   BigDecimal getTotalAmtPart();
  //  BigDecimal getTotalAmtLabour();
   // BigDecimal getTotalAmtOutsideLabour();
    
    
	/*
	 * BigDecimal getBasicAmtPart(); BigDecimal getBasicAmtLabour(); BigDecimal
	 * getBasicAmtOutsideLabour(); BigDecimal getDiscountAmtPart(); BigDecimal
	 * getDiscountAmtLabour(); BigDecimal getDiscountAmtOutsideLabour();
	 */
    
   
    
    
//    Quotation number
//    2.Quotation date
//    3. Status
//    4.Source
//    5.Service category
//    6.Service Type
    
    
    
    
    
    


}
