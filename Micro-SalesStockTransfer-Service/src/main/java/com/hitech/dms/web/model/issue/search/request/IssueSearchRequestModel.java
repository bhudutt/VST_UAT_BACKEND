/**
 * 
 */
package com.hitech.dms.web.model.issue.search.request;

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
public class IssueSearchRequestModel {
	private Integer pcId;
	private BigInteger orgHierID;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String issueNumber;
	private BigInteger issueBy;
	private BigInteger issueToBranchId;
	private String series;
	private String segment;
	private String model;
	private String variant;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private String includeInActive;
	private int page;
	private int size;
}
