package com.hitech.dms.web.model.indent.search.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class IndentSearchResponseModel {
	private BigInteger id; // indentId
	private String indentNumber;
	private String indentDate;
	private String indentBy;
	private BigInteger id1; // dealerId
	private String dealerShip;
	private String profitCenter;
	private BigInteger id2; // branchId
	private String indentFrom;
	private String indentToBranch;
}
