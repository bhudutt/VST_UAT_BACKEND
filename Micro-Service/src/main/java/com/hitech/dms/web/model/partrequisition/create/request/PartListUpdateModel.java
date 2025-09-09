package com.hitech.dms.web.model.partrequisition.create.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;
@Data
public class PartListUpdateModel {

	private Integer partBranchId;
	private BigInteger requisitionId;
	private  BigDecimal requestedQty;
}
