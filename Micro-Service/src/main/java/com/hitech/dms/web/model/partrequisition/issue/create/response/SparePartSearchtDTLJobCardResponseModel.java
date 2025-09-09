package com.hitech.dms.web.model.partrequisition.issue.create.response;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SparePartSearchtDTLJobCardResponseModel {

	private BigInteger RequisitionId;
	private String jobCardNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private String jobcarddate;
	private String chassisNo;
	private String registrationNumber;
	private String modelVariant;
	private String firstname;
	private String requisitionNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private String requisitionDate;
	
}
