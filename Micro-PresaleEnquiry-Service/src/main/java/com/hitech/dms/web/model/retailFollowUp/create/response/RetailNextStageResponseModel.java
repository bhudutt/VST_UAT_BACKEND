package com.hitech.dms.web.model.retailFollowUp.create.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class RetailNextStageResponseModel {
	
	private Integer retailStageId;
	private String retailStageSubValue;
	private char isConsideredRejected;

}
