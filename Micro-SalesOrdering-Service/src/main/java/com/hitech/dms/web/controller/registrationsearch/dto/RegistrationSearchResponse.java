package com.hitech.dms.web.controller.registrationsearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id","profitCenter","chassisNumber","engineNumber", "invoiceNumber", "registrationNumber","modelName","itemDescription","customerName","customerMobileNo","fileName" })
@JsonIgnoreProperties({"recordCount"})
public interface RegistrationSearchResponse {

    Long getId();
    
    @JsonProperty("profitCenter")
    String getProfitCenter();
    
    @JsonProperty("chassisNumber")
    String getChassisNumber();
    
    @JsonProperty("engineNumber")
    String getEngineNumber();
    
    @JsonProperty("invoiceNumber")
    String getInvoiceNumber();
    
    @JsonProperty("registrationNumber")
    String getRegistrationNumber();
    
    @JsonProperty("modelName")
    String getModelName();
    
    @JsonProperty("itemDescription")
    String getItemDescription();
    
    @JsonProperty("customerName")
    String getCustomerName();
    
    @JsonProperty("customerMobileNo")
    String getCustomerMobileNo();
    
    @JsonProperty("fileName")
    String getfileName();
    
    Long getRecordCount();

}
