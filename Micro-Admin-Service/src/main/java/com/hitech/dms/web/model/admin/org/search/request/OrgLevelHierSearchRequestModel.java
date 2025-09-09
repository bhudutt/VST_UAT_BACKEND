/**
 * 
 */
package com.hitech.dms.web.model.admin.org.search.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class OrgLevelHierSearchRequestModel {
	private BigInteger dealerId;
	private Integer departmentId;
	private String includeInActive;
	private Integer page;
	private Integer size;
}
