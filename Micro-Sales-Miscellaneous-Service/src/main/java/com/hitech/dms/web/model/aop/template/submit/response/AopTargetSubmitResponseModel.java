/**
 * 
 */
package com.hitech.dms.web.model.aop.template.submit.response;

import java.math.BigInteger;

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
public class AopTargetSubmitResponseModel {
	private BigInteger aopId;
	private String aopNumber;
	private String msg;
	private int statusCode;
}
