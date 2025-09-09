/**
 * 
 */
package com.hitech.dms.web.model.machinepo.calculation.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachinePOTotalAmntRequestModel {
	private Integer pcId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private BigDecimal tcsPer;
	private BigDecimal totalBaseAmount;
	private BigDecimal totalGstAmount;
}
