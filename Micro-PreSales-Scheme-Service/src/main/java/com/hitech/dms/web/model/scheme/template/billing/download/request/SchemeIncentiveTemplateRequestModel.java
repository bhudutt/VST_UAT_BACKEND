/**
 * 
 */
package com.hitech.dms.web.model.scheme.template.billing.download.request;

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
public class SchemeIncentiveTemplateRequestModel {
	private Integer pcId;
	private String seriesName;
	private String isFor;
	private String isInactiveInclude;
}
