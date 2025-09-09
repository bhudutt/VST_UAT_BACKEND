/**
 * 
 */
package com.hitech.dms.web.model.geo.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class GeoDistricDTLRequestModel {
	private Long dealerID;
	private BigInteger stateId;
}
