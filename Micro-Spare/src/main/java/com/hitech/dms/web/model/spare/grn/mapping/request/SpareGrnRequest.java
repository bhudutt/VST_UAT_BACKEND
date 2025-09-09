package com.hitech.dms.web.model.spare.grn.mapping.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;

import lombok.Data;

@Data
public class SpareGrnRequest {

	private BigInteger branchId;
	private BigInteger grnTypeId;
	private String grnNumber;
	private Date grnDate;
	private BigInteger partyId;
	private BigInteger branchStoreId;
	private String invoiceNumber;
	private Date invoiceDate;
	private BigDecimal invoiceAmount;
	private String driverName;
	private String driverMobNo;
	private String transporterName;
	private String transporterVehicleNo;
	private String poNumber;
	private Date poDate;
	private String lrNo;
	private Date lrDate;
	private BigDecimal grnValue;
	private String status;
	private BigInteger poPlantId;
	private BigDecimal totalDiscountValue;
	private BigDecimal totalTaxValue;
	private BigDecimal totalBasicValue;
	private List<PartNumberDetailResponse> partNumberDetailResponseList;
	
}
