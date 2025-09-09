package com.hitech.dms.web.model.paymentReceipt.search.request;

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
public class PaymentReceiptSearchRequestModel {
	private BigInteger dealerId;
	private BigInteger branchId;
	private String userCode;
	private BigInteger enquiryId;
	private BigInteger paymentId;
	@JsonDeserialize(using = DateHandler.class)
	private Date fromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date toDate;

	private String includeInActive;
	private BigInteger orgHierId;
	private int page;
	private int size;


}
