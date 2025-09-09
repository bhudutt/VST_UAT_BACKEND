package com.hitech.dms.web.model.spare.sale.invoice.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;

import lombok.Data;

@Data
public class SpareSalesInvoiceRequest {

	private BigInteger branchId;
	private BigInteger referenceDocumentId;
	private BigInteger productCategoryId;
	private String productCategoryCode;
	private BigInteger partyTypeId;
	private String partyCode;
	private String partyName;
	private String poNumber;
	private Date poDate;
	private BigInteger poHdrId;
	private BigInteger customerOrderId;
	private String customerName;
	private String mobileNo;
	private String address;
	private String state;
	private String district;
	private String tehsil;
	private String city;
	private String postOffice;
	private BigInteger pinId;
	private String pinCode;
	private String totalPart;
	private String totalQuantity;
	private String transporterName;
	private String transporterVehicleNo;
	private String lrNo;
	private String splDiscountType;
	private BigDecimal splDiscountPercent;
	private BigDecimal totalSplDiscountValue;
	private BigDecimal totalBasicValue;
	private BigDecimal totalNetValue;
	private BigDecimal totalDiscountValue;
	private BigDecimal otherCharges;
	private BigDecimal totalTaxValue;
	private BigDecimal totalTaxableAmount;
	private BigDecimal tcsPercent;
	private BigDecimal tcsAmount;
	private BigDecimal totalBillValue;
	private BigInteger customerId;
	private BigDecimal forwardingPackagingAmount;
	private BigDecimal forwardingPackagingGst;
	private BigDecimal forwardGstAmount;
    
	private List<PartDetailRequest> partDetails;

//	private List<CustomerOrderOrDCRequest> customerOrderList;

}
