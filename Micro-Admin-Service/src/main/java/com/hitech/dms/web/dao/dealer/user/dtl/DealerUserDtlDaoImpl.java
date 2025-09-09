/**
 * 
 */
package com.hitech.dms.web.dao.dealer.user.dtl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.dao.common.dao.CommonDao;
import com.hitech.dms.web.model.dealer.user.dtl.request.DealerEmpAutoSearchRequestModel;
import com.hitech.dms.web.model.dealer.user.dtl.request.DealerEmpDtlRequestModel;
import com.hitech.dms.web.model.dealer.user.dtl.request.DealerEmpUserDtlRequestModel;
import com.hitech.dms.web.model.dealer.user.dtl.response.DealerEmpAutoSearchResponseModel;
import com.hitech.dms.web.model.dealer.user.dtl.response.DealerEmpDtlResponseModel;
import com.hitech.dms.web.model.dealer.user.dtl.response.DealerEmpUserDtlResponseModel;
import com.hitech.dms.web.model.dealer.user.dtl.response.DealerEmpUserRoleListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class DealerUserDtlDaoImpl implements DealerUserDtlDao {
	private static final Logger logger = LoggerFactory.getLogger(DealerUserDtlDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public DealerEmpDtlResponseModel fetchEmployeeDTLForUserById(String userCode,
			DealerEmpDtlRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEmployeeDTLForUserById invoked..");
		}
		Session session = null;
		NativeQuery<?> query = null;
		DealerEmpDtlResponseModel responseModel = null;
		String sqlQuery = "Select emp_Id as empId, EmpCode, MobileNumber,  "
				+ " ISNULL(stuff(coalesce(' '+nullif(FirstName, ''), '') + coalesce(' '+nullif(MiddleName, ''), '') + coalesce(' '+nullif(LastName, ''), '') + coalesce(' '+nullif(MobileNumber, ''), '') ,1,1,''),'') as displayValue "
				+ " from ADM_BP_DEALER_EMP (nolock) where emp_Id =:empId";
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("empId", requestModel.getEmpId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new DealerEmpDtlResponseModel();
					responseModel.setEmpId((BigInteger) row.get("empId"));
					responseModel.setEmpCode((String) row.get("EmpCode"));
					responseModel.setMobileNumber((String) row.get("MobileNumber"));
					responseModel.setDisplayValue((String) row.get("displayValue"));
				}
			}
		} catch (SQLGrammarException ex) {

		} catch (Exception ex) {

		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public Map<String, Object> fetchEmployeeAutoList(String userCode, DealerEmpAutoSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEmployeeAutoList invoked..");
		}
		Session session = null;
		NativeQuery<?> query = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		List<DealerEmpAutoSearchResponseModel> responseModelList = null;
		String sqlQuery = "[SP_ADM_AutoSearch_EMPList] :userCode, :empText, :dealerId, :isFor";
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("empText", requestModel.getEmpCode());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			responseModelList = (List<DealerEmpAutoSearchResponseModel>) query.list();
			if (responseModelList != null && !responseModelList.isEmpty()) {
				mapData.put("responseModelList", responseModelList);
				mapData.put("msg", "SUCCESS");
			} else {
				mapData.put("msg", "Employee Not Found.");
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
			mapData.put("msg", "Employee Not Found.");
		} catch (Exception ex) {
			mapData.put("msg", "Employee Not Found.");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mapData;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List fetchEmpUserDetail(Session session, String userCode,
			DealerEmpUserDtlRequestModel requestModel, int flag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEmpUserDetail invoked..");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "[SP_ADM_DLREMP_USRDTL] :userCode, :empId, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("empId", requestModel.getEmpId());
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

	@SuppressWarnings({ "rawtypes" })
	public DealerEmpUserDtlResponseModel fetchEmpUserDetail(String userCode,
			DealerEmpUserDtlRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEmpUserDetail invoked..");
		}
		Session session = null;
		DealerEmpUserDtlResponseModel responseModel = null;
		List<DealerEmpUserRoleListResponseModel> roleList = null;
		try {
			session = sessionFactory.openSession();
			List data = fetchEmpUserDetail(session, userCode, requestModel, 1);
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new DealerEmpUserDtlResponseModel();

					responseModel.setProfitCenterId((Integer) row.get("pcId"));
					responseModel.setProfitCenterDesc((String) row.get("pcDesc"));
					responseModel.setDealerId((BigInteger) row.get("dealerId"));
					responseModel.setDealerName((String) row.get("dealerName"));
					responseModel.setEmpId((BigInteger) row.get("empid"));
					responseModel.setEmpCode((String) row.get("EmpCode"));
					responseModel.setEmpName((String) row.get("empName"));
					responseModel.setPassword((String) row.get("Password"));
					responseModel.setConfirmPassword((String) row.get("Password"));
					responseModel.setUserCode((String) row.get("UserCode"));
					responseModel.setUserId((BigInteger) row.get("user_id"));
					responseModel.setUserTypeId((Integer) row.get("userTypeId"));
					Character isActive = (Character) row.get("isActive");
					if (isActive != null && isActive.toString().equals("Y")) {
						responseModel.setIsActive(true);
					} else {
						responseModel.setIsActive(false);
					}

				}
				
				DealerEmpUserRoleListResponseModel roleModel = null;
				roleList = new ArrayList<DealerEmpUserRoleListResponseModel>();
				data = fetchEmpUserDetail(session, userCode, requestModel, 2);
				if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						Map row = (Map) object;
						roleModel = new DealerEmpUserRoleListResponseModel();
						
						roleModel.setRoleId((BigInteger) row.get("roleid"));
						roleModel.setUserId((BigInteger) row.get("userid"));
						roleModel.setRoleCode((String) row.get("roleCode"));
						roleModel.setRoleDesc((String) row.get("roleDesc"));
						roleModel.setUserRoleId((BigInteger) row.get("userRoleId"));
						Character isActive = (Character) row.get("isActive");
						if (isActive != null && isActive.toString().equals("Y")) {
							roleModel.setIsActive(true);
						} else {
							roleModel.setIsActive(false);
						}
						
						roleList.add(roleModel);
					}
				}
				responseModel.setRoleList(roleList);
			}
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}
}
