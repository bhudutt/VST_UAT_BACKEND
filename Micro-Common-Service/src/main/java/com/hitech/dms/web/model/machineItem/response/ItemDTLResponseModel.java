/**
 * 
 */
package com.hitech.dms.web.model.machineItem.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ItemDTLResponseModel {
	private BigInteger machineItemId;
	private String itemNo;
	private String itemDescription;
	private String modelName;
	private String segmentName;
	private String seriesName;
	private String variant;
	private BigInteger modelId;
	private Integer qty;
	private BigDecimal rate;
	private BigDecimal basicPrice;
	private BigDecimal discount;
	private BigDecimal taxableValue;
	private double cgstPercent;
	private BigDecimal cgstAmount;
	private double sgstPercent;
	private BigDecimal sgstAmount;
	private double igstPercent;
	private BigDecimal igstAmount;
	private BigDecimal gstAmount;
	private BigDecimal totalAmount;
}
