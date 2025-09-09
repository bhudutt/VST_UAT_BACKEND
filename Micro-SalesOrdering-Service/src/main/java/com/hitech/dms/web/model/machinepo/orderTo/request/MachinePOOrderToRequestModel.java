/**
 * 
 */
package com.hitech.dms.web.model.machinepo.orderTo.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class MachinePOOrderToRequestModel {
	private BigInteger poHdrId;
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String includeInActive;
}
