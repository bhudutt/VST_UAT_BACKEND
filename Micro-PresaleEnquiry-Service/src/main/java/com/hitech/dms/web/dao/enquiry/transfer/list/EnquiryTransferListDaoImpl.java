/**
 * 
 */
package com.hitech.dms.web.dao.enquiry.transfer.list;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
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

import com.hitech.dms.web.model.enquiry.transfer.request.TransferENQRequestModel;
import com.hitech.dms.web.model.enquiry.transfer.response.TransferENQResponse;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class EnquiryTransferListDaoImpl implements EnquiryTransferListDao {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryTransferListDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<TransferENQResponse> fetchTransferENQList(String userCode, TransferENQRequestModel enqRequestModel) {
//		if (logger.isDebugEnabled()) {
			logger.debug("fetchTransferENQList invoked.." + enqRequestModel.toString());
//		}
		Session session = null;
		Query query = null;
		List<TransferENQResponse> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "exec [SP_ENQ_getTransferENQdtls] :dealerId, :branchID, :userCode, :salesPersonID, :enquiryNo, "
					+ " :district_id, :tehsil_id, :enquiryTypeID, :enquiryStage, :enquiryFromDate, :enquiryToDate, :includeInactive";
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerId", enqRequestModel.getDealerId());
			query.setParameter("branchID", enqRequestModel.getBranchID());
			query.setParameter("userCode", userCode);
			query.setParameter("salesPersonID", enqRequestModel.getSalesPersonID());
			query.setParameter("enquiryNo", enqRequestModel.getEnquiryNo());
			query.setParameter("district_id", enqRequestModel.getDistrictId());
			query.setParameter("tehsil_id", enqRequestModel.getTehsilId());
			query.setParameter("enquiryTypeID", enqRequestModel.getEnquiryTypeID());
			query.setParameter("enquiryStage", enqRequestModel.getEnquiryStage());
			query.setParameter("enquiryFromDate", enqRequestModel.getEnquiryFromDate());
			query.setParameter("enquiryToDate", enqRequestModel.getEnquiryToDate());
			query.setParameter("includeInactive", enqRequestModel.getIncludeInactive());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<TransferENQResponse>();
				TransferENQResponse responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new TransferENQResponse();
					responseModel.setEnquiryId((BigInteger) row.get("enquiry_id"));
					responseModel.setEnquiryNumber((String) row.get("enquiryNumber"));
					responseModel.setEnquiryDate((String) row.get("enquiry_date"));
					responseModel.setEnquiryStage((String) row.get("enq_stage"));
					responseModel.setEnquiryStatus((String) row.get("enquiry_status"));
					responseModel.setEnquiryType((String) row.get("enquiry_type"));
					responseModel.setSalesman((String) row.get("dsp_name"));
					responseModel.setModelPlusVariant((String) row.get("modelPlusVariant"));
					responseModel.setVillage((String) row.get("Village"));
					responseModel.setDistrict((String) row.get("district"));
					responseModel.setCity((String) row.get("City"));
					responseModel.setTehsil((String) row.get("tehsil"));
					responseModel.setCustomerName((String) row.get("customer_name"));
					responseModel.setMobileNumber((String) row.get("mobile_no"));
					responseModel.setState((String) row.get("state"));
					responseModel.setCountry((String) row.get("country"));
					responseModelList.add(responseModel);
				}
			}

		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}
}
