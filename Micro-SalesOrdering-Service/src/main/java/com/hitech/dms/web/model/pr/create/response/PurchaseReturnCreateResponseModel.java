/**
 * 
 */
package com.hitech.dms.web.model.pr.create.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PurchaseReturnCreateResponseModel {
	private BigInteger purchaseReturnId;
	private String purchaseReturnNumber;
	private String msg;
	private Integer statusCode;
}
