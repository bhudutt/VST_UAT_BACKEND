package com.hitech.dms.web.model.partrequisition.issue.create.response;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PartRequisitionNumberResponseModel {

	private BigInteger requisitionId;
	private String requisitionNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	private Date requisitionDate;
}
