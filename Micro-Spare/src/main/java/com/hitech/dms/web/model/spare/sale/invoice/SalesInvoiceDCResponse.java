package com.hitech.dms.web.model.spare.sale.invoice;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class SalesInvoiceDCResponse {
	
	private BigInteger dChallanId;
	
	private String dcNumber;
	
	private Date dcDate;
	
	private String dcStatus;

}
