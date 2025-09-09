package com.hitech.dms.web.model.partmaster.create.request;

import java.util.Date;

import lombok.Data;

@Data
public class PartBranchStoreModel {

	//private PartsADMBranchModel partsADMBranchModel;

	private String storeCode;

	private String storeDescription;

	private boolean blockedForTransaction;

	private boolean activeStatus;

	private Date createdDate;

	private String createdBy;

	private Date modifiedDate;

	private String modifiedBy;
}
