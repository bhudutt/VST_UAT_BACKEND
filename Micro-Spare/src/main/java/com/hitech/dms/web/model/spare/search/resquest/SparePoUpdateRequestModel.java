/**
 * 
 */
package com.hitech.dms.web.model.spare.search.resquest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hitech.dms.web.model.SpareModel.SparePoPartDetailsModel;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SparePoUpdateRequestModel {
	@JsonProperty(value = "poHdrId")
	private BigInteger poHdrId;
	@JsonProperty(value = "branchId")
	private BigInteger branchId;
	@JsonProperty(value = "poNumber")
	private String poNumber;
	@JsonProperty(value = "poStatus")
	private String poStatus;
	@JsonProperty(value = "poON")
	private String poON;
	@JsonProperty(value = "pOType")
	private String pOType;
	@JsonProperty(value = "productCategory")
	private String productCategory;
	@JsonProperty(value = "partyCode")
	private String partyCode;
	@JsonProperty(value = "partyName")
	private String partyName;
	@JsonProperty(value = "jobCardNo")
	private String jobCardNo;
	@JsonProperty(value="rsoNumber")
	private int rsoNumber;
	@JsonProperty(value = "remarks")
	private String remarks;
	@JsonProperty(value = "totelItem")
	private int totelItem;
	@JsonProperty(value = "totalQty")
	private int totalQty;
	@JsonProperty(value = "poReleaseDate")
	private Date poReleaseDate;
	@JsonProperty(value = "totalBaseAmount")
	private BigDecimal totalBaseAmount;
	@JsonProperty(value = "totalGstAmount")
	private BigDecimal totalGstAmount;
	@JsonProperty(value = "tcsPercent")
	private String tcsPercent;
	@JsonProperty(value = "totalTcsAmount")
	private BigDecimal totalTcsAmount;
	@JsonProperty(value = "totalPoAmount")
	private BigDecimal totalPoAmount;
	private List<SparePoPartDetailsModel> sparePODtlList;
	@JsonProperty(value = "action", required = true, defaultValue = "Save")
	private String action;
}
