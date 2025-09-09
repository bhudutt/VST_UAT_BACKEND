/**
 * 
 */
package com.hitech.dms.web.model.admin.dtl.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class HoUserDTLRequestModel {
	private BigInteger hoUserId;
	private String criteria;
	private String criteriaText;
	private String empCodeVal;
}
