package com.hitech.dms.web.model.spare.grn.mapping.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SpareGrnDetailsResponse {

//	private String action;
//	private BigInteger  grnDtlId;
	private BigInteger id;
	private String grnNumber;
	private String grnDate;
	private BigDecimal grnValue;
	private String grnFrom;
	private String status;
//	private String binName;
	private String invoiceNo;
	private String invoiceDate;
	private String partyCategoryName;
	private String partyCategoryCode;
	private String store;
	private String transporterName;
	private String transporterVehicle;
	private String productCategory;
	private String driverName;
	private String driverMobNo;
	private String supplier;
	private BigInteger specialDiscount;
	private BigInteger transportCharges;
	private BigInteger transportGST;
	private BigInteger tcsValue;
	private BigDecimal InvoiceAmount;
	private List<PartNumberDetailResponse> partNumberDetailResponse;
}
