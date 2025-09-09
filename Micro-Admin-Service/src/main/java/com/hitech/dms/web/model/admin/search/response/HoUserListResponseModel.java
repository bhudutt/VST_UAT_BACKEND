/**
 * 
 */
package com.hitech.dms.web.model.admin.search.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class HoUserListResponseModel {
	private BigInteger hoUserId;
	private String employeeCode;
	private String employeeName;
	private String empContactNo;
	private String deisplayValue;
}
