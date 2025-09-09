/**
 * 
 */
package com.hitech.dms.web.model.allot.item.dtl.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class ItemDtlForAllotRequestModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private Integer pcId;
	private BigInteger machineItemId;
}
