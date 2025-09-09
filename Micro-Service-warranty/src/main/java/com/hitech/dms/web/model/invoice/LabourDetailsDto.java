package com.hitech.dms.web.model.invoice;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class LabourDetailsDto {
	private Integer labourId;
    private String labourCode;
    private String labourDescription;
    private String hsnCode;
    private BigDecimal rate;
    private BigDecimal hour;
    private BigDecimal netAmt;
    private BigDecimal cgstPer;
	private BigDecimal cgstAmt;
	private BigDecimal sgstPer;
	private BigDecimal sgstAmt;
	private BigDecimal igstPer;
	private BigDecimal igstAmt;
	private BigDecimal totalAmt;

}
