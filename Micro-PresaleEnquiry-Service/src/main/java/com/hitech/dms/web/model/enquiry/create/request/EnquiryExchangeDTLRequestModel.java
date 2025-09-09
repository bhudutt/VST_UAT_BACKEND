/**
 * 
 */
package com.hitech.dms.web.model.enquiry.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryExchangeDTLRequestModel {
	private BigInteger enquiryExcDTLId;

	private EnquiryCreateRequestModel enquiryHdr;

	private BigInteger brandId;

	private String modelName;

	private Integer modelYear;

	private BigDecimal estimatedExchangePrice;

	private Boolean machineReceived;
}
