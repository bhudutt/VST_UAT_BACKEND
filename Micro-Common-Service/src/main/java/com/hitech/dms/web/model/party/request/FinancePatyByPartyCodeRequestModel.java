/**
 * 
 */
package com.hitech.dms.web.model.party.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class FinancePatyByPartyCodeRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private BigInteger partyId;
	private String partyCode;
}
