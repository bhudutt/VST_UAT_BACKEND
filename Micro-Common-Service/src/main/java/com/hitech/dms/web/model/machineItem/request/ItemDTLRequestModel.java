/**
 * 
 */
package com.hitech.dms.web.model.machineItem.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ItemDTLRequestModel {
	private BigInteger itemId;
	private BigInteger branchId;
	private BigInteger customerId;
}
