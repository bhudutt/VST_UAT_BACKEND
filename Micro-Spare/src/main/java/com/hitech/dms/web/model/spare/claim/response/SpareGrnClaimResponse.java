package com.hitech.dms.web.model.spare.claim.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.entity.spare.grn.SpareGrnClaimPhotosEntity;

import lombok.Data;

@Data
public class SpareGrnClaimResponse {

	private String action;
	private BigInteger id;
	private String claimNumber;
	private String claimDate;
	private String dealerName;
	private String claimStatus;
	private String grnNumber;
	private String grnDate;
	private String invoiceNumber;
	private String invoiceDate;
	private String partyCategoryName;
	private String partyCategoryCode;
	private Character isAgree;
//	private String store;
	private String transporterName;
	private String transporterVehicle;
	private String productCategory;
	private String driverName;
	private String driverMobNo;
	private String supplier;
	private BigDecimal invoiceAmount;
	private String claimType;
	private BigInteger branchId;
	private List<SpareGrnClaimPhotosEntity> spareGrnClaimPhotos;
}
