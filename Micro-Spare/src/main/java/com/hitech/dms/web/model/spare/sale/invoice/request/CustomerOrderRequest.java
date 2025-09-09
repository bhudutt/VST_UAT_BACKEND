
package com.hitech.dms.web.model.spare.sale.invoice.request;

import java.util.Date;
import java.util.List;

import com.hitech.dms.web.model.spara.customer.order.picklist.request.PartDetailRequest;

import lombok.Data;

@Data
public class CustomerOrderRequest {

	private String customerOrderNo;
	private Date customerOrderDate;
	private List<PartDetailRequest> partDetails;

}
