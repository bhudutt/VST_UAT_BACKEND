/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.search.export;

import java.io.IOException;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.api.response.HeaderResponse;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.web.model.enquiry.list.request.EnquiryListRequestModel;
import com.hitech.dms.web.model.enquiry.list.response.EnquiryListResponseModel;
import com.hitech.dms.web.service.client.EnquiryServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class EnquirySearchExportDaoImpl implements EnquirySearchExportDao {
	private static final Logger logger = LoggerFactory.getLogger(EnquirySearchExportDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private EnquiryServiceClient enquiryServiceClient;

	public List<EnquiryListResponseModel> exportEnquiryList(String authorizationHeader, String userCode,
			EnquiryListRequestModel enquiryListRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("exportEnquiryList invoked.." + enquiryListRequestModel.toString());
		}
		List<EnquiryListResponseModel> responseModelList = null;
		try {
			HeaderResponse headerResponse = enquiryServiceClient.fetchEnquiryList(userCode, enquiryListRequestModel);
			Object object = headerResponse.getResponseData();
			if (object != null) {
				try {
					String jsonString = new com.google.gson.Gson().toJson(object);
					responseModelList = CommonUtils.jsonArrayToObjectList(jsonString, EnquiryListResponseModel.class);
					logger.debug((responseModelList == null ? "Enquiry Export Search List Not Found."
							: responseModelList.toString()));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(this.getClass().getName(), e);
				}
			}

		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseModelList;
	}
}
