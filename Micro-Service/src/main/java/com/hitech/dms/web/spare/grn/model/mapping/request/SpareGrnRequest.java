package com.hitech.dms.web.spare.grn.model.mapping.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.spare.grn.model.mapping.response.PartNumberDetailResponse;

import lombok.Data;

@Data
public class SpareGrnRequest {

	private BigInteger branchId;
	private BigInteger grnTypeId;
	private String grnNumber;
	private Date grnDate;
	private BigInteger partyBranchId;
	private BigInteger branchStoreId;
	private String invoiceNumber;
	private Date invoiceDate;
	private String driverName;
	private String driverMobNo;
	private String transporter;
	private String poNumber;
	private Date poDate;
	private String lrNo;
	private Date lrDate;
	private BigDecimal grnValue;
	private String status;
	private List<PartNumberDetailResponse> partNumberDetailResponseList;
	
}
