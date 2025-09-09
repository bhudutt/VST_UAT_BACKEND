/**
 * 
 */
package com.hitech.dms.web.model.indent.search.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class IndentSearchRequestModel {
	private Integer pcId;
	private BigInteger orgHierID;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String indentNumber;
	private BigInteger indentBy;
	private BigInteger indentToBranchId;
	private String series;
	private String segment;
	private String model;
	private String variant;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private String indentStatus;
	private String includeInActive;
	private int page;
	private int size;
}
