package com.hitech.dms.web.model.common.search;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CommonFilterDealerModel {
	
	 private BigInteger parentDealerId;
	  private String parentDealerName;
	  private String parentDealerCode;
	  private BigInteger dealerStateId;

}
