package com.hitech.dms.web.model.spare.inventory.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.spare.grn.mapping.response.PartNumberDetailResponse;

import lombok.Data;

@Data
public class SpareInventoryRequest {

	private Integer branchId;
	private BigInteger grnHdrId;
	private List<PartDetailRequest> saveSpareInventory;
	
}
