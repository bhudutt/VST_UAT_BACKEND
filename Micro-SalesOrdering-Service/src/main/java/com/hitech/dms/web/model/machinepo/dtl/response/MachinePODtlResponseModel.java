package com.hitech.dms.web.model.machinepo.dtl.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class MachinePODtlResponseModel {
	private BigInteger poDtlId;
	private BigInteger machineItemId;
	private Integer quantity;
	private BigDecimal unitPrice;
	private BigDecimal mrpPrice;
	private BigDecimal discountAmount;
	private BigDecimal netAmount;
	private BigDecimal gstPercent;
	private BigDecimal gstAmount;
	private BigDecimal totalAmount;
	private String itemNumber;
	private String itemDescription;
	private String variant;
	
}
