package com.hitech.dms.web.model.quotation.view.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class VehQuoDTLViewResponseModel {
	private BigInteger quotationId;
	private BigInteger enquiryId;
	private String enquiryNumber;
	private String enquiryDate;
	private String modelName;
	private String sourceOfEnquiry;
	private String dspName;
	private String expectedPurchaseDate;
	private String enquiryType;
	private String enquiryStage;
	private BigInteger quotationDtlId;
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
