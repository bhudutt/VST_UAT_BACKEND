package com.hitech.dms.web.model.invoice;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class HandlingChargeDto {

	private String material;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal amount;
    private String sac;
    private BigDecimal taxPercent;
    private BigDecimal taxValue;
    private BigDecimal totalValue;
}
