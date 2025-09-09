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
public class GeoDTLResponseModel {
	private BigInteger pinID;
	private String pinCode;
	private String localityCode;
	private String localityName;
	private String displayName;
	private BigInteger cityID;
	private String cityDesc;
	private BigInteger tehsilID;
	private String tehsilDesc;
	private BigInteger districtID;
	private String districDesc;
	private BigInteger stateID;
	private String stateDesc;
	private BigInteger countryID;
	private String countryDesc;
}
