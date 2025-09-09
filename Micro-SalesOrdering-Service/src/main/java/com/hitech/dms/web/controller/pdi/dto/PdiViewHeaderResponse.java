package com.hitech.dms.web.controller.pdi.dto;

public interface PdiViewHeaderResponse {

    public String getPdiNumber();
    String getPdiDate();
    Long getChassisId();
    String getEngineNumber();
    String getKaiInvoiceNumber();
    String getMachineModel();
    String getDmsGrnNumber();
    String getDmsGrnDate();
    String getChassisNo();
    Boolean getOkFlag();
    String getRemarks();

}
