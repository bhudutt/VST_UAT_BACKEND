package com.hitech.dms.web.controller.installation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id","chassisNumber","engineNumber", "installationNumber", "installationDate", "installationType", "MachineModel","profitCenter","customerName","customerMobile","currentHours"
	,"installationDoneBy","representativeType","representativeName","fileName","remarks", "createdDate", "createdBy"})
@JsonIgnoreProperties({"recordCount","createdDate","createdBy"})
public interface InstallationSearchResponse {

    Long getId();
    @JsonProperty("chassisNumber")
    String getChassisNumber();
    @JsonProperty("engineNumber")
    String getEngineNumber();
    @JsonProperty("installationNumber")
    String getInstallationNumber();
    @JsonProperty("installationDate")
    String getInstallationDate();
    @JsonProperty("installationType")
    String getInstallationType();
    @JsonProperty("MachineModel")
    String getMachineModel();
    
    @JsonProperty("profitCenter")
    String getProfitCenter();
    
    @JsonProperty("customerName")
    String getCustomerName();
    
    @JsonProperty("customerMobile")
    String getCustomerMobileNo();
    
    @JsonProperty("currentHours")
    String getCurrentHours();
    
    @JsonProperty("installationDoneBy")
    String getInstallationDoneBy();
    
    @JsonProperty("representativeType")
    String getRepresentativeType();
    
    @JsonProperty("representativeName")
    String getRepresentativeName();
    
    @JsonProperty("fileName")
    String getFileUpload();
    
    @JsonProperty("remarks")
    String getRemarks();
    
    @JsonProperty("createdDate")
    String getcreatedDate();
    @JsonProperty("createdBy")
    String getcreatedBy();
    
    Long getRecordCount();



}
