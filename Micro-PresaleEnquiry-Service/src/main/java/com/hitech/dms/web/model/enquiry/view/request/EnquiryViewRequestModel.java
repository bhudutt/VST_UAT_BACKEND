/**
 * 
 */
package com.hitech.dms.web.model.enquiry.view.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryViewRequestModel {
	private String enqNumber;
	private BigInteger enquiryId;
	private int flag;
}
