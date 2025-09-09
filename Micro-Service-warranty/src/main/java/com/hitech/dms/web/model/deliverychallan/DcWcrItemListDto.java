package com.hitech.dms.web.model.deliverychallan;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class DcWcrItemListDto {
	
	private BigInteger wcrId;
	private String wcrNo;
	private String partNo;
	private String partDesc;
	private String hsnCode;
	private BigDecimal rate;
	private BigDecimal qty;
	private BigDecimal value;
	private BigInteger gstPercentage;
	private BigDecimal gstAmount;
	
}
