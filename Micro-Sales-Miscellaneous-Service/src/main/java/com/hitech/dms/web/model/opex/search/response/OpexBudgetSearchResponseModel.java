/**
 * 
 */
package com.hitech.dms.web.model.opex.search.response;

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
public class OpexBudgetSearchResponseModel {
	private BigInteger id; // opexId
	private String action;
	private String opexNumber;
	private String opexDate;
	private String opexStatus;
	private String state;
	private String profitCenter;
	private String finYear;
	private String remarks;
	private String opexUpdatedDate;
}
