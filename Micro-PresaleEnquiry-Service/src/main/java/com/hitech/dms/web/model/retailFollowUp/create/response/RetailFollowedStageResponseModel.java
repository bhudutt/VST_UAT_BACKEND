package com.hitech.dms.web.model.retailFollowUp.create.response;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author vinay.gautam
 *
 */
@Setter
@Getter
@ToString
public class RetailFollowedStageResponseModel {
	
	private Integer retailStageId;
	private String  retailStageMainCode;
	private String retailStageSubValue;
	private Integer groupSeqNo;
	private BigInteger retailFollowupHdrId;

}
