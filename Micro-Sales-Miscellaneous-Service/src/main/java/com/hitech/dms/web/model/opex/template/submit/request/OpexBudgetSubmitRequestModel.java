/**
 * 
 */
package com.hitech.dms.web.model.opex.template.submit.request;

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
public class OpexBudgetSubmitRequestModel {
	@NotNull(message = "OPEX Number/Id must not be blank.")
	private BigInteger stgOpexId;
	@NotNull(message = "OPEX Number must not be blank.")
	private String stgOpexNumber;
	private BigInteger opexId;
}
