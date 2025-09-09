package com.hitech.dms.web.spare.grn.model.mapping.response;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class SpareGrnPODetailsReponse {

	private String poNumber;
	private BigInteger poHdrId;
	private String productCategory;
	private Date pOCreationDate;
	private String pOStatus;
}
