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
	private BigInteger dealerID;
	private BigInteger branchID;
	private BigInteger pcID;
	private String enqNumber;
	private String enqFrom;
	private BigInteger enqStageId;
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
	private BigInteger modelID;
	private BigInteger salesPerson;
	private BigInteger enqSourceID;
	private String prospectType;
	private BigInteger orgHierID;
	private String includeInActive;
	private int page;
	private int size;
	
}
