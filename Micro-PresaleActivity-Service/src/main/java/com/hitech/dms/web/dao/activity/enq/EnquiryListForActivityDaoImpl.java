/**
 * 
 */
package com.hitech.dms.web.dao.activity.enq;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.activity.enq.request.EnquiryListForActivityRequestModel;
import com.hitech.dms.web.model.activity.enq.response.EnquiryListForActivityResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class EnquiryListForActivityDaoImpl implements EnquiryListForActivityDao {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryListForActivityDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<EnquiryListForActivityResponseModel> fetchEnquiryListForActvity(String userCode,
			EnquiryListForActivityRequestModel requestModel) {
		Session session = null;
		Query query = null;
		List<EnquiryListForActivityResponseModel> responseModelList = null;
		EnquiryListForActivityResponseModel responseModel = null;
		String sqlQuery = "exec [SP_SACT_ENQ_LIST] :userCode, :DealerId, :activityPlanHdrId, :ActivityID, :fromDate, :toDate, :ActualActivityHDRId, :isFor ";
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("DealerId", requestModel.getDealerId());
			query.setParameter("activityPlanHdrId", requestModel.getActvityPlanHDRId());
			query.setParameter("ActivityID", requestModel.getActvityId());
			query.setParameter("fromDate", (requestModel.getFromDate() == null ? null
					: DateToStringParserUtils.addStratTimeOFTheDay(requestModel.getFromDate())));
			query.setParameter("toDate", (requestModel.getToDate() == null ? null
					: DateToStringParserUtils.addEndTimeOFTheDay(requestModel.getToDate())));
			query.setParameter("ActualActivityHDRId", requestModel.getActualActivityHDRId());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<EnquiryListForActivityResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new EnquiryListForActivityResponseModel();
					responseModel.setEnquiryId((BigInteger) row.get("enquiry_id"));
					responseModel.setEnquiryNumber((String) row.get("enquiry_number"));
					responseModel.setEnquiryDate((String) row.get("model_name"));
					responseModel.setModelName((String) row.get("enquiry_date"));
					responseModel.setDspName((String) row.get("dsp_name"));
					responseModel.setCustomerName((String) row.get("customer_name"));
					responseModel.setMobileNumber((String) row.get("mobile_no"));
					responseModel.setTehsil((String) row.get("tehsil"));
					responseModel.setExpectedPurchaseDate((String) row.get("expected_date_of_purchase"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.clear();
			}
		}
		return responseModelList;
	}
}
