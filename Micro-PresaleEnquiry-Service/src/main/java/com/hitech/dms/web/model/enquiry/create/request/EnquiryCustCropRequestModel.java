package com.hitech.dms.web.model.enquiry.create.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryCustCropRequestModel {
	private BigInteger enquiryCropId;

	private EnquiryCreateRequestModel enquiryHdr;

	private String cropGrown;

	private Boolean deleteFlag;
}
