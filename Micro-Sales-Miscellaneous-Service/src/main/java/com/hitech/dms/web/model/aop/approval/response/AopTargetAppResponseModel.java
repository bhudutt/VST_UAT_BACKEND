/**
 * 
 */
package com.hitech.dms.web.model.aop.approval.response;

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
public class AopTargetAppResponseModel {
	private String msg;
	private Integer statusCode;
	private String approvalStatus;
}
