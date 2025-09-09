/**
 * 
 */
package com.hitech.dms.web.model.allot.autolist.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachineEnqAllotAutoListResponseModel {
	private BigInteger enquiryHdrId;
	private String enquiryNo;
	private String enquiryDate;
	private String displayValue;
}
