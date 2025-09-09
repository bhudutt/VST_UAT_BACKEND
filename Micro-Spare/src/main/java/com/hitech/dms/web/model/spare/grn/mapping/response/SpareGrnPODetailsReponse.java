package com.hitech.dms.web.model.spare.grn.mapping.response;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class SpareGrnPODetailsReponse {

	private String poNumber;
	private String invoiceNo;
	private BigInteger poHdrId;
	private String productCategory;
	private Date pOCreationDate;
	private String pOStatus;
}
