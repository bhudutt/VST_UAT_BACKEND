package com.hitech.dms.web.model.spara.payment.voucher.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class GrnInvReferenceDocList {
	
	  private BigDecimal totalPendingAmt;
		
	  private BigDecimal totalSettleAmt;
	
	  List<PVGrnAndInvoiceResponse> dataList;
	

}
