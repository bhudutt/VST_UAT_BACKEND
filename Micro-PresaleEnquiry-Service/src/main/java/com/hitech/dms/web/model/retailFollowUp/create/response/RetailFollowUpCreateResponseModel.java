package com.hitech.dms.web.model.retailFollowUp.create.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class RetailFollowUpCreateResponseModel {
	private BigInteger retailFollowUpDtlId;
	private String msg;
	private int statusCode;
}
