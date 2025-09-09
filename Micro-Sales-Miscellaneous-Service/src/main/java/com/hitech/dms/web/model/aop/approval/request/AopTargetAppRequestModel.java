/**
 * 
 */
package com.hitech.dms.web.model.aop.approval.request;

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
public class AopTargetAppRequestModel {
	@NotNull(message = "AOP Number/Id must not be blank.")
	private BigInteger aopId;
	@NotNull(message = "AOP Number must not be blank.")
	private String aopNumber;
	@NotNull(message = "AOP Approval Status must not be blank.")
	private String approvalStatus;
	@NotNull(message = "Remarks must not be blank.")
	private String remarks;
}
