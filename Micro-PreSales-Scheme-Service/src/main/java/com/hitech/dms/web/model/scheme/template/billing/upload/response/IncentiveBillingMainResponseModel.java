/**
 * 
 */
package com.hitech.dms.web.model.scheme.template.billing.upload.response;

import java.util.List;

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
public class IncentiveBillingMainResponseModel {
	private String msg;
	private int statusCode; 
	private List<IncentiveBillingResponseModel> recordList;
}
