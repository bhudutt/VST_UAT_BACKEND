/**
 * 
 */
package com.hitech.dms.web.model.dc.search.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DcSearchRequestModel {
	private BigInteger orgHierID;
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String dcNumber;
	private Integer stateId;
	private String allotNumber;
	private String enquiryNo;
	private String series;
	private String segment;
	private String model;
	private String variant;
	private String dcStatus;
	//@JsonDeserialize(using = DateHandler.class)
	private String fromDate;
	//@JsonDeserialize(using = DateHandler.class)
	private String toDate;
	private String includeInActive;
	private int page;
	private int size;
	private String chassisNo;
	@Override
	public String toString() {
		return "DcSearchRequestModel [orgHierID=" + orgHierID + ", pcId=" + pcId + ", dealerId=" + dealerId
				+ ", branchId=" + branchId + ", dcNumber=" + dcNumber + ", allotNumber=" + allotNumber + ", enquiryNo="
				+ enquiryNo + ", series=" + series + ", segment=" + segment + ", model=" + model + ", variant="
				+ variant + ", dcStatus=" + dcStatus + ", fromDate=" + fromDate + ", toDate=" + toDate
				+ ", includeInActive=" + includeInActive + ", page=" + page + ", size=" + size + "]";
	}
	
	
}
