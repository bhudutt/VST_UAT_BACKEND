/**
 * 
 */
package com.hitech.dms.web.model.pr.inv.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PrAutoListFornvoiceResponseModel {
	private BigInteger purchaseReturnId;
	private String purchaseReturnNumber;
	private String displayValue;
}
