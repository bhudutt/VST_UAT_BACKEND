package com.hitech.dms.web.model.spare.inventory.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;

import lombok.Data;

@Data
public class SpareInventorySearchRequest {

	private String grnNumber; 
	private String inventoryNumber;
	private String fromDate;
	private String toDate; 
	private String userCode;
	private Integer page;
	private Integer size;
	private Integer pcId;
	private Integer hoId;
	private Integer zoneId;
	private Integer stateId;
	private Integer territoryId;
	
}
