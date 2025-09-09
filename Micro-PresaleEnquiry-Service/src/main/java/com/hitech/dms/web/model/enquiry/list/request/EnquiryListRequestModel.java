/**
 * 
 */
package com.hitech.dms.web.model.enquiry.list.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryListRequestModel {
	private Integer dealerID;
	private Integer branchID;
	private Integer pcID;
	private String enqNumber;
	private String enqFrom;
	private Integer enqStageId;
	private String enqStatus;
	//@JsonDeserialize(using = DateHandler.class)
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date enqFromDate;
	//@JsonDeserialize(using = DateHandler.class)
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date enqToDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date enqFlpFromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date enqFlpToDate;
	private String series;
	private String segmant;
	private String variant;
	private Integer modelID;
	private Integer salesPerson;
	private Integer enqSourceID;
	private String prospectType;
	private Integer orgHierID;
	private String includeInActive;
	private int page;
	private int size;
	private String customerNameMobile;
	
}
