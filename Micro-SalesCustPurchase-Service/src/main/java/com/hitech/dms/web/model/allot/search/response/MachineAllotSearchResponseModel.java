/**
 * 
 */
package com.hitech.dms.web.model.allot.search.response;

import java.math.BigInteger;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */

@Getter
@Setter
public class MachineAllotSearchResponseModel {
	private BigInteger id; // machineAllotmentId
	private BigInteger id1; // branchId
	private Integer id2; // pcId
	private String dealerCode;
	private String dealerName;
	private String pcDesc;
	private String allotNumber;
	private String allotDate;
	
	private String allotStatus;
	private String enquiryNo;
	private String enquiryDate;
	private BigInteger id3; // customerId
	private String customerName;
	private String onlyImplementFlag;
	private String deAllotFlag;
	private String deAllotDate;
	private String deAllotReason;
}
