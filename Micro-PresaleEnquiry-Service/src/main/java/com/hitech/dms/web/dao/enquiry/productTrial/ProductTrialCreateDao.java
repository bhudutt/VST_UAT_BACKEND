package com.hitech.dms.web.dao.enquiry.productTrial;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Session;
import org.springframework.mobile.device.Device;

import com.hitech.dms.web.entity.productTrial.ProductTrialHdrEntity;
import com.hitech.dms.web.model.paymentReceipt.view.request.PaymentReceiptViewRequestModel;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialAttributeResponse;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialCreateResponseModel;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialEnqProspectHistoryResponse;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialEnqProspectResponseModel;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialEnquiryHistoryResponse;

/**
 * @author vinay.gautam
 *
 */
public interface ProductTrialCreateDao {
	
	public ProductTrialEnqProspectHistoryResponse fetchEnqProspectEnqHistory(String userCode, PaymentReceiptViewRequestModel paymentListRequestModel);
	
	public ProductTrialEnqProspectResponseModel fetchEnqProspectDtl(Session session, String userCode, BigInteger enquiryId, int flag);
	
	public List<ProductTrialEnquiryHistoryResponse> fetchEnqHistory(Session session, BigInteger enquiryId, int page,  int size,String isFor);
	
	public List<ProductTrialAttributeResponse> fetchTrialAttribute(Session session, BigInteger enquiryId, String isFor);
	
	public ProductTrialCreateResponseModel createProductTrial(String userCode, ProductTrialHdrEntity ptModel, Device device);

}
