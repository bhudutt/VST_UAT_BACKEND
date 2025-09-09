/**
 * 
 */
package com.hitech.dms.web.model.machinepo.calculation.response;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class MachinePOItemAmntResponseModel {
	private BigDecimal netAmount;
	private BigDecimal itemGstAmount;
	private Double itemGstPer;
	private BigDecimal totalAmount;
}
