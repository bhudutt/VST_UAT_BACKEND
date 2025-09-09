/**
 * 
 */
package com.hitech.dms.web.model.pr.view.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PurchaseReturnViewRequestModel {
	private BigInteger purchaseReturnId;
	private String purchaseReturnNumber;
	private int flag;
}
