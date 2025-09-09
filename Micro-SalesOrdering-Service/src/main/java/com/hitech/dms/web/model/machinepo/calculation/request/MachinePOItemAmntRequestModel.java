/**
 * 
 */
package com.hitech.dms.web.model.machinepo.calculation.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class MachinePOItemAmntRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private BigInteger itemId;
	private Integer qty;
}
