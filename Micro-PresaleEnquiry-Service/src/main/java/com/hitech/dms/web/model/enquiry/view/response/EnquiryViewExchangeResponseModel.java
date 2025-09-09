package com.hitech.dms.web.model.enquiry.view.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class EnquiryViewExchangeResponseModel {
	private BigInteger enquiryExcDTLId;
	private BigInteger brandId;
	private String modelName;
	private Integer modelYear;
	private BigDecimal estimatedExchangePrice;
}
