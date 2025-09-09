package com.hitech.dms.web.model.storemaster.create.request;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class StoreMasterModel implements Serializable {

	private Integer branchStoreId;
	
	private Integer branchId;

	private String storeCode;

	private String storeDescription;

	private boolean blockedForTransaction;

	private boolean activeStatus;

	private Date createdDate;

	private String createdBy;

	private Date modifiedDate;

	private String modifiedBy;
	
	private Boolean mainStoreStatus;
}
