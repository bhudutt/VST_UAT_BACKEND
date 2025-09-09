package com.hitech.dms.web.model.partrequisition.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PartRequisitionSearchResponseModel {
    
	private BigInteger Id;
	private String action;
	private String 	requisitionNo;
	private String requisitionType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private String  requisitionDate;
	private String jobCardNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private String jobCarddate;
	private BigDecimal totalRequestedQty;
	private String requisitionStatus;
}
