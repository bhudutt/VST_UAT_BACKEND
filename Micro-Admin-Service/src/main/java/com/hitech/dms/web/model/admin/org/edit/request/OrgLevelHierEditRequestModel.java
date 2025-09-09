/**
 * 
 */
package com.hitech.dms.web.model.admin.org.edit.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class OrgLevelHierEditRequestModel {
	private BigInteger dealerVsFieldRoleId;
	private BigInteger dealerId;
	private Integer departmentId;
	private Integer pcId;
	private BigInteger orgHierId;
	private Boolean isActive;
}
