/**
 * 
 */
package com.hitech.dms.web.model.dc.search.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcSearchResponseModel {
	private BigInteger id; // dcId
	private BigInteger id1; // branchId
	private Integer id2; // pcId
	private String action;
	private String dealerCode;
	private String dealerName;
	private String pcDesc;
	private String dcNumber;
	private String dcStatus;
	private String dcDate;
	private String allotNumber;
	private String allotDate;
	private String allotStatus;
	private String enquiryNo;
	private String enquiryDate;
	private BigInteger id3; // customerId
	private String customerName;
}
