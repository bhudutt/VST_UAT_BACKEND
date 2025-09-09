package com.hitech.dms.web.model.admin.aprmapping;

import lombok.Data;

@Data
public class AprMappingRequestModel {

	private Integer pcID;
	private Integer dealerId;
	private String dealerCode;
	private String branchCode;
	private String isInactiveInclude;
}
