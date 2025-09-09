	package com.hitech.dms.web.dao.enquiry.digitalenquiry.create;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.digitalenquiry.DigitalEnquiryEntity;
import com.hitech.dms.web.model.enquiry.digitalenquiry.create.request.DigitalEnquiryCreateRequestModel;
import com.hitech.dms.web.model.enquiry.digitalenquiry.create.response.DigitalEnquiryCreateResponseModel;
import com.hitech.dms.web.model.enquiry.digitalenquiry.create.response.DigitalEnquiryListResponseModel;

@Repository
public class DigitalEnquiryCreateDaoImpl implements DigitalEnquiryCreateDao{
	private static final Logger logger = LoggerFactory.getLogger(DigitalEnquiryCreateDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private RestTemplate restTemplate;
	
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public DigitalEnquiryCreateResponseModel createDigitalEnquiry(String userCode,
			DigitalEnquiryEntity digitalEnquiryCreateRequestModel, Device device) {
		  
		if (logger.isDebugEnabled()) {
			logger.debug("createDigitalEnquiry invoked.." + userCode);
			logger.debug(digitalEnquiryCreateRequestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		DigitalEnquiryCreateResponseModel responseModel = new DigitalEnquiryCreateResponseModel();
		DigitalEnquiryCreateRequestModel requestModel = new DigitalEnquiryCreateRequestModel();
		Query query = null;
		Map<String, Object> mapData = null;
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);
			BigInteger userId = null;
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
			}
			mapData = fetchLastDlrEmpNo(session, digitalEnquiryCreateRequestModel.getCompanyName());
			String lastEmpCode = null;
			if (mapData != null && mapData.get("SUCCESS") != null) {
				if(mapData.get("EmpCode") != null) {
					lastEmpCode = (String) mapData.get("EmpCode");
				}
			}
			String empCode = "DIGIT";
			if(lastEmpCode != null && !lastEmpCode.equals("")) {
				String lastSuffix = null;
				try {
					lastSuffix = lastEmpCode.substring(5, lastEmpCode.length());
					Integer i = Integer.valueOf(lastSuffix);
					if(i == 0) {
						empCode = empCode + "0001";
					}else {
						empCode = empCode+String.format("%04d", i + 1);
					}
				} catch(Exception ex) {
					empCode = empCode + "0001";
				}
			}else {
				empCode = empCode + "0001";
			}
			
			digitalEnquiryCreateRequestModel.setDigitalEmpCode(empCode);
			
		session.save(digitalEnquiryCreateRequestModel);

		if (isSuccess) {
			transaction.commit();
			session.close();
			//sessionFactory.close();
			responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			msg = "Digital Enquiry Created Successfully";
			responseModel.setMsg(msg);
		}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
	
  }
return responseModel;
}

	//List API
	
	public List<DigitalEnquiryListResponseModel>fetchDigitalFormMasterList(String userCode,
			Integer Digital_Source_ID){
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDigitalFormMasterList invoked.." + userCode);
		}
		Session session = null;
		List<DigitalEnquiryListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			responseModelList = fetchDigitalFormMasterList(session, userCode, Digital_Source_ID);
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<DigitalEnquiryListResponseModel> fetchDigitalFormMasterList(Session session, String userCode,
			Integer Digital_Source_ID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDigitalFormMasterList invoked.." + userCode + " ");
		}
		Query query = null;
		List<DigitalEnquiryListResponseModel> responseModelList = null;
		String sqlQuery = "select DigitalSourceName, CompanyName, CompanyContact_No, CompanyEmail_id, ContactPersonName, ContactPerson_No, ContactPerson_Email_id,Designation, CompanyAddress1, CompanyAddress2, CompanyAddress3, DIS.DistrictDesc, TEH.TehsilDesc,PI.pincode, CT.CityDesc, ST.StateDesc, GST_NO, TAN_NO, PAN_NO, DTG.IsActive,DTG.DigitalEmpCode from SA_MST_ENQ_SOURCE_DIGITAL DTG LEFT JOIN CM_GEO_STATE ST on ST.state_id=DTG.State LEFT join CM_GEO_DIST DIS on DIS.District_ID=DTG.District LEFT JOIN CM_GEO_TEHSIL TEH ON TEH.tehsil_id=DTG.Tehsil LEFT JOIN CM_GEO_CITY CT ON CT.city_id=DTG.City left JOIN CM_GEO_PIN PI ON PI.pin_id=DTG.pin_code";
		try {
			query = session.createSQLQuery(sqlQuery);
			
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<DigitalEnquiryListResponseModel>();
				DigitalEnquiryListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new DigitalEnquiryListResponseModel();
					responseModel.setDigitalSourceName((String) row.get("DigitalSourceName")); 
					responseModel.setCompanyName((String) row.get("CompanyName"));
					responseModel.setCompanyContactNo((String)row.get("CompanyContact_No"));
					responseModel.setCompanyEmailid((String) row.get("CompanyEmail_id"));
					responseModel.setContactPersonName((String)row.get("ContactPersonName"));
					responseModel.setContactPersonNo((String)row.get("ContactPerson_No"));
					responseModel.setContactPersonEmailid((String) row.get("ContactPerson_Email_id"));
					responseModel.setDesignation((String)row.get("Designation"));
					responseModel.setCompanyAddress1((String)row.get("CompanyAddress1"));
					responseModel.setCompanyAddress2((String)row.get("CompanyAddress2"));
					responseModel.setCompanyAddress3((String)row.get("CompanyAddress3"));
					responseModel.setDistrict((String)row.get("DistrictDesc"));
					responseModel.setTehsil((String)row.get("TehsilDesc"));
					responseModel.setPincode((String) row.get("pincode"));
					responseModel.setCity((String) row.get("CityDesc"));
					responseModel.setState((String)row.get("StateDesc"));
					responseModel.setGSTNO((String)row.get("GST_NO"));
					responseModel.setTANNO((String)row.get("TAN_NO"));
					responseModel.setPANNO((String)row.get("PAN_NO"));
					responseModel.setIsActive((String)row.get("IsActive"));
					String digitalCodeVal=(String)row.get("DigitalEmpCode");
					if(digitalCodeVal !=null) {
						responseModel.setDigitalEmpCode(digitalCodeVal);
					}
					
					
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		NativeQuery<?> query = null;
		String sqlQuery = "Select user_id from ADM_USER (nolock) u where u.UserCode =:userCode";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("user_id");
				}
				mapData.put("userId", userId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchLastDlrEmpNo(Session session, String companyName) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select Top 1 sacih.DigitalEmpCode from SA_MST_ENQ_SOURCE_DIGITAL (nolock) sacih where sacih.CompanyName =:companyName order by sacih.Digital_Source_ID desc ";
		mapData.put("ERROR", "Last Employee Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("companyName", companyName);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String empCode = null;
				for (Object object : data) {
					Map row = (Map) object;
					empCode = (String) row.get("DigitalEmpCode");
				}
				mapData.put("EmpCode", empCode);
			}
			mapData.put("SUCCESS", "FETCHED");
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING LAST EMPLOYEE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING LAST EMPLOYEE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
	
	//END
		
}


