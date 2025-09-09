/**
 * 
 */
package com.hitech.dms.web.model.inv.po.list.request;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class PoMachListForInvRequestModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private Integer pcId;
	private BigInteger toDealerId;
	private BigInteger poHdrId;
	private String poNumber;
}
