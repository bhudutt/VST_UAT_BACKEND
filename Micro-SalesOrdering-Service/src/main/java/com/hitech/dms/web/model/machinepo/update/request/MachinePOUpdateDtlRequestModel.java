/**
 * 
 */
package com.hitech.dms.web.model.machinepo.update.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class MachinePOUpdateDtlRequestModel {
	@JsonProperty(value = "poDtlId", required = true)
	private BigInteger poDtlId;
	@JsonProperty(value = "machineItemId", required = true)
	private BigInteger machineItemId;
	@JsonProperty(value = "quantity", required = true)
	private Integer quantity;
	@JsonProperty(value = "unitPrice", required = true)
	private BigDecimal unitPrice;
	@JsonProperty(value = "mrpPrice", required = true)
	private BigDecimal mrpPrice;
	@JsonProperty(value = "discountAmount", required = true)
	private BigDecimal discountAmount;
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
