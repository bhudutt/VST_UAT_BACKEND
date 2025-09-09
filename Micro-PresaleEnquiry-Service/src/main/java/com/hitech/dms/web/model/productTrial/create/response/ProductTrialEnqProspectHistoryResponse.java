package com.hitech.dms.web.model.productTrial.create.response;

import java.util.List;

import com.hitech.dms.web.model.paymentReceipt.view.response.PaymentReceiptEnqAndProspectViewResponseModel;

import lombok.Data;

/**
 * @author vinay.gautam
 *
 */
@Data
public class ProductTrialEnqProspectHistoryResponse {
	
	ProductTrialEnqProspectResponseModel enqProspect;
	List<ProductTrialEnquiryHistoryResponse> enquiryHistory;
	List<ProductTrialAttributeResponse> attribute;

}
