package com.hitech.dms.web.model.partreturn.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PartReturnSearchResponseModel {

	private BigInteger id;
	private String partReturnNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date partReturnDate;
	private String partReturnType;
	private String returnBy;
	private String issueNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date issueDate;
	private String jobCardNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date jobCardDate;
	private String reasonForReturn;
//	private BigDecimal returnQTY;
}
