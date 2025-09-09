package com.hitech.dms.web.dao.customer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.entity.common.SystemLookUpEntity;
import com.hitech.dms.web.entity.user.CustomerDTLEntity;
import com.hitech.dms.web.entity.user.CustomerHDREntity;
import com.hitech.dms.web.model.customer.create.CustServiceRep;
import com.hitech.dms.web.model.customer.create.CustServiceSaveReq;
import com.hitech.dms.web.model.customer.create.CustomerDetailsResponse;
import com.hitech.dms.web.model.customer.create.CustomerMobileRes;
import com.hitech.dms.web.model.customer.create.LookupResponse;

@Repository
public class CustomerCreDaoIml implements CustomerCreDao {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerCreDaoIml.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public CustomerDetailsResponse fetchCustomerDTLByCustMob(String userCode, BigInteger customerId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCustomerDTLByCustMob invoked.." + customerId);
		}
		Session session = null;
		Query query = null;
		CustomerDetailsResponse responseModel = null;
		String sqlQuery = "exec [SP_Fetch_Cust_DTL] :customerId, :userCode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("customerId", customerId);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new CustomerDetailsResponse();
					responseModel.setCustomerId((BigInteger) row.get("customer_id"));
					responseModel.setCustomerCode((String) row.get("customerCode"));
					responseModel.setAddress1((String) row.get("address1"));
					responseModel.setAddress2((String) row.get("address2"));
					responseModel.setAddress3((String) row.get("address3"));
					responseModel.setAlternateNumber((String) row.get("alternate_no"));
					responseModel.setAnniversaryDate((Date) row.get("anniversaryDate"));
					responseModel.setCityDesc((String) row.get("citydesc"));
					responseModel.setCityID((BigInteger) row.get("city_id"));
					responseModel.setContactTitle((String) row.get("contactTitle"));
					responseModel.setCountryDesc((String) row.get("countryDesc"));
					responseModel.setCountryID((BigInteger) row.get("country_id"));
					responseModel.setCustomerCategory((String) row.get("customerCategory"));
					responseModel.setCustomerCategoryID((BigInteger) row.get("CustomerCategory_Id"));
					responseModel.setDateOfBirth((Date) row.get("dateofbirth"));
					responseModel.setDistrictDesc((String) row.get("districtDesc"));
					responseModel.setDistrictId((BigInteger) row.get("district_id"));
					responseModel.setEmailId((String) row.get("Email_id"));
					responseModel.setFirstName((String) row.get("Firstname"));
					responseModel.setGstIN((String) row.get("GSTIN"));
					responseModel.setLandInAcres((Double) row.get("LAND_IN_ACRES"));
					responseModel.setLastName((String) row.get("lastname"));
					responseModel.setMiddleName((String) row.get("Middlename"));
					responseModel.setMobileNumber((String) row.get("mobile_no"));
					responseModel.setPanNo((String) row.get("PAN_NO"));
					responseModel.setPinCode((String) row.get("pincode"));
					responseModel.setPinID((BigInteger) row.get("pin_id"));
					responseModel.setStateDesc((String) row.get("stateDesc"));
					responseModel.setStateID((BigInteger) row.get("state_id"));
					responseModel.setTehsilDesc((String) row.get("TehsilDesc"));
					responseModel.setTehsilId((BigInteger) row.get("tehsil_id"));
					responseModel.setWatsupNumber((String) row.get("WhatsappNo"));
					responseModel.setProspectType((String)row.get("ProspectType"));
					responseModel.setGender((String)row.get("Gender"));
					
					responseModel.setFatherName((String) row.get("FatherName"));
					responseModel.setAadharCardNo((String)row.get("AADHAR_CARD_NO"));
					
