/**
 * 
 */
package com.hitech.dms.web.model.scheme.template.billing.upload.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author dinesh.jakhar
 *
 */
@Getter
@Setter
@ToString
public class IncentiveBillingRequestModel {
	@JsonProperty(value = "schemeTypeId", required = true)
	private Integer schemeTypeId;
	@JsonProperty(value = "pcId", required = true)
	private Integer pcId;
	@JsonProperty(value = "seriesName", required = true)
	private String seriesName;
	private String isFor;
}
