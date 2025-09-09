package com.hitech.dms.web.model.spare.inventory.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class SpareGrnInventoryResponse {

	private BigInteger id;
	private String inventoryTransferNumber;
	private String inventoryTransferDate;
	private String grnNumber;
	private String grnDate;	
	private String status;
	private String invoiceNo;
	private String invoiceDate;
	private String partyCategoryName;
	private String partyCategoryCode;
	private String transporterName;
	private String transporterVehicle;
	private String productCategory;
	private String store;
	private String driverName;
	private String driverMobNo;
	private String supplierPoNumber;
	private String msg;
	private Integer statusCode;
	private BigDecimal invoiceAmount;
}
