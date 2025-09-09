package com.hitech.dms.web.model.retailFollowUp.create.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
public class RetailStageModelResponse {
	private Integer retailStageId;
	private String  retailCode;
	private String retailValue;
	private boolean checked;
	
}
