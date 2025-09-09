package com.hitech.dms.web.model.SpareModel;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.Data;
@Data
public class SparePoPartDetailsModel {
	@JsonProperty(value = "partNo", required = true)
	private String partNo;
	@JsonProperty(value = "partId", required = true)
	private String partId;
	@JsonProperty(value = "partDescription", required = true)
	private String partDescription;
	@JsonProperty(value = "prodSubCat", required = true)
	private String prodSubCat;
	@JsonProperty(value = "packQty", required = true)
	private Integer packQty;
	@JsonProperty(value = "minOrderQty", required = true)
	private Integer minOrderQty;
	@JsonProperty(value = "currentStock", required = true)
	private Integer currentStock;
	@JsonProperty(value = "backOrderQty", required = true)
	private Integer backOrderQty;
	@JsonProperty(value = "transitQty", required = true)
	private Integer transitQty;
	@JsonProperty(value = "mrpPrice", required = true)
	private BigDecimal mrpPrice;
	@JsonProperty(value = "quantity", required = true)
	private Integer quantity;
	@JsonProperty(value = "netAmount", required = true)
	private BigDecimal netAmount;
	@JsonProperty(value = "gstPercent", required = true)
	private BigDecimal gstPercent;
	@JsonProperty(value = "gstAmount", required = true)
	private BigDecimal gstAmount;
	private Boolean deleteFlag;
	@JsonProperty(value = "totalAmount", required = true)
	private BigDecimal totalAmount;
}
