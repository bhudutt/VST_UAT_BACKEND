package com.hitech.dms.web.model.masterdata.request;

import lombok.Data;

@Data
public class MasterDataModelRequest {
	private String searchOnValue;
	private String criteria;
	private String formName;
	private String forTest;
	private String vinId;
	private String totalKMReading;
	private String docType;
	private String rId;
	//validate service
	private String amcStatus;
	private String isFor;
	private String modelFamilyId;
	private String serviceTypeId;
	private String warrantyType;
	//validate part
	private String branchId;
	private String partNumber;
	private String billabillType;
	private String chassisNo;
	private String labourGrp;
	//part
	private String partType;
	private String partDivision;
	private String partCategoryId;
	private String orderToOemFlag;
	private String partCategoryCode;
}
