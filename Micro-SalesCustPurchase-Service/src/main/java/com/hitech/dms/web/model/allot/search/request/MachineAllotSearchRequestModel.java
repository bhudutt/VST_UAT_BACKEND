/**
 * 
 */
package com.hitech.dms.web.model.allot.search.request;

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
public class MachineAllotSearchRequestModel {
	private BigInteger orgHierID;
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String allotNumber;
	private String enquiryNo;
	private String series;
	private String segment;
	private String model;
	private String variant;
	private Integer stateId;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private String includeInActive;
	private int page;
	private int size;
	
	private String fromDate1;
	private String toDate1;
	private String zone;
	private String area;
	private String allotmentId;
	private String dcNumber;
	private String dcStatus;
	private String invoiceNumber;
	private String poNumber;
	private String invoiceStatus;
	
	private String activity_plan_hdr_id;
	private String ActivityNo;
	private String actualActivityHdrId;
	private Integer orgHierID1;
	private Integer branchId1;
	private Integer dealerId1;
	
	private Integer plateFormId;
	
	
}
