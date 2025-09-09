package com.hitech.dms.web.model.enquiry.transfer.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandlerForDDMMMYYYY;

import lombok.Data;

@Data
public class TransferENQRequestModel {
	private BigInteger dealerId;
	private BigInteger branchID;
	private BigInteger salesPersonID;
	private String enquiryNo;
	private BigInteger districtId;
	private BigInteger tehsilId;
	private BigInteger enquiryTypeID;
	private Integer enquiryStage;
//	@JsonDeserialize(using = DateHandlerForDDMMMYYYY.class)
	private String enquiryFromDate;
//	@JsonDeserialize(using = DateHandlerForDDMMMYYYY.class)
	private String enquiryToDate;
	private String includeInactive;
}
