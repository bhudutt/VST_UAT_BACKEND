/**
 * 
 */
package com.hitech.dms.web.model.indent.create.request;

import java.math.BigInteger;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@EqualsAndHashCode(exclude = { "indentQty" })
public class IndentItemCreateRequestModel {
	private BigInteger machineItemId;
	private Integer indentQty;
}
