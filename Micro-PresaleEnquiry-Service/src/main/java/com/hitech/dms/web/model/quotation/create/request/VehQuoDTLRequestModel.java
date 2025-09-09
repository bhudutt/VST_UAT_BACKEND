/**
 * 
 */
package com.hitech.dms.web.model.quotation.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class VehQuoDTLRequestModel {
	private BigInteger enquiryHDRId;
	private VehQuoHDRRequestModel vehQuoHDR;

	private BigInteger machineItemId;

	private Integer qty;

	private BigDecimal unitRate;

	private BigDecimal basicValue;

	private BigDecimal grossAmount;

	private BigDecimal amountAfterDiscount;

	private float igstPer;
	private BigDecimal igstAmnt;
	private float sgstPer;
	private BigDecimal sgstAmnt;
	private float cgstPer;
	private BigDecimal cgstAmnt;

	private BigDecimal totalGstAmnt;

	private BigDecimal totalItemAmnt;
}
