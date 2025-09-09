package com.hitech.dms.web.dao.dealer.employee.search;

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

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.dealer.employee.search.request.DealerEmployeeSearchRequest;
import com.hitech.dms.web.model.dealer.employee.search.response.DealerEmployeeSearchMainResponse;
import com.hitech.dms.web.model.dealer.employee.search.response.DealerEmployeeSearchResponse;

/**
 * @author vinay.gautam
 *
 */

@Repository
public class DealerEmployeeSearchDaoImpl implements DealerEmployeeSearchDao{
	
	private static final Logger logger = LoggerFactory.getLogger(DealerEmployeeSearchDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("deprecation")
	@Override
	public DealerEmployeeSearchMainResponse fetchDealerEmployeeSearch(String userCode,DealerEmployeeSearchRequest requestModel) {
		NativeQuery<?> query = null;
		Session session = null;
		DealerEmployeeSearchResponse responseModel = null;
		List<DealerEmployeeSearchResponse> responseModelList = null;
		DealerEmployeeSearchMainResponse mainSearchResponse = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_DLR_EMP_SEARCH] :dealerId, :branchId, :userCode, :employeeCode, :mobileNo, :fromDate, :toDate ,:orgHierId, :includeInactive,:status,:page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("userCode", userCode);
			query.setParameter("employeeCode", requestModel.getEmployeeCode());
			query.setParameter("mobileNo", requestModel.getMobileNo());
			/*
			 * query.setParameter("fromDate", (requestModel.getFromDate() == null ? null :
			 * DateToStringParserUtils.addStratTimeOFTheDay(requestModel.getFromDate())));
			 * query.setParameter("toDate", (requestModel.getToDate() == null ? null :
			 * DateToStringParserUtils.addEndTimeOFTheDay(requestModel.getToDate())));
			 */
			query.setParameter("fromDate", (requestModel.getFromDate1()));
			query.setParameter("toDate", (requestModel.getToDate1()));
			query.setParameter("orgHierId", requestModel.getOrgHierId());
			query.setParameter("includeInactive", requestModel.getIncludeInactive());
			query.setParameter("status", requestModel.getStatus());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			
			logger.info("jasperParameter "+requestModel);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<DealerEmployeeSearchResponse>();
				mainSearchResponse = new DealerEmployeeSearchMainResponse();
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					responseModel = new DealerEmployeeSearchResponse();
					responseModel.setId((BigInteger) row.get("emp_Id"));
					responseModel.setAction((String) row.get("action"));
					responseModel.setEmployeeCode((String) row.get("employeeCode"));
//					responseModel.setTitle((String) row.get("ContactTitle"));
					responseModel.setEmployeeName((String) row.get("employeeName"));
					responseModel.setMiddleName((String) row.get("middleName"));
					responseModel.setLastName((String) row.get("lastName"));
					responseModel.setActiveStatus((Character) row.get("ActiveStatus"));
					responseModel.setEmailId((String) row.get("emailId"));
					responseModel.setMobileNumber((String) row.get("mobileNumber"));
					responseModel.setAlternateMobileNumber((String) row.get("alternateMobileNumber"));
					responseModel.setDepartmentName((String) row.get("departmentName"));
					responseModel.setDesignation((String) row.get("DesignationDesc"));
					responseModel.setLicenceType((String) row.get("licenceType"));
					responseModel.setDrivingLicenceNumber((String) row.get("drivingLicenceNumber"));
					responseModel.setDrivingLicenceExpiryDate((String) row.get("drivingLicenceExpiryDate"));
					responseModel.setJoiningDate((String) row.get("joiningDate"));
					responseModel.setLatestSalary((String) row.get("latestSalary"));
					responseModel.setLeavingDate((String) row.get("leavingDate"));
					responseModel.setPfNumber((String) row.get("pfNumber"));
					responseModel.setPanNumber((String) row.get("panNumber"));
					responseModel.setEsiNumber((String) row.get("esiNumber"));
					responseModel.setBankAccountNumber((String) row.get("bankAccountNumber"));
					responseModel.setBankName((String) row.get("bankName"));
					responseModel.setBankBranch((String) row.get("bankBranch"));
					responseModel.setAddress1((String) row.get("CustAddLine1"));
					responseModel.setAddress2((String) row.get("CustAddLine2"));
					responseModel.setAddress3((String) row.get("CustAddLine3"));
					responseModel.setPinCode((String) row.get("pinCode"));
					responseModel.setLocality((String) row.get("locality"));
					responseModel.setTehsil((String) row.get("TehsilDesc"));
					responseModel.setDistrict((String) row.get("DistrictDesc"));
					responseModel.setCity((String) row.get("CityDesc"));
					responseModel.setState((String) row.get("StateDesc"));
					responseModel.setCountry((String) row.get("country"));
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("COUNT");
					}
					responseModelList.add(responseModel);
					}
				mainSearchResponse.setSearch(responseModelList);
				mainSearchResponse.setRecordCount(recordCount);
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session.isOpen())
				session.close();
		}

		
		return mainSearchResponse;
	}
	


}
