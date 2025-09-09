/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.dao.paymentReceipt.search;


import com.hitech.dms.web.model.paymentReceipt.search.request.PaymentReceiptSearchRequestModel;
import com.hitech.dms.web.model.paymentReceipt.search.response.PaymentReceiptSearchMainResponseModel;

/**
 * @author vinay.gautam
 *
 */
public interface PaymentReceiptSearchDao {
	public PaymentReceiptSearchMainResponseModel fetchPaymentReceiptList(String userCode, PaymentReceiptSearchRequestModel paymentListRequestModel);
	

}
