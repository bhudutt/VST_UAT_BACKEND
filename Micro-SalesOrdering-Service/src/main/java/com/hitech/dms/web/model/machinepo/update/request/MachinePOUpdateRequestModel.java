/**
 * 
 */
package com.hitech.dms.web.model.machinepo.update.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class MachinePOUpdateRequestModel {
	private BigInteger poHdrId;
	private String poNumber;
	@JsonProperty(value = "dealerId", required = true)
	private BigInteger dealerId;
	@JsonProperty(value = "pcId", required = true)
	private Integer pcId;
	@JsonProperty(value = "poTypeId", required = true)
	private Integer poTypeId;
	private BigInteger poToDealerId;
	private String poToPartyName;
	@JsonDeserialize(using = DateHandler.class)
	private Date poDate;
	private Integer poPlantId;
	@JsonProperty(value = "basicAmount", required = true)
	private BigDecimal basicAmount;
	@JsonProperty(value = "totalGstAmount", required = true)
	private BigDecimal totalGstAmount;
	private BigDecimal tcsPercent;
	private BigDecimal tcsValue;
	private BigDecimal totalAmount;
	private List<MachinePOUpdateDtlRequestModel> machinePODtlList;
	@JsonProperty(value = "action", required = true, defaultValue = "Submit")
	private String action;
}