					responseModel.setAnnualIncome((String) row.get("ANNUAL_INCOME"));
					responseModel.setIsMarried((String) row.get("IsMarried"));
					responseModel.setQualification((String) row.get("Qualification"));
					responseModel.setOrganizationName((String) row.get("Organization_Name"));
					responseModel.setPhoneNumber((String) row.get("PhoneNumber"));
					responseModel.setDlNo((String) row.get("DL_NO"));
					       
					
					
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException  exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@Override
	public List<CustomerMobileRes> fetchCustomerMobNameDtl(String userCode,
			String searchText) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCustomerDTLByCustMob invoked.." + searchText);
		}
		Map<String, Object> mapData = null;
		Integer userId=null;
		Session session = null;
		Query query = null;
		List<CustomerMobileRes> responseModelList = null;
		String sqlQuery = "exec [SP_SearchBy_Customer] :mobileOrname, :userId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = ((BigInteger)mapData.get("userId")).intValue();
			}
			query.setParameter("mobileOrname", searchText);
			query.setParameter("userId", userId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<CustomerMobileRes>();
				CustomerMobileRes responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
				
					responseModel = new CustomerMobileRes();
					responseModel.setCustomerId((BigInteger) row.get("customer_id"));
					responseModel.setCustomerName((String) row.get("CustomerName"));
					responseModel.setCustomerCode((String) row.get("CustomerCode"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException  exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}
	
	
	@SuppressWarnings("deprecation")
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
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

	@Override
	public List<LookupResponse> fetchCustomerMaster(String lookupTypeCode) {
	
			
		List<LookupResponse> finalList =null;
		LookupResponse res = null;
		 CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
		 CriteriaQuery<SystemLookUpEntity> criteria = builder.createQuery(SystemLookUpEntity.class);
		 Root<SystemLookUpEntity> postRoot =  criteria.from(SystemLookUpEntity.class);
		 criteria.select(postRoot).where(builder.equal(postRoot.get("lookTypeCode"), lookupTypeCode));
		 List<SystemLookUpEntity> list = sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
		 if(!list.isEmpty()) {
			 finalList = new ArrayList<>();
			 for(SystemLookUpEntity bean:list) {
				 res = new LookupResponse();
				 res.setValueId(bean.getLookUpId());
				 res.setValueCode(bean.getLookupVal());
				 res.setDisplayValue(bean.getDisplayOrder());
				 finalList.add(res);
			 }
		 }
		  return finalList;
		}

	@Override
	public CustServiceRep createCustomerDetail(String authorizationHeader, String userCode,
			CustServiceSaveReq requestModel, Device device) {
		CustServiceRep rep = new CustServiceRep();
		Map<String,Object> mapData = null;
		BigInteger id =null;
		Session session = null;
		String customerCode = null;
		boolean isSuccess=false;
		Transaction trans = null;
		CustomerHDREntity header = new CustomerHDREntity();
		CustomerDTLEntity detail = new CustomerDTLEntity();
		try {
			 session = sessionFactory.openSession();
			 trans = session.beginTransaction();
		customerCode = (String) session.createSQLQuery("select Customer_Code from Generate_CustomerCodeNo()").uniqueResult();
		mapData = fetchUserDTLByUserCode(session, userCode);
		 
		BeanUtils.copyProperties(requestModel, header);
		header.setCustomerCode(customerCode);
		header.setProspectType("PROSPECT");
		header.setCustomerCategoryId(BigInteger.valueOf(requestModel.getCustomerCtgId()));
		header.setCreatedBy(((BigInteger)mapData.get("userId")));
		header.setCreatedDate(new Date());
		header.setLandInAcres(requestModel.getLandInAcres());
		header.setQualification(requestModel.getQualification());
		header.setPinId(requestModel.getPinCode());
		header.setPhoneNo(requestModel.getPhoneNumber());
		header.setWhatsAppNo(requestModel.getWhatsappNo());
		header.setDLNo(requestModel.getDlNumber());
		header.setGstIN(requestModel.getGstIn());
		BeanUtils.copyProperties(requestModel, detail);
		id = (BigInteger) session.save(header);
		if(id!=null) {
			 detail.setCustomermstIds(id);
			 detail.setDistrictId(requestModel.getDistinctId());
			 detail.setPinCode(requestModel.getPinCode().intValue());
			 detail.setVillageId(requestModel.getCityId());
			 session.save(detail);
			 trans.commit();
			 isSuccess=true;
		}
		}catch(Exception e) {
			e.printStackTrace();
			trans.rollback();
		}finally {
			if(isSuccess) {
				rep.setCsNumber(customerCode);
				rep.setStatusCode(200);
				rep.setMsg("Data save successfully...");
			}else {
				rep.setStatusCode(500);
				rep.setMsg("Server side error");
			}
		}
		return rep;
	}
	
	@Override
	public BigInteger alreadyExistMobileNo(String mobileNo) {
		
		Session session = null;
		BigInteger available=null;
		Query query = null;
		try {
			session = sessionFactory.openSession();
			String sqlQuery = "select Customer_Id from CM_CUST_HDR where Mobile_No = '"+mobileNo+"'"; 
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {

				for (Object object : data) {
					Map row = (Map) object;
					available = (BigInteger) row.get("Customer_Id");
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
		return available;
	}

}
