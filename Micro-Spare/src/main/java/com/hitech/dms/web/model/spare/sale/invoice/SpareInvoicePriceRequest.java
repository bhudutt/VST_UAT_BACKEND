package com.hitech.dms.web.model.spare.sale.invoice;

import lombok.Data;

@Data
public class SpareInvoicePriceRequest {

	
	private Integer partId;
	private Integer refDocId;
	private Integer mrp;
	private Integer partyType;
}
