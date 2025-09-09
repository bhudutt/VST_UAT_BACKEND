package com.hitech.dms.web.model.spare.grn.mapping.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class SpareGrnHdr {

	private BigInteger grnHdrId;
	private BigDecimal grnValue;
}
