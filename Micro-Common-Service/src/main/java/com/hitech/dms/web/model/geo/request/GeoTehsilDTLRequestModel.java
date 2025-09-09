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
public class GeoTehsilDTLRequestModel {
	private Long districtID;
	private Long dealerID;
	private String isFor;
}
