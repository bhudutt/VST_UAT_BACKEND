/**
 * 
 */
package com.hitech.dms.web.model.geo.request;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class GeoStateDTLRequestModel {
	private Long countryID;
	private Long stateId;
	private String isFor;
}
