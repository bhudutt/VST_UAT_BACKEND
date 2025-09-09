package com.hitech.dms.web.model.common.search;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;
@Data
public class CommonFilterDealerResponse {
	
	
	private List<CommonFilterDealerModel> dealerList;
	private BigInteger orgHierId;
	private Integer branchId;
	private String message;
	private int statusCode;

}
