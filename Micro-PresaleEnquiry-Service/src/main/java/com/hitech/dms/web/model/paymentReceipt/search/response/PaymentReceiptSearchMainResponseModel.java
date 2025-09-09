package com.hitech.dms.web.model.paymentReceipt.search.response;

import java.util.List;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class PaymentReceiptSearchMainResponseModel {
	private List<PaymentReceiptSearchResponseModel> paymentSearch;
	private Integer recordCount;
}
