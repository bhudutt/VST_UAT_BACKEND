/**
 * 
 */
package com.hitech.dms.web.dao.customer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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

import com.hitech.dms.web.model.customer.request.CustomerDTLByCustIDRequestModel;
import com.hitech.dms.web.model.customer.request.CustomerDTLByMobileRequestModel;
import com.hitech.dms.web.model.customer.response.CustomerDTLByCustIDResponseModel;
import com.hitech.dms.web.model.customer.response.CustomerDTLByMobileResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class CustomerDaoImpl implements CustomerDao {
	private static final Logger logger = LoggerFactory.getLogger(CustomerDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<CustomerDTLByMobileResponseModel> fetchCustomerDTLByMobileNo(String userCode, String mobileNumber,
			String isFor, Long dealerId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCustomerDTLByMobileNo invoked.." + mobileNumber + " " + isFor);
		}
		Session session = null;
		Query query = null;
		List<CustomerDTLByMobileResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_CustomerAuto_ByMobile] :mobileNumber, :userCode, :isFor, :dealerID";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("mobileNumber", mobileNumber);
			query.setParameter("isFor", isFor);
			query.setParameter("dealerId", dealerId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<CustomerDTLByMobileResponseModel>();
				CustomerDTLByMobileResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new CustomerDTLByMobileResponseModel();
					responseModel.setCustomerId((BigInteger) row.get("customer_id"));
					responseModel.setCustomerCode((String) row.get("customerCode"));
					responseModel.setDisplayValue((String) row.get("displayValue"));
					responseModel.setProspectType((String) row.get("ProspectType"));
					responseModel.setMobileNo((String) row.get("Mobile_No"));
					responseModel.setUnderDealerTerritory((String) row.get("UnderDealerTerritory"));
					responseModel.setErrorFlag((String) row.get("error_flag"));
					responseModel.setErrorMsg((String) row.get("error_msg"));
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
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<CustomerDTLByMobileResponseModel> fetchCustomerDTLByMobileNo(
			CustomerDTLByMobileRequestModel customerDTLByMobileRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCustomerDTLByMobileNo invoked.." + customerDTLByMobileRequestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<CustomerDTLByMobileResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_CustomerAuto_ByMobile] :mobileNumber, :userCode, :isFor, :dealerID";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", customerDTLByMobileRequestModel.getUserCode());
			query.setParameter("mobileNumber", customerDTLByMobileRequestModel.getMobileNumber());
			query.setParameter("isFor", customerDTLByMobileRequestModel.getIsFor());
			query.setParameter("dealerID", customerDTLByMobileRequestModel.getDealerID());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<CustomerDTLByMobileResponseModel>();
				CustomerDTLByMobileResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new CustomerDTLByMobileResponseModel();
					responseModel.setCustomerId((BigInteger) row.get("customer_id"));
					responseModel.setCustomerCode((String) row.get("customerCode"));
					responseModel.setDisplayValue((String) row.get("displayValue"));
					responseModel.setProspectType((String) row.get("ProspectType"));
					responseModel.setMobileNo((String) row.get("Mobile_No"));
					responseModel.setUnderDealerTerritory((String) row.get("UnderDealerTerritory"));
					responseModel.setErrorFlag((String) row.get("error_flag"));
					responseModel.setErrorMsg((String) row.get("error_msg"));
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
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<CustomerDTLByCustIDResponseModel> fetchCustomerDTLByCustID(String userCode, Long custID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCustomerDTLByCustID invoked.." + custID);
		}
		Session session = null;
		Query query = null;
		List<CustomerDTLByCustIDResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_Customer_Details] :custID";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("custID", custID);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<CustomerDTLByCustIDResponseModel>();
				CustomerDTLByCustIDResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new CustomerDTLByCustIDResponseModel();
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
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<CustomerDTLByCustIDResponseModel> fetchCustomerDTLByCustID(String userCode,
			CustomerDTLByCustIDRequestModel customerDTLByCustIDRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCustomerDTLByCustID invoked.." + customerDTLByCustIDRequestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<CustomerDTLByCustIDResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_Customer_Details] :custID";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("custID", customerDTLByCustIDRequestModel.getCustomerID());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<CustomerDTLByCustIDResponseModel>();
				CustomerDTLByCustIDResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new CustomerDTLByCustIDResponseModel();
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
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}
}
