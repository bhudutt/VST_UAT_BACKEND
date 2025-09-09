/**
 * 
 */
package com.hitech.dms.web.model.aop.view.request;

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
public class AopTargetViewRequestModel {
	private BigInteger aopId;
	private String aopNumber;
	private int flag;
}
