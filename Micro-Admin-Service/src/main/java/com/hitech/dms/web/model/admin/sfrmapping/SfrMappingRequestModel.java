package com.hitech.dms.web.model.admin.sfrmapping;

import lombok.Data;

@Data
public class SfrMappingRequestModel {

	private Integer pcID;
	private Integer dealerId;
	private String dealerCode;
	private String branchCode;
	private String isInactiveInclude;
}
