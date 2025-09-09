/**
 * 
 */
package com.hitech.dms.web.model.pr.inv.view.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PrForInvoiceViewRequestModel {
	private BigInteger purchaseReturnInvId;
	private String purchaseReturnInvNumber;
	private int flag;
}
