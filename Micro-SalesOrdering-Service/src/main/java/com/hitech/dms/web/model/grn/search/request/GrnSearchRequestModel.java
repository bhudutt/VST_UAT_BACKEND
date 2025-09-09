/**
 * 
 */
package com.hitech.dms.web.model.grn.search.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class GrnSearchRequestModel {
	private BigInteger orgHierID;
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String grnNo;
	private String invoiceNo;
	private String series;
	private String segment;
	private String model;
	private String variant;
	private Integer grnTypeId;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private String includeInActive;
	private int page;
	private int size;
	private String zone;
	private String area;
	private String poNumber;
	
	private String fromDate1;
	private String toDate1;
	private Integer dealerId1;
	private Integer branchId1;
	private Integer orgHierID1;
	private String chassisNo;
	private String engineNo;
	private String pdiNo;
	private String postatus;
	private String POon;
	private Integer partyId;
	private Integer plantId;
	
	
}
