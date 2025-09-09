package com.hitech.dms.web.dao.enquiry.productTrial.view;

import com.hitech.dms.web.model.paymentReceipt.view.request.PaymentReceiptViewRequestModel;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialEnqProspectHistoryResponse;

/**
 * @author vinay.gautam
 *
 */
public interface ProductTrialViewDao {
	
	public ProductTrialEnqProspectHistoryResponse fetchProductTrailView(String userCode, PaymentReceiptViewRequestModel paymentListRequestModel);

}
