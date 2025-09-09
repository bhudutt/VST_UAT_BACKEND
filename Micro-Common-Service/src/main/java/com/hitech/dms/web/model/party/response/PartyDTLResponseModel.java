/**
 * 
 */
package com.hitech.dms.web.model.party.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class PartyDTLResponseModel {
	private BigInteger id;
	private String partyCode;
	private String partyName;
	private String partyLocation;
	private String displayValue;
}
