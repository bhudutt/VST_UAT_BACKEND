package com.hitech.dms.web.model.enquiry.followup.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

@Data
public class FollowupCreateRequestModel {

	private EnquiryHdrRequestModel enquiryHdr;
	private BigInteger enquiryTypeId;
	private Integer enquiryStageId;
	private BigInteger followupTypeId;
	@JsonDeserialize(using = DateHandler.class)
	private Date currentFollowupDate;
	private String remarks;
	@JsonDeserialize(using = DateHandler.class)
	private Date expectedPurchaseDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date nextFollowUpDate;
	private BigInteger nextFollowupActivityId;
	private String followupAction;

	private String lostDropReason;
	private String lostDropRemark;
	private BigInteger brandId;
	private String model;

}
