/**
 * 
 */
package com.hitech.dms.web.dao.common.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.department.list.response.DepartmentListResponseModel;
import com.hitech.dms.web.model.desig.list.response.DealerDesiginationResponseModel;
import com.hitech.dms.web.model.desig.list.response.DesiginationLevelResponseModel;
import com.hitech.dms.web.model.desig.list.response.DesiginationResponseModel;
import com.hitech.dms.web.model.user.role.response.UserRoleResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class CommonDaoImpl implements CommonDao {
	private static final Logger logger = LoggerFactory.getLogger(CommonDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	/*
	 * fetching User role List by user id
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<UserRoleResponseModel> fetchUserRoleList(Session session, String userCode, String isFor,
			BigInteger userId) {
		List<UserRoleResponseModel> responseList = null;
		String sqlQuery = "exec [SP_CM_FETCHROLELIST] :userCode, :isFor, :userId";
		try {
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("isFor", isFor);
			query.setParameter("userId", userId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<UserRoleResponseModel>();
				UserRoleResponseModel role = null;
				for (Object object : data) {
					Map row = (Map) object;
					role = new UserRoleResponseModel();
					role.setUserId((BigInteger) row.get("user_id"));
					role.setUserRoleId((BigInteger) row.get("user_role_id"));
					role.setRoleId((BigInteger) row.get("role_id"));
					role.setRoleDesc((String) row.get("RoleName"));
					role.setRoleCode((String) row.get("RoleCode"));
					Character isActive = (Character) row.get("IsActive");
					if (isActive != null && isActive.toString().equals("Y")) {
						role.setIsActive(true);
					} else {
						role.setIsActive(false);
					}
					responseList.add(role);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		}
		return responseList;
	}

	public List<UserRoleResponseModel> fetchUserRoleList(String userCode, String isFor, BigInteger userId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchUserRoleList invoked.." + userCode + " " + userId);
		}
		Session session = null;
		List<UserRoleResponseModel> responseList = null;
		try {
			session = sessionFactory.openSession();
			responseList = fetchUserRoleList(session, userCode, isFor, userId);
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseList;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<DepartmentListResponseModel> fetchDepartmentList(String userCode, String applicableForDealer,
			String isActive) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDepartmentList invoked.." + userCode + " " + applicableForDealer);
		}
		Session session = null;
		List<DepartmentListResponseModel> responseList = null;
		String sqlQuery = "select amd.* from ADM_MST_DEPARTMENT (nolock) amd " + " where IsActive =:isActive ";
		if (applicableForDealer != null) {
			sqlQuery = sqlQuery + " and ApplicableForDealer =:applicableForDealer ";
		}
		try {
			session = sessionFactory.openSession();
			Query query = session.createNativeQuery(sqlQuery);
//			query.setParameter("userCode", userCode);
			query.setParameter("isActive", isActive);
			if (applicableForDealer != null) {
				query.setParameter("applicableForDealer", applicableForDealer);
			}
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<DepartmentListResponseModel>();
				DepartmentListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new DepartmentListResponseModel();
					responseModel.setDepartmentId((Integer) row.get("department_id"));
					responseModel.setDepartmentCode((String) row.get("DepartmentCode"));
					responseModel.setDepartmentDesc((String) row.get("DepartmentDesc"));
					responseList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseList;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<DesiginationResponseModel> fetchDesigList(String userCode, Integer departmentId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDesigList invoked.." + userCode + " " + departmentId);
		}
		Session session = null;
		List<DesiginationResponseModel> responseList = null;
		String sqlQuery = "select amd.* from ADM_HO_MST_DESIG (nolock) amd "
				+ " where department_id =:departmentId and IsActive = 'Y' ";
		try {
			session = sessionFactory.openSession();
			Query query = session.createNativeQuery(sqlQuery);
//			query.setParameter("userCode", userCode);
			query.setParameter("departmentId", departmentId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<DesiginationResponseModel>();
				DesiginationResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new DesiginationResponseModel();
					responseModel.setDesigId((Integer) row.get("ho_designation_id"));
					responseModel.setDesigCode((String) row.get("DesignationCode"));
					responseModel.setDesigDesc((String) row.get("DesignationDesc"));
					responseModel.setDepartmentId((Integer) row.get("department_id"));
					responseList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseList;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<DesiginationLevelResponseModel> fetchDesigLevelList(String userCode, Integer departmentId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDesigLevelList invoked.." + userCode + " " + departmentId);
		}
		Session session = null;
		List<DesiginationLevelResponseModel> responseList = null;
		String sqlQuery = "select amd.* from ADM_HO_MST_DESIG_LEVEL (nolock) amd "
				+ " where department_id =:departmentId and IsActive = 'Y' ";
		try {
			session = sessionFactory.openSession();
			Query query = session.createNativeQuery(sqlQuery);
//			query.setParameter("userCode", userCode);
			query.setParameter("departmentId", departmentId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<DesiginationLevelResponseModel>();
				DesiginationLevelResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new DesiginationLevelResponseModel();
					responseModel.setDesigLevelId((Integer) row.get("ho_designation_level_id"));
					responseModel.setDesigLevelCode((String) row.get("DesignationLevelCode"));
					responseModel.setDesigLevelDesc((String) row.get("DesignationLevelDesc"));
					responseModel.setDepartmentId((Integer) row.get("department_id"));
					responseList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchHOUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select HO.ho_usr_id from ADM_HO_USER (nolock) HO INNER JOIN ADM_USER (nolock) u ON u.ho_usr_id = HO.ho_usr_id where u.UserCode =:userCode";
		mapData.put("ERROR", "HO USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("ho_usr_id");
				}
				mapData.put("hoUserId", userId);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING HO USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING HO USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
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

	@Override
	public List<DealerDesiginationResponseModel> fetchDealerDesigList(String userCode, Integer dealerDepartmentId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDealerDesigLevelList invoked.." + userCode + " " + dealerDepartmentId);
		}
		Session session = null;
		List<DealerDesiginationResponseModel> responseList = null;
		String sqlQuery = "select amd.* from ADM_BP_MST_DEALER_DESIG (nolock) amd "
				+ " where department_id =:departmentId and IsActive = 'Y' ";
		try {
			session = sessionFactory.openSession();
			Query query = session.createNativeQuery(sqlQuery);
//			query.setParameter("userCode", userCode);
			query.setParameter("departmentId", dealerDepartmentId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<DealerDesiginationResponseModel>();
				DealerDesiginationResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new DealerDesiginationResponseModel();
					responseModel.setDealerDesignationId((Integer) row.get("dlr_designation_id"));
					responseModel.setDesignationCode((String) row.get("DesignationCode"));
					responseModel.setDesignationDesc((String) row.get("DesignationDesc"));
					responseList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseList;
	}

	@Override
	public String getPartyIdFromdealercode(String userCode, Integer dealerCode) {
	    if (logger.isDebugEnabled()) {
	        logger.debug("getPartyIdFromdealcode invoked.." + userCode + " " + dealerCode);
	    }

	    Session session = null;
	    String partyId = null;

	    String sqlQuery = "exec [GetoldDMSdealerid] :dealerCode";

	    try {
	        session = sessionFactory.openSession();
	        Query query = session.createNativeQuery(sqlQuery);
	        query.setParameter("dealerCode", dealerCode);
	        Object result = query.getSingleResult();
	        if (result != null) {
	            partyId = String.valueOf(result);
	        }

	     } catch (Exception exp) {
	        logger.error("Error executing [GetoldDMSdealerid]", exp);
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }

	    return partyId;
	}


}
