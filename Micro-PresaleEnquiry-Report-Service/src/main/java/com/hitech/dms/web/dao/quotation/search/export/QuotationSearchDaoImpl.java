/**
 * 
 */
package com.hitech.dms.web.dao.quotation.search.export;

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
import com.hitech.dms.web.model.quotation.search.request.VehQuoSearchRequestModel;
import com.hitech.dms.web.model.quotation.search.response.VehQuoSearchResponse;
import com.hitech.dms.web.service.client.EnquiryServiceClient;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class QuotationSearchDaoImpl implements QuotationSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(QuotationSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private EnquiryServiceClient enquiryServiceClient;

	public List<VehQuoSearchResponse> exportQuotationList(String authorizationHeader, String userCode,
			VehQuoSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("exportQuotationList invoked.." + requestModel.toString());
		}
		List<VehQuoSearchResponse> responseModelList = null;
		try {
			HeaderResponse headerResponse = enquiryServiceClient.searchQuoList(userCode, requestModel);
			Object object = headerResponse.getResponseData();
			if (object != null) {
				try {
					String jsonString = new com.google.gson.Gson().toJson(object);
					responseModelList = CommonUtils.jsonArrayToObjectList(jsonString, VehQuoSearchResponse.class);
					logger.debug((responseModelList == null ? "Quotation Export Search List Not Found."
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
