/**
 * 
 */
package com.hitech.dms.web.model.spare.create.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author santosh.kumar
 *
 */
@Data
public class SparePoDealerAndDistributerSearchResponse {
	private BigInteger parentDealerId;
	private String parentDealerCode;
	private String parentDealerName;

}
