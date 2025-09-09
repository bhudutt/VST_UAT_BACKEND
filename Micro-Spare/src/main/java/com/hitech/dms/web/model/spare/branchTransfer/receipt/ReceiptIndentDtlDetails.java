package com.hitech.dms.web.model.spare.branchTransfer.receipt;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class ReceiptIndentDtlDetails {

	private BigInteger paIndDtlId;
	private String toStore;
	private String storeBinLocation;
}
