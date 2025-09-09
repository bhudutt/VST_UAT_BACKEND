/**
 * 
 */
package com.hitech.dms.web.model.enquiry.create.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryCustFleetDTLRequestModel {
	private BigInteger enquiryMachineryId;

	private EnquiryCreateRequestModel enquiryHdr;

	private BigInteger brandId;

	private String modelName;

	private Integer yearOfPurchase;

	private Boolean deleteFlag;
}
