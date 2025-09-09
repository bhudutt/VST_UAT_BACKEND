package com.hitech.dms.web.model.spare.grn.mapping.response;

import lombok.Data;

@Data
public class StoreResponse {

	private Integer branchStoreId;
	private String storeCode;
	private String storeDesc;
	private String binName;
	private Character isMainStore;
}
