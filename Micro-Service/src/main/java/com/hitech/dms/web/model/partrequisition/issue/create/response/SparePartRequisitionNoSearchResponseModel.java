package com.hitech.dms.web.model.partrequisition.issue.create.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SparePartRequisitionNoSearchResponseModel {
   
	private String requisitionNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date jobCardDate;
	private String chassisNo;
	private String registrationNumber;
	private String modelName;
	private String firstname;
}
