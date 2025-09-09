package com.hitech.dms.web.dao.paymentReceipt.view;



import com.hitech.dms.web.model.paymentReceipt.view.request.PaymentReceiptViewRequestModel;
import com.hitech.dms.web.model.paymentReceipt.view.response.PaymentReceiptViewMainResponse;

/**
 * @author vinay.gautam
 *
 */
public interface PaymentReceiptViewDao {

	public PaymentReceiptViewMainResponse fetchPaymentReceiptViewList(String userCode, PaymentReceiptViewRequestModel paymentListRequestModel);
}
