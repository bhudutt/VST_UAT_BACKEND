/**
 * 
 */
package com.hitech.dms.web.model.pr.grn.dtl.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class GrnDtlForPurchaseReturnRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private String grnNumber;
	private BigInteger grnId;
	private String includeInactive;
	private int flag;
}
