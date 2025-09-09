/**
 * 
 */
package com.hitech.dms.web.model.dealer;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class UserDealerResponseModel {
	private BigInteger dealerId;
	private String dealerCode;
	private String dealerName;
	private String dealerLocation;
}
