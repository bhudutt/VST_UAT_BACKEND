/**
 * 
 */
package com.hitech.dms.web.model.opex.approval.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class OpexBudgetAppResponseModel {
	private String msg;
	private Integer statusCode;
	private String approvalStatus;
}
