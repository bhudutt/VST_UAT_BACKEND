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
public class HoUserSearchResponseModel {
	private BigInteger id;
	private String action;
	private String empCode;
	private String empName;
	private String empStatus;
	private String loginId;
	private String loginIdStatus;
}
