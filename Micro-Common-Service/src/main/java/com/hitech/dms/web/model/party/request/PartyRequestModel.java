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
public class PartyRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String isFor;
	
}
