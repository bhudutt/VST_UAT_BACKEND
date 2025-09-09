/**
 * 
 */
package com.hitech.dms.web.model.aop.template.submit.request;

import java.math.BigInteger;

import javax.validation.constraints.NotNull;

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
public class AopTargetSubmitRequestModel {
	@NotNull(message = "AOP Number/Id must not be blank.")
	private BigInteger stgAopId;
	@NotNull(message = "AOP Number must not be blank.")
	private String stgAopNumber;
	private BigInteger aopId;
}
