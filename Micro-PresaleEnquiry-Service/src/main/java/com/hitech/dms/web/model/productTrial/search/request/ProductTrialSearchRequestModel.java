package com.hitech.dms.web.model.productTrial.search.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class ProductTrialSearchRequestModel {
	
	private Integer pcId;
	private BigInteger orgHierId;
	private BigInteger dealerId;
	private BigInteger branchId;
	private String productTrialNo;
	private String enquiryNo;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;
	private Integer page;
	private Integer size;

}
