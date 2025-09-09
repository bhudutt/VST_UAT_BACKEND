/**
 * 
 */
package com.hitech.dms.web.model.org.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class OrgLevelHierForParentRequestModel {
	private Integer levelID;
	private Long orgHierID;
	private String includeInactive;
	private String isFor;
	private BigInteger dealerId;
	private Integer departmentId;
	private Integer pcId;
}
