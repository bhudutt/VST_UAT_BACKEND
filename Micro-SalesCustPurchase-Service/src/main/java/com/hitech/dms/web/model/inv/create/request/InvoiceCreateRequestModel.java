/**
 * 
 */
package com.hitech.dms.web.model.inv.create.request;

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
public class InvoiceCreateRequestModel {
	private BigInteger dealerId;
	@JsonProperty(value = "branchId", required = true)
	private BigInteger branchId;
	private Integer pcId;
	@JsonProperty(value = "invoiceTypeId", required = true)
	private Integer invoiceTypeId;
	@JsonProperty(value = "invoiceDate", required = true)
	//@JsonDeserialize(using = DateHandler.class)
	private Date invoiceDate;
	private BigInteger customerId;
	private BigInteger toDealerId;
	private BigInteger toPoHdrId;
	private BigDecimal insuranceCharges = BigDecimal.ZERO;
	private BigDecimal rtoCharges = BigDecimal.ZERO;
	private BigDecimal hsrpCharges = BigDecimal.ZERO;
	private BigDecimal otherCharges = BigDecimal.ZERO;
	private BigDecimal totalBasicAmnt;
	private BigDecimal totalGstAmnt;
	private BigDecimal totalInvoiceAmnt;
	private BigInteger financerId;
	private BigInteger insuranceMasterId;
	@JsonDeserialize(using = DateHandler.class)
	private Date policyStartDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date policyExpiryDate;
	private String remarks;
	private List<InvoiceMachDtlCreateRequestModel> machineInvoiceDtlList;
	private List<InvoiceItemDtlCreateRequestModel> machineInvoiceItemDtlList;
}
