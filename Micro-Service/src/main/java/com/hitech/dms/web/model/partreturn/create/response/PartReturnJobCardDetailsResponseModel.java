package com.hitech.dms.web.model.partreturn.create.response;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class PartReturnJobCardDetailsResponseModel {

	private String jobcardNo;
	private Date jobcardDate;
	private String chassisNo;
	private String registrationNo;
	private String modelVariant;
	private String customerName;
	private String partRequisitionNo;
	private String partNo;
	private String partDescription;
	private BigDecimal pendingrequestedQty;
	private BigDecimal requestedQty;
	private BigDecimal issuedQty;
	private String formStore;
	private String binLocations;
	private String returnmarks;
}
