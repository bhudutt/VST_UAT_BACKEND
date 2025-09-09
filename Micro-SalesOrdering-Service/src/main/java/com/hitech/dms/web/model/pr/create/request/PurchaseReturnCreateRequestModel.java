/**
 * 
 */
package com.hitech.dms.web.model.pr.create.request;

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
public class PurchaseReturnCreateRequestModel {
	@JsonProperty(value = "dealerId", required = true)
	private BigInteger dealerId;
	private String dealerCode;
	@JsonDeserialize(using = DateHandler.class)
	private Date purchaseReturnDate;
	@JsonProperty(value = "grnId", required = true)
	private BigInteger grnId;
	@JsonProperty(value = "grnNumber", required = true)
	private String grnNumber;
	@JsonDeserialize(using = DateHandler.class)
	private Date grnDate;
	private Integer grnTypeId;
	private String grnType;
	private String grnStatus;
	private BigInteger invoiceId;
	private String invoiceNumber;
	private String invoiceType;
	@JsonDeserialize(using = DateHandler.class)
	private Date invoiceDate;
	private String displayValue;
	private String partyCode;
	private String partyName;
	private String transporterName;
	private String driverName;
	private String driverMobileNo;
	private String transporterVehicleNo;
	@JsonProperty(value = "grossTotalValue", required = true)
	private BigDecimal grossTotalValue;
	@JsonProperty(value = "pcId", required = true)
	private Integer pcId;
	private String pcDesc;
	private List<PurchaseReturnMachDtlCreateRequestModel> salesMachineGrnDtlList;
	private List<PurchaseReturnItemDtlCreateRequestModel> salesMachineGrnImplDtlList;
}
