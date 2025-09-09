package com.hitech.dms.web.model.spara.payment.voucher.response;

import java.util.List;

import lombok.Data;
@Data
public class SearchPaymentVoucherResponse {
	
	List<PaymentVoucherReponse> searchList;
	private Integer totalCount;
	private Integer rowCount;
	private String msg;
	private Integer statusCode;
	

}
