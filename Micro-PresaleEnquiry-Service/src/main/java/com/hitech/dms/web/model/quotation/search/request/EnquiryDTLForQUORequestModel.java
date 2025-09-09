package com.hitech.dms.web.model.quotation.search.request;

import java.math.BigInteger;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryDTLForQUORequestModel {
	private BigInteger customerId;
	private Integer pcId;
	private BigInteger branchId;
}
