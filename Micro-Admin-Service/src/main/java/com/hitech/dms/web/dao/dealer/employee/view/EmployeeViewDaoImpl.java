/**
 * 
 */
package com.hitech.dms.web.dao.dealer.employee.view;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.dealer.employee.view.request.EmployeeViewRequestModel;
import com.hitech.dms.web.model.dealer.employee.view.response.EmployeeAddressViewResponseModel;
import com.hitech.dms.web.model.dealer.employee.view.response.EmployeeViewResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class EmployeeViewDaoImpl implements EmployeeViewDao {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeViewDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings({ "rawtypes" })
	@Override
	public EmployeeViewResponseModel fetchEmployeeDetailById(String userCode, EmployeeViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEmployeeDetailById invoked.." + requestModel.toString());
		}
		Session session = null;
		EmployeeViewResponseModel responseModel = null;
		List<EmployeeAddressViewResponseModel> empAddressList = null;
		try {
			session = sessionFactory.openSession();
			List data = fetchEmpDetail(session, userCode, requestModel, 1);
			if (data != null && !data.isEmpty()) {
				responseModel = new EmployeeViewResponseModel();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel.setDealerId((BigInteger) row.get("DealerId"));
					responseModel.setDealerName((String) row.get("DealerName"));
					responseModel.setEmployeeId((BigInteger) row.get("EmployeeId"));
					responseModel.setIsDefultSalesman((Character) row.get("IsDefultSalesman"));
					responseModel.setEmployeeCode((String) row.get("EmployeeCode"));
					responseModel.setIsActive((Character) row.get("IsActive"));
					responseModel.setDealerDepartmentId((Integer) row.get("DealerDepartmentId"));
					responseModel.setDealerDepartment((String) row.get("DealerDepartment"));
					responseModel.setDealerDesignationId((Integer) row.get("DealerDesignationId"));
					responseModel.setDealerDesignation((String) row.get("DealerDesignation"));
					responseModel.setTitle((String) row.get("Title"));
					responseModel.setTitleId((BigInteger) row.get("TitleId"));
					responseModel.setFirstName((String) row.get("FirstName"));
					responseModel.setMiddleName((String) row.get("MiddleName"));
					responseModel.setLastName((String) row.get("LastName"));
					responseModel.setMobileNumber((String) row.get("MobileNumber"));
					responseModel.setEmail((String) row.get("Email"));
					responseModel.setAlternateMobileNo((String) row.get("AlternateMobileNo"));
					responseModel.setDrivingLicenceType((String) row.get("DrivingLicenceType"));
					responseModel.setDrivingLicenceTypeId((BigInteger) row.get("DrivingLicenceTypeId"));
					responseModel.setDrivingLicenceNo((String) row.get("DrivingLicenceNo"));
					responseModel.setDrivingLicenceExpiryDate((String) row.get("DrivingLicenceExpiryDate"));
					responseModel.setReportingEmployeeId((BigInteger) row.get("ReportingEmployeeId"));
					responseModel.setReportingEmployee((String) row.get("ReportingEmployee"));
					responseModel.setBirthDate((String) row.get("BirthDate"));
					responseModel.setAnnivarsaryDate((String) row.get("AnnivarsaryDate"));
					responseModel.setQualification((String) row.get("Qualification"));
					responseModel.setMaritalStatus((String) row.get("MaritalStatus"));
					responseModel.setBloodGroup((String) row.get("BloodGroup"));
					responseModel.setGender((String) row.get("Gender"));
					responseModel.setEmgContactName((String) row.get("EmgContactName"));
					responseModel.setEmgContactNo((String) row.get("EmgContactNo"));
					responseModel.setJoiningDate((String) row.get("JoiningDate"));
					responseModel.setSalary((BigDecimal) row.get("Salary"));
					responseModel.setLeavingDate((String) row.get("LeavingDate"));
					responseModel.setPfNo((String) row.get("PfNo"));
					responseModel.setPanNo((String) row.get("PanNo"));
					responseModel.setEsiNo((String) row.get("EsiNo"));
					responseModel.setBankAcNo((String) row.get("BankAcNo"));
					responseModel.setBankName((String) row.get("BankName"));
					responseModel.setBankBranch((String) row.get("BankBranch"));
					responseModel.setIfscCode((String) row.get("ifscCode"));
					responseModel.setAdharcardNo((String) row.get("adharcardNo"));
					responseModel.setPasspostNo((String) row.get("passpostNo"));
				}

				data = fetchEmpDetail(session, userCode, requestModel, 2);
				if (data != null && !data.isEmpty()) {
					empAddressList = new ArrayList<EmployeeAddressViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						EmployeeAddressViewResponseModel addressViewResponseModel = new EmployeeAddressViewResponseModel();
						addressViewResponseModel.setEmpAddressId((BigInteger) row.get("EmpAddressId"));
						addressViewResponseModel.setEmployeeId((BigInteger) row.get("EmployeeId"));
						addressViewResponseModel.setCustAddress1((String) row.get("CustAddress1"));
						addressViewResponseModel.setCustAddLine2((String) row.get("CustAddress2"));
						addressViewResponseModel.setCity((String) row.get("CustAddress3"));
						addressViewResponseModel.setCityId((BigInteger) row.get("CityId"));
						addressViewResponseModel.setCity((String) row.get("City"));
						addressViewResponseModel.setPinId((BigInteger) row.get("PinId"));
						addressViewResponseModel.setLocality((String) row.get("Locality"));
						addressViewResponseModel.setPinCode((String) row.get("PinCode"));
						addressViewResponseModel.setDistrict((String) row.get("District"));
						addressViewResponseModel.setDistrictId((BigInteger) row.get("DistrictId"));
						addressViewResponseModel.setTehsil((String) row.get("Tehsil"));
						addressViewResponseModel.setTehsilId((BigInteger) row.get("TehsilId"));
						addressViewResponseModel.setState((String) row.get("State"));
						addressViewResponseModel.setStateId((BigInteger) row.get("StateId"));
						addressViewResponseModel.setCountry((String) row.get("Country"));
						addressViewResponseModel.setCountryId((BigInteger) row.get("CountryId"));
						empAddressList.add(addressViewResponseModel);
					}
					responseModel.setDealerAddressList(empAddressList);
				}
			}

		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;

	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List fetchEmpDetail(Session session, String userCode, EmployeeViewRequestModel requestModel, int flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEmpDetail invoked..");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "[SP_DLR_EMP_DTL] :userCode, :empId, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("empId", requestModel.getEmployeeId());
			query.setParameter("flag", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			data = query.list();
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return data;
	}
}
