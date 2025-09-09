/**
 * 
 */
package com.hitech.dms.web.model.enquiry.create.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryCreateResponseModel {
	private BigInteger enquiryHdrId;
	private String enquiryNo;
	private String msg;
	private int statusCode;
}
