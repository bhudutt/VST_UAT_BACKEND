/**
 * 
 */
package com.hitech.dms.web.model.aop.template.request;

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
public class AopTargetRequestModel {
	private Integer pcId;
	private String finYear;
	private int lastFinYearCount;
	private String isFor;
	private BigInteger aopId;
}
