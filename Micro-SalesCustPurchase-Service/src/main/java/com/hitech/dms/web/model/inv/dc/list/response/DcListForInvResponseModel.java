/**
 * 
 */
package com.hitech.dms.web.model.inv.dc.list.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcListForInvResponseModel {	
	private BigInteger dcId;
	private String dcNumber;
	private String dcDate;
	private String dcStatus;
	private Integer dcTypeId;
	private String dcType;
	private BigInteger machineAllotmentId;
	private String allotNumber;
	private String allotDate;
	private BigInteger enquiryHdrId;
	private String enquiryNo;
	private String enquiryType;
	private String enquiryDate;
}
