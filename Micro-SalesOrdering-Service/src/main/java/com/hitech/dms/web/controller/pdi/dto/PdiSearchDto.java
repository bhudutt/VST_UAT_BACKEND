package com.hitech.dms.web.controller.pdi.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PdiSearchDto {

    public Long pdiId;

    public String pdiFromDate;

    public String pdiToDate;

    public String invoiceNumber;
    public String chassisNo;
    public String engineNo;
    public String pdiNumber;

    public String dmsGrnNumber;

    public String dmsGrnFromDate;

    public String dmsGrnToDate;

    public Integer dealerId;

    public Integer dealerEmployeeId;

    public Integer employeeId;

    public Boolean managementAccess;

    public Integer page;

    public Integer size;
    
    public Long orgId;


}
