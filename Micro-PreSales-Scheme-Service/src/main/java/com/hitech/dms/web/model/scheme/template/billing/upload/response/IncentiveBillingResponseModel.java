/**
 * 
 */
package com.hitech.dms.web.model.scheme.template.billing.upload.response;

import java.math.BigDecimal;

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
public class IncentiveBillingResponseModel {
	private String modelName;
	private String incentiveType;
	private String dealerCategory;
	private BigDecimal incentiveAmount;
	private String qualifyingCriteria;
	private String deliveryCriteria;
}
