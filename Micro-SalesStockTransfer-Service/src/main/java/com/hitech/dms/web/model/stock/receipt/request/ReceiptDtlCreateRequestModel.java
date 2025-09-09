/**
 * 
 */
package com.hitech.dms.web.model.stock.receipt.request;

import java.math.BigInteger;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ReceiptDtlCreateRequestModel {
	@NotNull(message = "Receipt Issue Detail must not be blank.")
	private BigInteger issueDtlId;
}
