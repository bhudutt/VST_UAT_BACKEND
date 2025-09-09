package com.hitech.dms.web.dao.paymentReceipt.create;



import java.math.BigInteger;
import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.entity.paymentReceipt.PaymentReceiptEntity;
import com.hitech.dms.web.model.paymentReceipt.create.request.PaymentReceiptCreateRequestModel;
import com.hitech.dms.web.model.paymentReceipt.create.response.EnquiryNoAutoSearchResponseModel;
import com.hitech.dms.web.model.paymentReceipt.create.response.PaymentReceiptCreateResponseModel;
import com.hitech.dms.web.model.paymentReceipt.create.response.PaymentReceiptModeResponseModel;
import com.hitech.dms.web.model.paymentReceipt.create.response.PaymentReceiptTypeResponseModel;;

/**
 * @author vinay.gautam
 *
 */
public interface PaymentReceiptCreateDao {
	
	public PaymentReceiptCreateResponseModel createPaymentRecepit(String userCode, PaymentReceiptEntity prModel, Device device);
	
	public List<PaymentReceiptModeResponseModel> getPaymentReceiptMode(String userCode);
	
	public List<PaymentReceiptTypeResponseModel> getPaymentReceiptType(String userCode,BigInteger enquiryId);
	
	public List<EnquiryNoAutoSearchResponseModel> enquryNoAutoSearch(String userCode,String enquiryNo, String branchId, String isFor);

	public List<PaymentReceiptTypeResponseModel> getReceiptAmtTotalByEnqId(String userCode, BigInteger enquiryId,
			BigInteger receiptTypeId);


}
