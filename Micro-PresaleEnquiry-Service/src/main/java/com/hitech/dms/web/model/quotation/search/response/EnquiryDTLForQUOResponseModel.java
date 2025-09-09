/**
 * 
 */
package com.hitech.dms.web.model.quotation.search.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryDTLForQUOResponseModel {
	private BigInteger enquiryId;
	private String enqNumber;
	private String enqDate;
	private String modelName;
	private String sourceOfEnq;
	private String salesman;
	private String expectedPurchaseDate;
	private String prospectType;
	private String enqStage;
	private BigInteger machineItemId;
	private String itemNo;
	private String itemDesc;
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
