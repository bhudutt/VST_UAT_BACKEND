package com.hitech.dms.web.model.spare.sale.invoice.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;
import com.hitech.dms.web.model.spara.customer.order.response.CustomerOrderNumberResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DeliveryChallanNumberResponse;

import lombok.Data;

@Data
public class SpareSalesInvoiceResponse {

	private BigInteger id;
	private String action;
	private String invoiceNumber;
	private String invoiceDate;
	private String partyName;
	private String InvoiceStatus;
	private BigInteger branchId;
	private String branchName;
	private String docType;
	private BigInteger docTypeId;
	private String partyCategory;
	private String partyCode;
	private String poNumber;
	private BigInteger poHdrId;
	private String poDate;
	private String customerName;
	private String customerMobileNumber;
	private List<CustomerOrderNumberResponse> customerOrderResponse;
	private List<DeliveryChallanNumberResponse> dcResponse;
	private List<PartDetailRequest> saleInvoiceDtl;
	private String pinCode;
	private String address;
	private String State;
	private String District;
	private String Tehsil;
	private String City;
	private String PostOffice;
	private String transporterName;
	private String transporterVehicleNo;
	private String lrNo;
	private BigDecimal otherCharges;
	private BigDecimal totalBaseAmount;
	private BigDecimal tDiscountValue;
	private BigDecimal netBaseAmount; 
	private BigDecimal specialDiscountPercent;
	private BigDecimal specialDiscountAmount;
	private BigDecimal totalTaxableAmount;
	private BigDecimal totalGstAmount;
	private BigDecimal tcsPercent; 
	private BigDecimal tcsAmount; 
	private BigDecimal totalInvocieAmount;
	private BigDecimal taxableAmount;
	private Integer totalPart;
	private BigDecimal totalQty;
	private Boolean isCancelled;
	private String cancellationRemarks;
	private String qRCodeText;
	private Boolean isQRCodeGenerated;
	private Integer totalCount;
	private BigDecimal forwardingPackagingAmount;
	private BigDecimal forwardingPackagingGst;
	private BigDecimal forwardGstAmount;
    
	
	private int statusCode;
	private String msg;
}
