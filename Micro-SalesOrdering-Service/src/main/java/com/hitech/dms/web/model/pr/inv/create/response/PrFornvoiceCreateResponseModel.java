/**
 * 
 */
package com.hitech.dms.web.model.pr.inv.create.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PrFornvoiceCreateResponseModel {
	private BigInteger purchaseReturnInvId;
	private String purchaseReturnInvNumber;
	private String msg;
	private Integer statusCode;
}
