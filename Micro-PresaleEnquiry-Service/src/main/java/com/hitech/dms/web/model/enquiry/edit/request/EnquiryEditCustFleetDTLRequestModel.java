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
public class EnquiryEditCustFleetDTLRequestModel {
	private BigInteger enquiryMachineryId;

	private EnquiryEditRequestModel enquiryHdr;

	private BigInteger brandId;

	private String modelName;

	private Integer yearOfPurchase;

	private Boolean deleteFlag;
}
