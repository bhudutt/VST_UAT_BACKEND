package com.hitech.dms.web.model.spara.customer.order.response;

import java.math.BigInteger;

import com.hitech.dms.web.model.SpareModel.SparePoCreateResponseModel;

import lombok.Data;
@Data
public class SpareCustOrderCreateResponseModel {
	
	
	
		private BigInteger customerHdrId;
		private String customerOrderNumber;
		private String msg;
		private Integer statusCode;

}
