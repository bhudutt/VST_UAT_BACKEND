package com.hitech.dms.web.model.machinestock.search;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Data;

@Data
public class MachineStockSearchRequestModel {

private String userCode;
private String dealerCode;
//long now = System.currentTimeMillis();
//@JsonDeserialize(using = DateHandler.class)
//private Date fromDate = new Date(now);
//@JsonDeserialize(using = DateHandler.class)
//private Date toDate = new Date(now);
private String fromDate;
private String toDate;
private String chassisNo;
private String engineNo;
private String vinNo;
private String invoiceNo;
private String includeInactive;
private Integer orgId;
private Integer page;
private Integer size;
private Integer pcId;
private Integer dealerId;
private Integer branchID;
private String productDivision;
private String itemNo;
private String variant;
private Integer modelId;
private Integer stateId;

}
