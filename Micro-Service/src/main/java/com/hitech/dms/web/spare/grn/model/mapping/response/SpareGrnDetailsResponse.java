package com.hitech.dms.web.spare.grn.model.mapping.response;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class SpareGrnDetailsResponse {

	private String grnNumber;
	private Date grnDate;
	private BigDecimal grnValue;
	private String status;
	private String binName;
	private String InvoiceNo;
	private Date InvoiceDate;
	private String PartyCategoryName;
	private String transporterName;
	private String transporterVehicle;
	private String productCategory;
	private String driverName;
	private String driverMobNo;

}
