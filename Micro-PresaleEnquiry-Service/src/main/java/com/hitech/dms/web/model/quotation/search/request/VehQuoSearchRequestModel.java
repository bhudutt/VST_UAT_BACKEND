/**
 * 
 */
package com.hitech.dms.web.model.quotation.search.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class VehQuoSearchRequestModel {
	private BigInteger pcID;
	private BigInteger orgHierID;
	private BigInteger dealerID;
	private BigInteger branchID;
	private String quoNumber;
	private String enqNumber;
	private String enqFrom;
	private BigInteger enqStageId;
	private String enqStatus;
	@JsonDeserialize(using = DateHandler.class)
	private Date enqFromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date enqToDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date quoFromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date quoToDate;
	private String series;
	private String segmant;
	private String variant;
	private BigInteger modelID;
	private BigInteger salesPerson;
	private BigInteger enqSourceID;
	private String prospectType;
	private String includeInActive;
	private int page;
	private int size;
}
