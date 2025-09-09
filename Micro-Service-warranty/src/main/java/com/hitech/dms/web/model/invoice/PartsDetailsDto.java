package com.hitech.dms.web.model.invoice;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class PartsDetailsDto {
	private Integer partId;
	private String PartNumber;
	private String PartDesc;
	private String Uom;
	private String HsnCode;
	private BigDecimal unitPrice;
	private BigDecimal qty;
	private BigDecimal netAmt;
	private BigDecimal cgstPer;
	private BigDecimal cgstAmt;
	private BigDecimal sgstPer;
	private BigDecimal sgstAmt;
	private BigDecimal igstPer;
	private BigDecimal igstAmt;
	private BigDecimal totalAmt;
	private String billableTypeDesc;
	

}
