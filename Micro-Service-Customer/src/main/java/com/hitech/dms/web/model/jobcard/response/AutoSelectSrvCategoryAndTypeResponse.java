/**
 * 
 */
package com.hitech.dms.web.model.jobcard.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class AutoSelectSrvCategoryAndTypeResponse {
	private BigInteger serviceTypeId;
	private BigInteger serviceTypeOemId;
	private String srvTypeCode;
	private String srvTypeDesc;
	private int serviceCategoryId;
	private String categoryCode;
	private String categoryDesc;
}
