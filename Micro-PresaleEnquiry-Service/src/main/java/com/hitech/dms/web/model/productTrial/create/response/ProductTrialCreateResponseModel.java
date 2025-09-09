package com.hitech.dms.web.model.productTrial.create.response;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */

@Data
public class ProductTrialCreateResponseModel {
	private BigInteger productTrialId;
	private String productTrialNo;
	private String msg;
	private int statusCode;
	List<ProductTrialEnquiryHistoryResponse> enquiryHistory;
}
