/**
 * 
 */
package com.hitech.dms.web.model.quotation.view.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class VehQuoImplementViewResponseModel {
	private BigInteger quotationImplementId;

	private BigInteger machineItemId;

	private String itemNumber;

	private String itemDescription;

	private Integer qty;

	private BigDecimal unitRate;

	private BigDecimal basicValue;

	private BigDecimal grossAmount;

	private BigDecimal amountAfterDiscount;

	private Double igstPer;
	private BigDecimal igstAmnt;
	private Double sgstPer;
	private BigDecimal sgstAmnt;
	private Double cgstPer;
	private BigDecimal cgstAmnt;

	private BigDecimal totalGstAmnt;

	private BigDecimal totalItemAmnt;
}
