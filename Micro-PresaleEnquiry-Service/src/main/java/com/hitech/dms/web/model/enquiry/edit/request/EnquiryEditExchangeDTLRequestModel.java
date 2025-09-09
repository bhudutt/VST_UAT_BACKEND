/**
 * 
 */
package com.hitech.dms.web.model.enquiry.edit.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryEditExchangeDTLRequestModel {
	private BigInteger enquiryExcDTLId;

	private EnquiryEditRequestModel enquiryHdr;

	private BigInteger brandId;

	private String modelName;

	private Integer modelYear;

	private BigDecimal estimatedExchangePrice;

	private Boolean machineReceived;
}
