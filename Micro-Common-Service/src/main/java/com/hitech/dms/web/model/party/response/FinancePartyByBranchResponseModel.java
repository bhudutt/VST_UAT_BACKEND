/**
 * 
 */
package com.hitech.dms.web.model.party.response;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class FinancePartyByBranchResponseModel {
	private BigInteger financePartyId;
	private String partyCode;
	private String partyName;
	private String partLocation;
	private String displayName;
}
