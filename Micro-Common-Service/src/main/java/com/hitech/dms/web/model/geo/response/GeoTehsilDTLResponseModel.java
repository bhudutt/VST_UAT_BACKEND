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
public class GeoTehsilDTLResponseModel {
	private BigInteger tehsilID;
	private String tehsilCode;
	private String tehsilDesc;
	private BigInteger districtId;
}
