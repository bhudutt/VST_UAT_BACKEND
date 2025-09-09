/**
 * 
 */
package com.hitech.dms.web.model.dealerdtl.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
public class DealerDTLResponseModel {
	private BigInteger dealerId;
	private String dealerName;
	private String dealerCode;
	private String dealerLocation;
	private String dealerType;
}
