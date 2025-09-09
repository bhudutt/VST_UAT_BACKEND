/**
 * 
 */
package com.hitech.dms.web.model.machinepo.calculation.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachinePOTotalAmntResponseModel {
	private BigDecimal totalTcsAmount;
	private BigDecimal totalPoAmnt;
}
