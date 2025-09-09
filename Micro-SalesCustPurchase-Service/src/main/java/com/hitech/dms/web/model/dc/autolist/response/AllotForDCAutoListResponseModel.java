/**
 * 
 */
package com.hitech.dms.web.model.dc.autolist.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class AllotForDCAutoListResponseModel {
	private BigInteger machineAllotmentId;
	private String allotNumber;
	private String allotDate;
	private BigInteger enquiryHdrId;
	private String enquiryNo;
	private String enquiryType;
	private String enquiryDate;
	private String displayValue;
}
