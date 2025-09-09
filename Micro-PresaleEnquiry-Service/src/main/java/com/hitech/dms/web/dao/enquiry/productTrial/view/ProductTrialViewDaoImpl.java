package com.hitech.dms.web.dao.enquiry.productTrial.view;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.dao.enquiry.productTrial.ProductTrialCreateDao;
import com.hitech.dms.web.dao.enquiry.productTrial.ProductTrialCreateDaoImpl;
import com.hitech.dms.web.model.paymentReceipt.view.request.PaymentReceiptViewRequestModel;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialAttributeResponse;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialEnqProspectHistoryResponse;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialEnqProspectResponseModel;
import com.hitech.dms.web.model.productTrial.create.response.ProductTrialEnquiryHistoryResponse;

/**
 * @author vinay.gautam
 *
 */
@Repository
public class ProductTrialViewDaoImpl implements ProductTrialViewDao{
	
	private static final Logger logger = LoggerFactory.getLogger(ProductTrialCreateDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private ProductTrialCreateDao productTrialCreateDao;
	
	Integer size = Integer.MAX_VALUE-1;

	@Override
	public ProductTrialEnqProspectHistoryResponse fetchProductTrailView(String userCode, PaymentReceiptViewRequestModel requestModel) {
		ProductTrialEnqProspectHistoryResponse responseModelList = null;
		Session session = null;

		try {
			session = sessionFactory.openSession();
			responseModelList = new ProductTrialEnqProspectHistoryResponse();

			ProductTrialEnqProspectResponseModel enqProRes = productTrialCreateDao.fetchEnqProspectDtl(session, userCode, requestModel.getId(), 6);
			if (enqProRes !=null) {
				responseModelList.setEnqProspect(enqProRes);
				List<ProductTrialEnquiryHistoryResponse>  engHistory = productTrialCreateDao.fetchEnqHistory(session, requestModel.getId(),0,size,"view");
				List<ProductTrialAttributeResponse> feedback = productTrialCreateDao.fetchTrialAttribute(session, requestModel.getId(),"view");
				if (engHistory !=null) {
					responseModelList.setEnquiryHistory(engHistory);
				}
				if (feedback!=null) {
					responseModelList.setAttribute(feedback);
				}
			}

			
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session != null) {
				session.close();
			}
		}		
		return responseModelList;
	}
	

}
