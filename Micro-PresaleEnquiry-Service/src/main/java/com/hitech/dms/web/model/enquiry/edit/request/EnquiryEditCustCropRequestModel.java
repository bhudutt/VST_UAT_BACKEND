/**
 * 
 */
package com.hitech.dms.web.model.enquiry.edit.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryEditCustCropRequestModel {
	private BigInteger enquiryCropId;

	private EnquiryEditRequestModel enquiryHdr;

	private String cropGrown;

	private Boolean deleteFlag;
}
