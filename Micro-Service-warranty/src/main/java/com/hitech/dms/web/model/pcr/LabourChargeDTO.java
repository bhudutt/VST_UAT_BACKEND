package com.hitech.dms.web.model.pcr;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class LabourChargeDTO {
	private Long srNo;
    private String labourCode;
    private String labourDescription;
    private BigInteger hours;
    private Integer claimQty;
    private BigDecimal rate;
    private BigDecimal totalAmt;

}
