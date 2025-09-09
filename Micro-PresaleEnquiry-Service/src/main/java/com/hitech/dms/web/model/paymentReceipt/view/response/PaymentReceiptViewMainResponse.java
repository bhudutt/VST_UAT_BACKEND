/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.model.paymentReceipt.view.response;

import java.util.List;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class PaymentReceiptViewMainResponse {

	PaymentReceiptEnqAndProspectViewResponseModel enqProspect;
	List<PaymentReceiptDetailsViewResponse> viewDetails;
}
