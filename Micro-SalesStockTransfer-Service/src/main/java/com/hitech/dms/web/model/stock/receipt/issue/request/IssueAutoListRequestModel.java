/**
 * 
 */
package com.hitech.dms.web.model.stock.receipt.issue.request;

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
public class IssueAutoListRequestModel {
	private BigInteger dealerId;
	private Integer branchId;
	private Integer pcId;
	private String searchText;
}
