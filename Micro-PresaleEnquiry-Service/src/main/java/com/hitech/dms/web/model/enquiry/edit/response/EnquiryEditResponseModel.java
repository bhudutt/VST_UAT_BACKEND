/**
 * 
 */
package com.hitech.dms.web.model.enquiry.edit.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryEditResponseModel {
	private BigInteger enquiryHdrId;
	private String enquiryNo;
	private String msg;
	private int statusCode;
}
