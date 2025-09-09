/**
 * 
 */
package com.hitech.dms.web.model.indent.create.request;

import java.math.BigInteger;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class IndentDtlCreateRequestModel {
	@NotNull(message = "Item must not be blank.")
	private BigInteger machineItemId;
	@NotNull(message = "Indent Qty. must not be blank.")
	private Integer indentQty;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndentDtlCreateRequestModel other = (IndentDtlCreateRequestModel) obj;
		return Objects.equals(machineItemId, other.machineItemId);
	}
	@Override
	public int hashCode() {
		return Objects.hash(machineItemId);
	}
}
