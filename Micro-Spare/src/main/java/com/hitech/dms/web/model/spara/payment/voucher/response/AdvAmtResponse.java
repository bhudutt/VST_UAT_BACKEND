package com.hitech.dms.web.model.spara.payment.voucher.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
@Data
public class AdvAmtResponse {
	
	private BigDecimal advAmt;
	
	private List<PaymentVoucherList> refDocList;

}
