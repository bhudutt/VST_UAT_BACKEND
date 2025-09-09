package com.hitech.dms.web.model.partrequisition.issue.create.response;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PartRequisitionDetailsResponseModel {

	private BigInteger requisitionId;
	private String partRequisitionNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private String partRequisitionDate;
	private String chassisNo;
	private String registrationNumber;
	private String modelVariant;
	private String firstname;
}
