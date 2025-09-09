/**
 * 
 */
package com.hitech.dms.web.model.pr.inv.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class PrAutoListFornvoiceRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private String purchaseReturnNumber;
	private String includeInactive;
}
