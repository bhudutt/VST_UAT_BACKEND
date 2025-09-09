/**
 * 
 */
package com.hitech.dms.web.model.pr.inv.dtl.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PrDtlFornvoiceRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger purchaseReturnId;
	private String purchaseReturnNumber;
	private int flag;
}
