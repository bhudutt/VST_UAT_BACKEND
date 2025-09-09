package com.hitech.dms.web.model.pr.grn.autosearch.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class GrnsForPurchaseReturnResponseModel {
	private BigInteger grnId;
	private String grnNumber;
	private String grnType;
	private String invoiceNumber;
	private String displayValue;
}
