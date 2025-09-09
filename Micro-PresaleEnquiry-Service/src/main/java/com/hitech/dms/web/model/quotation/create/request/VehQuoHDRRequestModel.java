/**
 * 
 */
package com.hitech.dms.web.model.quotation.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class VehQuoHDRRequestModel {
	// QuoMapId 
	@JsonProperty(value = "branchId", required = true)
	@NotNull(message = "Branch Is Required")
	private BigInteger branchId;
	@JsonProperty(value = "pcId", required = true)
	private Integer pcId;
	@JsonProperty(value = "customerId", required = true)
	private BigInteger customerId;
	private String customerCode;

	private BigDecimal totalBasicValue;

	private BigDecimal totalDiscount;

	private BigDecimal totalTaxableAmount;

	private BigDecimal totalGstAmount;

	@JsonProperty(value = "totalCharges", required = true)
	private BigDecimal totalCharges;

	@JsonProperty(value = "totalAmount", required = true)
	private BigDecimal totalAmount;

	private List<VehQuoChargesRequestModel> vehQuoChrgList;
	@JsonProperty(value = "vehQuoDTLList", required = true)
	private List<VehQuoDTLRequestModel> vehQuoDTLList;

	private List<VehQuoImplementRequestModel> vehQuoImplementList;
}
