package com.hitech.dms.web.model.productTrial.create.response;

import java.math.BigInteger;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class ProductTrialAttributeResponse {
	private BigInteger trialAttributeId;
	private String attributeCode;
	private String attributeDesc;
	private Integer rating= null;
    private String feedbackRemarks;

}
