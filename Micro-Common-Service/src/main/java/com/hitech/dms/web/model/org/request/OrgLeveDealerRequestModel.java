package com.hitech.dms.web.model.org.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class OrgLeveDealerRequestModel {
	private BigInteger orgHierID;
	private String includeInactive;
}
