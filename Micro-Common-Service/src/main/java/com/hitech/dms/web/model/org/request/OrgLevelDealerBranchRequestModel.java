package com.hitech.dms.web.model.org.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class OrgLevelDealerBranchRequestModel {
	private BigInteger dealerId;
	private String includeInactive;
}
