/**
 * 
 */
package com.hitech.dms.web.model.org.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
//@JsonInclude(Include.NON_NULL)
public class OrgLevelHierForParentResponseModel {
	private BigInteger orgHierarchyID;
	private Integer levelID;
	private String hierarchyCode;
	private String hierarchyDesc;
	private BigInteger parentOrgHierarchyID;
	private Integer childLevel;
	private Boolean isActive;
	
	private BigInteger dealerId;
	private String dealerCode;
	private String dealerName;
	private Integer pcId;
	private String pcDesc;
	private Integer departmentId;
	private String departmentDesc;
}
