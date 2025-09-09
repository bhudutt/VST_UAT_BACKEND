package com.hitech.dms.web.model.dealer.user.search.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class DealerSearchResponseModel {

	private String action;
	private BigInteger id;
	private String empCode;
	private String empName;
//	private BigInteger userId;
	private String status;
	private Character isActive;

}
