package com.hitech.dms.web.model.dealer.user.create.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class DealerUserCreateResponseModel {
	private String msg;
	private Integer statusCode;
	private String userCode;
	private BigInteger userId;

}
