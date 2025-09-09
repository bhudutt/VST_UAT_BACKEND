/**
 * 
 */
package com.hitech.dms.web.model.enquiry.create.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryFollowupRequestModel {
	private BigInteger enquiryFallowUpId;

	private EnquiryCreateRequestModel enquiryHdr;

	private BigInteger enquiryTypeId;

	private Integer enquiryStageId;

	private BigInteger followupTypeId;

	@JsonDeserialize(using = DateHandler.class)
	private Date currentFollowupDate;

	private String remarks;

	@JsonDeserialize(using = DateHandler.class)
	private Date expectedPurchaseDate;

	@JsonDeserialize(using = DateHandler.class)
	private Date nextFollowupDate;

	private BigInteger nextFollowupActivityId;
}
