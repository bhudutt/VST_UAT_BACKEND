package com.hitech.dms.web.dao.user.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.dao.user.UserAuthDao;
import com.hitech.dms.web.entity.response.user.RoleEnum;
import com.hitech.dms.web.entity.response.user.UserRestEntity;
import com.hitech.dms.web.entity.user.UserCustomerEntity;
import com.hitech.dms.web.entity.user.UserEntity;
import com.hitech.dms.web.entity.user.UserHoEntity;
import com.hitech.dms.web.entity.user.UserTypeEntity;
import com.hitech.dms.web.model.user.User;

@Repository
public class UserAuthDaoImpl implements UserAuthDao {
	private static final Logger logger = LoggerFactory.getLogger(UserAuthDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private HibernateTransactionManager hibernateTransactionManager;
	@Autowired
	private Mapper mapper;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserEntity findByUser(String userName) {
		Session session = null;
		UserEntity user = null;
		String hqlQuery = "from UserEntity where username =:userCode and isActive ='Y'";
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery(hqlQuery);
			query.setParameter("userCode", userName);
			user = (UserEntity) query.uniqueResult();
			if (user != null) {
				user.setPasswordHash(user.getPassword());
				// CustomPasswordEncoder customPasswordEncoder = new CustomPasswordEncoder();
				// String encoded = customPasswordEncoder.encode(user.getPassword());
				// user.setPassword(encoded);
				UserTypeEntity userType = user.getUserType();
//				if (userType != null && user.getUsrCustId() != null) {
//					UserCustomerEntity userCustomerEntity = fetchUserCustomerDetails(session, user.getUsrCustId());
//					user.setUserCustModel(userCustomerEntity);
//				} else 
				if (userType != null && user.getHoUserId() != null) {
					UserHoEntity userHoEntity = fetchHOEmpDetails(session, user.getHoUserId());
					user.setUserHoModel(userHoEntity);
				}
//					else if (userType != null && user.getUserDLRId() != null) {
//
//				}
				
				if(user.getDlrEmpId()!=null) {
					boolean checkDealerStatus = checkDealerStatus(user.getDlrEmpId(),session);
					if(!checkDealerStatus) {
						user.setIsActive(false);					}
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return user;
	}

	@SuppressWarnings({ "rawtypes" })
	public UserCustomerEntity fetchUserCustomerDetails(Session session, Long usrCustId) {
		UserCustomerEntity userCustomerEntity = null;
		String sqlQuery = "select usrCust.* from adm_user_cust usrCust " + " where usrCust.usr_cust_id =:usrCustId";
		try {
			Query query = session.createNativeQuery(sqlQuery).addEntity(UserCustomerEntity.class);
			query.setParameter("usrCustId", usrCustId);
			userCustomerEntity = (UserCustomerEntity) query.uniqueResult();
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return userCustomerEntity;
	}
	
	
	
	public boolean checkDealerStatus(Long dlrEmpId,Session session) {
		boolean check=false;
		String sqlQuery = "select dlr.IsActive from ADM_BP_DEALER_EMP e"
				+ " inner join ADM_BP_DEALER dlr on e.dealer_Id=dlr.parent_dealer_id " 
				+ " where e.emp_Id=:dlrId and dlr.IsActive='Y' ";
		try {
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("dlrId", dlrEmpId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if(data !=null && data.size()>0) {
				check=true;
			}
			
			
		}catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		
		return check;
		
		
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public UserHoEntity fetchHOEmpDetails(Session session, Long hoUsrId) {
		UserHoEntity userHoEntity = null;
		String sqlQuery = "select usrHo.* from ADM_HO_USER usrHo " + " where usrHo.ho_usr_id =:hoUsrId";
		try {
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("hoUsrId", hoUsrId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					userHoEntity = new UserHoEntity();
					userHoEntity.setHoUserId(((BigInteger) row.get("ho_usr_id")).longValue());
					Character isActive = (Character) row.get("IsActive");
					if (isActive != null && isActive.toString().equals("Y")) {
						userHoEntity.setIsActive(true);
					} else {
						userHoEntity.setIsActive(false);
					}
					userHoEntity.setEmployeeCode((String) row.get("employee_code"));
					userHoEntity.setEmployeeName((String) row.get("employee_name"));
					userHoEntity.setPFirstName((String) row.get("emp_first_name"));
					userHoEntity.setPMiddleName((String) row.get("emp_middle_name"));
					userHoEntity.setPLastName((String) row.get("emp_last_name"));
					userHoEntity.setEmpContactNo((String) row.get("Emp_ContactNo"));
					userHoEntity.setEmpMail((String) row.get("Emp_mail"));
					userHoEntity.setCreatedBy((String) row.get("created_by"));
					userHoEntity.setCreatedDate((Date) row.get("created_date"));
					userHoEntity.setModifiedBy((String) row.get("modified_by"));
					userHoEntity.setModifiedDate((Date) row.get("modified_date"));
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return userHoEntity;
	}

	public UserRestEntity findByUserName(String userName) {
		Session session = null;
		UserRestEntity user = null;
		try {
			session = sessionFactory.openSession();
			user = findByUserName(session, userName);
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return user;
	}

	@SuppressWarnings("rawtypes")
	public UserRestEntity findByUserName(Session session, String userName) {
		UserRestEntity user = null;
		String hqlQuery = "Select AU.* from ADM_USER (nolock) AU where AU.UserCode =:userCode";
		try {
			Query query = session.createNativeQuery(hqlQuery).addEntity(UserEntity.class);
			query.setParameter("userCode", userName);
			UserEntity userEntity = (UserEntity) query.uniqueResult();
			if (userEntity != null) {
				user = new UserRestEntity();
				user.setUserDLRId(userEntity.getDlrEmpId());
				user.setHoUserId(userEntity.getHoUserId());
				user.setUsername(userEntity.getUsername());
				user.setUserType(userEntity.getUserType());
				doOtherStuffsForUser(session, user);
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return user;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	private UserRestEntity doOtherStuffsForUser(Session session, UserRestEntity user) {
		Query query = null;
		String sqlQuery = "exec [API_USER_LOGIN] :userCode";
		try {
			UserTypeEntity userType = user.getUserType();
			user.setRole(userType.getUserType());
//			if (userType != null && user.getUsrCustId() != null) {
//				UserCustomerEntity userCustomerEntity = fetchUserCustomerDetails(session, user.getUsrCustId());
//				user.setUserFullName(userCustomerEntity.getUserName());
//			} else 
//			if (userType != null && user.getHoUserId() != null) {
//				UserHoEntity userHoEntity = fetchHOEmpDetails(session, user.getHoUserId());
//				user.setUserFullName(userHoEntity.getEmployeeName());
//			}
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", user.getUsername());
			List data = query.list();
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					user.setBranchId((BigInteger) row.get("defaultBranchId"));
					user.setDealerCode((String) row.get("dealerCode"));
					user.setDealerId((BigInteger) row.get("dealerId"));
					user.setDealerName((String) row.get("dealerName"));
					user.setDealerLocation((String) row.get("dealerLocation"));
					user.setEmail((String) row.get("Email"));
					BigInteger userDLRId = (BigInteger) row.get("EmpId");
					if(userDLRId != null && userDLRId.compareTo(BigInteger.ZERO) > 0) {
						user.setUserDLRId(userDLRId.longValue());
					}
					BigInteger hoUserId = (BigInteger) row.get("hoUserId");
					if(hoUserId != null && hoUserId.compareTo(BigInteger.ZERO) > 0) {
						user.setHoUserId(hoUserId.longValue());
					}
					user.setLocality((String) row.get("Locality"));
					user.setMobileNumber((String) row.get("MobileNumber"));
					user.setUserFullName((String) row.get("EmpName"));
					user.setDesignationDesc((String) row.get("DesignationDesc"));
					user.setDealerTypeId((Integer)row.get("DealerTypeId"));
					user.setDesignationDescLevel((String)row.get("DesignationDescLevel"));
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return user;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchUserDTL(Session session, String userCode, Integer userId) {
		Map<String, Object> mapData = null;
		Query query = null;
		List data = null;
		String sqlQuery = "select CASE WHEN ISNULL(AMU.emp_id, '') <> '' THEN ISNULL(ABE.FirstName,'')+' '+ISNULL(ABE.MiddleName,'')+' '+ISNULL(ABE.LastName,'') "
				+ "	else ADH.P_Name end AS Name " + "   from ADM_USER AMU "
				+ "	LEFT JOIN  ADM_BP_EMP ABE ON ABE.emp_id = AMU.emp_id "
				+ "	LEFT JOIN ADM_HO_USER ADH on ADH.ho_usr_id = AMU.ho_usr_id "
				+ "	where AMU.username =:userCode or user_Id = :userId";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("userId", userId);
			data = query.list();
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			data = query.list();
			if (data != null && !data.isEmpty()) {
				mapData = new HashMap<String, Object>();
				for (Object object : data) {
					Map row = (Map) object;
					mapData.put("UserName", (String) row.get("Name"));
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			sqlQuery = null;
			query = null;
			data = null;
		}
		return mapData;
	}

	public Map<String, Object> fetchUserDTL(String userCode, Integer userId) {
		Session session = null;
		Map<String, Object> mapData = null;
		try {
			session = sessionFactory.openSession();
			mapData = fetchUserDTL(session, userCode, userId);
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mapData;
	}

	public List<UserTypeEntity> userTypeList() {
		Session session = null;
		List<UserTypeEntity> userTypeList = null;
		String hqlQuery = "from UserTypeEntity where isActive = 'Y'";
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery(hqlQuery);
			userTypeList = query.list();
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return userTypeList;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, Object> validateUserByUserCode(User user) {
		Session session = null;
		Query query = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("MSGSTATUS", "ERROR");
		mapData.put("MSG",
				messageSource.getMessage("label.server.error", new Object[] {}, LocaleContextHolder.getLocale()));
		String sqlQuery = "from UserEntity where userCode like (:userCode)";
		try {
			session = sessionFactory.openSession();
			query = session.createQuery(sqlQuery);
			query.setParameter("userCode", user.getUsername());
			UserEntity userEntity = (UserEntity) query.uniqueResult();
			if (userEntity == null) {
				mapData.put("MSGSTATUS", "AVAILABLE");
				mapData.put("MSG", "USER CODE AVIALABLE.");
			} else {
				mapData.put("MSG", messageSource.getMessage("label.user.name.ntavailable",
						new Object[] { user.getUsername() }, LocaleContextHolder.getLocale()));
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mapData;
	}

	@Override
	public Map<String, String> updateUserProfile(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> validateCurrentPassword(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> resetPassword(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> changePassword(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateOTPNumber(User authenticationRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}