/**
 * 
 */
package com.hitech.dms.web.model.issue.create.request;

import java.math.BigInteger;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@EqualsAndHashCode(exclude = {"isSelected", "indentQty", "machineItemId"})
public class IssueDtlCreateRequestModel {
	@NotNull(message = "Machine must not be blank.")
	private BigInteger machineItemId;
	@NotNull(message = "Chassis No. must not be blank.")
	private BigInteger vinId;
	private Boolean isSelected;
	private Integer indentQty;
}
