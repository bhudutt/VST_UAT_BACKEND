/**
 * 
 */
package com.hitech.dms.web.model.issue.search.response;

import java.math.BigInteger;

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
public class IssueSearchResponseModel {
	private BigInteger id; // issueId
	private String issueNumber;
	private String issueDate;
	private String issueBy;
	private BigInteger id1; // dealerId
	private String dealerShip;
	private String profitCenter;
	private BigInteger id2; // branchId
	private String issueFrom;
	private String issueToBranch;
}
