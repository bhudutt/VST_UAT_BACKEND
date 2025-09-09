package com.hitech.dms.web.dao.enquiry.digitalReport;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.digitalReport.request.DigitalEnquiReportModel;
import com.hitech.dms.web.model.digitalReport.request.DigitalEnquirySearchRequestModel;
import com.hitech.dms.web.model.enquiry.digitalReport.response.DigitalUploadResponseModel;

/**
 * @author vinay.gautam
 *
 */


@Repository
public class DigitalReportDaoImpl implements DigitalReportDao{
	private static final Logger logger = LoggerFactory.getLogger(DigitalReportDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<DigitalUploadResponseModel> downloadDigitalReport(String userCode, BigInteger digitalEnqHrdId) {
		List<DigitalUploadResponseModel> enquiryViewModel =  fetchDigitalReport(userCode, digitalEnqHrdId);
		return enquiryViewModel;
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<DigitalUploadResponseModel> fetchDigitalReport(String userCode,BigInteger digitalEnqHrdId) {
		Session session = null;
		List<DigitalUploadResponseModel> responseModelList = null;
		DigitalUploadResponseModel responseModel = null;
		Query<DigitalUploadResponseModel> query = null;
		String sqlQuery = "exec [SP_ENQ_getDigitalUploadDetails] :userCode, :digitalEnqHrdId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("digitalEnqHrdId", digitalEnqHrdId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<DigitalUploadResponseModel> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<DigitalUploadResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new DigitalUploadResponseModel();
					responseModel.setDtl_id( ((BigInteger) row.get("Dtl_id"))); 
					responseModel.setDigital_Enq_HDR_ID((BigInteger) row.get("Digital_Enq_HDR_ID"));
					responseModel.setDigital_Enq_No((String) row.get("Digital_Enq_No"));
					responseModel.setDigitalSourceName((String) row.get("DigitalSourceName"));
					responseModel.setPc_desc((String) row.get("pc_desc"));
					responseModel.setCustomer_Name((String) row.get("Customer_Name"));
					responseModel.setCustomer_Mobile_No((String) row.get("Customer_Mobile_No"));
					responseModel.setCustomer_Email_ID((String) row.get("Customer_Email_ID"));
					responseModel.setModel((String) row.get("Model"));
					responseModel.setCustomer_State((String) row.get("Customer_State"));
					responseModel.setCustomer_District((String) row.get("Customer_District"));
					responseModel.setCustomer_Tehsil((String) row.get("Customer_Tehsil"));
					responseModel.setCustomer_State((String) row.get("Customer_State"));
					responseModel.setSegment((String) row.get("Segment"));
					responseModel.setStatus((String) row.get("Status"));
					responseModel.setError_Detail((String) row.get("Error_Detail"));
					responseModelList.add(responseModel);
				}
			}
		}catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}
		
		return responseModelList;
	}
	
	
	
	
//	public List<DigitalEnquiReportModel> getSearchDigitalReport(Date fromDate,Date toDate,Session session,String userCode) {
//		//Session session = null;
//		session = sessionFactory.openSession();
//		List<DigitalEnquiReportModel> digitalReportSearchData =  searchDigitalReport(fromDate, toDate,session,userCode);
//		return digitalReportSearchData;
//	}
//	
	
	@SuppressWarnings("deprecation")
	@Override
	public List<DigitalEnquiReportModel> searchDigitalReport(DigitalEnquirySearchRequestModel requestModel,String userCode) {
		Session session = null;
		List<DigitalEnquiReportModel> responseList = null;
		DigitalEnquiReportModel response = null;
		Query<DigitalEnquiReportModel> query = null;
		String sqlQuery = "exec [SP_ENQ_getDigitalUploadSearchList] :userCode,:fromDate,:toDate,:pcId,:plateFormId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("fromDate", requestModel.getFromDate1());
			query.setParameter("toDate", requestModel.getToDate1());
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("plateFormId", requestModel.getPlateFormId());
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<DigitalEnquiReportModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					response = new DigitalEnquiReportModel();
					response.setId((BigInteger) row.get("id"));
					response.setDigital_Enq_No((String) row.get("Digital_Enq_No")) ;
					response.setUploadDate((String) row.get("UploadDate"));
					response.setDigitalSourceName((String) row.get("DigitalSourceName"));;
					response.setPc_desc((String) row.get("pc_desc"));
					response.setFile((String) row.get("file"));
					responseList.add(response);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}
		return responseList;
	}

}
