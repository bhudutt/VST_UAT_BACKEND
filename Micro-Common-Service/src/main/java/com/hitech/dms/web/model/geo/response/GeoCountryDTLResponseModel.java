/**
 * 
 */
package com.hitech.dms.web.model.geo.response;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class GeoCountryDTLResponseModel {
	private BigInteger countryID;
	private String countryCode;
	private String countryDesc;
}
