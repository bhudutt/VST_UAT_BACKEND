/**
 * 
 */
package com.hitech.dms.web.model.party.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class FinancePartyByBranchRequestModel {
	private BigInteger branchID;
	private String code;
}
