package com.hitech.dms.web.controller.pdi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"action","id","inwardNumber","inwardDate", "invoiceNumber", "chassisNumber", "engineNumber", "machineModel", "lrDate", "lrNumber","transporterName","pdiDoneBy", "starterMotorMakeNumber", "alternatorMakeNumber","fipMakeNumber","batteryMakeNumber","frontTyerMakeRHNumber","frontTyerMakeLHNumber","itemNumber","itemDescription","profitCenter","obervation","fileUpload","createdDate","createdBy", "pdiStatus"})
@JsonIgnoreProperties({"recordCount"})
public interface PdiSearchResponse {

    Long getId();

    String getAction();
    
    @JsonProperty("inwardNumber")
    String getInwardNumber();
    
    @JsonProperty("Inward Date")
    String getInwardDate();
    
    @JsonProperty("Invoice Number")
    String getInvoiceNumber();

    @JsonProperty("Chassis Number")
    String getChassisNumber();
    
    @JsonProperty("Engine Number")
    String getEngineNumber();

    @JsonProperty("Machine Model")
    String getMachineModel();

    @JsonProperty("LR Date")
    String getlrDate();
    
    @JsonProperty("LR Number")
    String getlrNumber();
    
    @JsonProperty("Transporter Name")
    String gettransporterName();
    
    @JsonProperty("PDI Done By")
    String getpdiDoneBy();
    
    @JsonProperty("Starter Motor Make Number")
    String getstarterMotorMakeNumber();
    
    @JsonProperty("Alternator Make Number")
    String getalternatorMakeNumber();
    
    @JsonProperty("FIP Make Number")
    String getfipMakeNumber();
    
    @JsonProperty("Battery Make Number")
    String getbatteryMakeNumber();
    
    @JsonProperty("Front Tyer Make RH Number")
    String getfrontTyerMakeRHNumber();
    
    
    @JsonProperty("Front Tyer Make LH Number")
    String getfrontTyerMakeLHNumber();
    
    @JsonProperty("Item Number")
    String getitemNumber();
    
    @JsonProperty("Item Description")
    String getitemDescription();
    
    @JsonProperty("Profit Center")
    String getprofitCenter();
    
    @JsonProperty("Obervation")
    String getobervation();
    
    @JsonProperty("FileUpload")
    String getfileUpload();
    
    @JsonProperty("Created Date")
    String getcreatedDate();
    
    @JsonProperty("Created By")
    String getcreatedBy();
    
    @JsonProperty("PDI Status")
    String getPdiStatus();
    Long getRecordCount();

}
