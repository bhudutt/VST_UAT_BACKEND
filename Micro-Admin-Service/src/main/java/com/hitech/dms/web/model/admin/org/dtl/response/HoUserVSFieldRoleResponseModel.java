/**
 * 
 */
package com.hitech.dms.web.model.admin.org.dtl.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class HoUserVSFieldRoleResponseModel {
	private BigInteger usrIdVsOrdId;
	private BigInteger hoUserId;
	private BigInteger orgId;
	private String orgDesc;
	private String pcDesc;
	private String departmentDesc;
	private Boolean isActive;
}
