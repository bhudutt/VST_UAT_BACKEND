
package com.hitech.dms.web.model.spare.sale.invoice.request;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;

import lombok.Data;

@Data
public class CustomerOrderOrDCRequest {

	private BigInteger customerOrderOrDCId;
	private String customerOrderOrDCNo;
	private Date customerOrderOrDCDate;
	private List<PartDetailRequest> partDetails;

}
