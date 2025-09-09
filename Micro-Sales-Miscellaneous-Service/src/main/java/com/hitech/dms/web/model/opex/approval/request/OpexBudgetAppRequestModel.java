/**
 * 
 */
package com.hitech.dms.web.model.opex.approval.request;

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
public class OpexBudgetAppRequestModel {
	@NotNull(message = "OPEX Number/Id must not be blank.")
	private BigInteger opexId;
	@NotNull(message = "OPEX Number must not be blank.")
	private String opexNumber;
	@NotNull(message = "OPEX Approval Status must not be blank.")
	private String approvalStatus;
	@NotNull(message = "Remarks must not be blank.")
	private String remarks;
}
