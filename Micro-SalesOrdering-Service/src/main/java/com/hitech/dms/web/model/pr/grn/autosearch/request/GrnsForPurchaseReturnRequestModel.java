/**
 * 
 */
package com.hitech.dms.web.model.pr.grn.autosearch.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class GrnsForPurchaseReturnRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private String grnNumber;
	private String includeInactive;
}
