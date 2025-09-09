/**
 * 
 */
package com.hitech.dms.web.model.grn.create.request;

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
public class SalesGrnCreateRequestModel {
	@JsonProperty(value = "dealerId", required = true)
	private BigInteger dealerId;
	@JsonProperty(value = "pcId", required = true)
	private Integer pcId;
	@JsonProperty(value = "grnTypeId", required = true)
	private Integer grnTypeId;
	@JsonDeserialize(using = DateHandler.class)
	private Date grnDate;
	private BigInteger invoiceId;
	private BigInteger coDealerInvoiceId;
	private BigInteger erpInvoiceHdrId;
	private BigInteger purchaseReturnInvId;
	private String invoiceNumber;
	@JsonDeserialize(using = DateHandler.class)
	private Date invoiceDate;
	private String partCode;
	private String partyName;
	private String plantCode;
	private String transporterName;
	private String driverName;
	private String driverMobileNo;
	private String transporterVehicleNo;
	@JsonProperty(value = "grossTotalValue", required = true)
	private BigDecimal grossTotalValue;
	private List<SalesGrnDtlCreateRequestModel> salesMachineGrnDtlList;
	private List<SalesGrnItemDtlCreateRequestModel> salesMachineGrnImplDtlList;
}
