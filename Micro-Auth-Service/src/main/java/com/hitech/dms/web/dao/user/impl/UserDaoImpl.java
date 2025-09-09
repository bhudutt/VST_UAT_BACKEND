package com.hitech.dms.web.dao.user.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.dao.user.UserDao;
import com.hitech.dms.web.entity.user.AccessLogEntity;
import com.hitech.dms.web.entity.user.MenuURLCodeEntity;
import com.hitech.dms.web.model.user.AppMenuModel;
import com.hitech.dms.web.model.user.MenuDetailListAnySize;
import com.hitech.dms.web.model.user.RoleDtlModel;
import com.hitech.dms.web.model.user.UserMenu;
import com.hitech.dms.web.model.user.accesslog.request.AccessLogRequestModel;
import com.hitech.dms.web.model.user.accesslog.request.AccessLogUpdateRequestModel;
import com.hitech.dms.web.model.user.activitylog.response.UserActivityLogResponseModel;

@Repository
public class UserDaoImpl implements UserDao {
	private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private Mapper mapper;

	@Override
	public List<UserMenu> fetchUserMenu(String userName) {
		Session session = null;
		List<UserMenu> detailsList = null;
		List<?> userMenuDetailsList = null;
		UserMenu menuDetails = null;
		Transaction transaction = null;
		Query query = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createSQLQuery("exec SP_CM_GetMenuDetails :UserCode");
			query.setParameter("UserCode", userName);
			userMenuDetailsList = query.list();
			if (null != userMenuDetailsList && !userMenuDetailsList.isEmpty()) {
				detailsList = new ArrayList<UserMenu>();
				for (int i = 0; i < userMenuDetailsList.size(); i++) {
					Object[] objectArray = (Object[]) userMenuDetailsList.get(i);
					menuDetails = new UserMenu();
					// menu Code
					menuDetails.setMenuCode((String) objectArray[0]);
					// menu Name
					menuDetails.setMenuName((String) objectArray[1]);
					// jsp Name
					menuDetails.setJspName((String) objectArray[2]);
					// page Title
					menuDetails.setPageTitle((String) objectArray[3]);
					// menu Status
					menuDetails.setMenuStatus((Character) objectArray[4]);
					if (menuDetails.getMenuStatus() != null) {
						menuDetails.setMenuStatusCheck(menuDetails.getMenuStatus().toString());
					}
					if (menuDetails.getJspName() != null && !menuDetails.getJspName().equals("")) {
						menuDetails.setUriName(menuDetails.getJspName());
					}
					menuDetails.setIndex(menuDetails.getMenuCode());
					menuDetails.setMenuUrl((String) objectArray[5]);
					detailsList.add(menuDetails);
				}
			}
		} catch (RuntimeException re) {
			transaction.rollback();
			re.printStackTrace();
		} finally {
			session.close();
		}
		MenuDetailListAnySize<UserMenu> menuDetails2 = new MenuDetailListAnySize<UserMenu>();
		ArrayList<UserMenu> finalList = new ArrayList<UserMenu>();
		if (null != detailsList && !detailsList.isEmpty()) {
			for (UserMenu details : detailsList) {
				if (details != null) {
					menuDetails2.add(Integer.valueOf(details.getIndex()), details);
				}
			}
			for (UserMenu details : menuDetails2) {
				if (details == null) {
					details = new UserMenu();
					details.setMenuStatusCheck("N");
					details.setJspName("");
					details.setMenuUrl("");
				}
				if (!finalList.contains(details))
					finalList.add(details);
			}
		}
		if (detailsList != null && !detailsList.isEmpty()) {
//			for (UserMenu details : detailsList) {
//				System.out.println("CNHI Menu : " + details.getMenuCode() + " : " + details.getMenuName());
//			}
		}
		return finalList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MenuURLCodeEntity> fetchMenuURLMasterList(String userCode) {
		Session session = null;
		List<MenuURLCodeEntity> urlCodeList = null;
		String hqlQuery = "From MenuURLCodeEntity";
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery(hqlQuery);
			// query.setCacheable(true);
			urlCodeList = query.list();

		} catch (SQLGrammarException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return urlCodeList;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public List<MenuURLCodeEntity> fetchUserMenuURLMasterList(String userCode) {
		Session session = null;
		List<MenuURLCodeEntity> urlCodeList = null;
		String hqlQuery = "exec [GetMenuURLDetails] :userCode";
		try {
			session = sessionFactory.openSession();
			Query query = session.createSQLQuery(hqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				MenuURLCodeEntity codeEntity = null;
				urlCodeList = new ArrayList<MenuURLCodeEntity>();
				for (Object object : data) {
					Map row = (Map) object;
					codeEntity = new MenuURLCodeEntity();
					codeEntity.setMenuCode((String) row.get("MenuCode"));
					codeEntity.setMenuUrl((String) row.get("MenuUrl"));
					urlCodeList.add(codeEntity);
				}
			}

		} catch (SQLGrammarException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return urlCodeList;
	}

	public UserActivityLogResponseModel insertIntoAccessLog(String userCode, AccessLogRequestModel requestModel) {
		Session session = null;
		Transaction tr = null;
		UserActivityLogResponseModel responseModel = new UserActivityLogResponseModel();
		AccessLogEntity accessLogEntity = null;
		BigInteger userLoginId = null;
		try {
			accessLogEntity = mapper.map(requestModel, AccessLogEntity.class, "UserAccessLogMapId");
			session = sessionFactory.openSession();
			tr = session.beginTransaction();
			userLoginId = (BigInteger) session.save(accessLogEntity);
			tr.commit();
			responseModel.setId(userLoginId);
			responseModel.setMsg("User Access Logged Successfuly.");
			responseModel.setStatusCode(200);
		} catch (Exception ex) {
			ex.printStackTrace();
			responseModel.setMsg("Error While Logging User Access.");
			responseModel.setStatusCode(500);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<RoleDtlModel> fetchUserRoleList(String userCode) {
		Session session = null;
		List<RoleDtlModel> roleList = null;
		String sqlQuery = "select MRH.role_id, MRH.role_code, MRH.role_name, MRH.is_active from ADM_MENU_ROLE_USER_HDR (nolock) RUH "
				+ " inner join ADM_MENU_ROLE_HDR (nolock) MRH on RUH.role_id = MRH.role_id "
				+ " inner join ADM_USER (nolock) AU on RUH.user_id = AU.user_id " + " where AU.UserCode =:userCode";
		try {
			session = sessionFactory.openSession();
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				roleList = new ArrayList<RoleDtlModel>();
				for (Object object : data) {
					Map row = (Map) object;
					RoleDtlModel roleDtlModel = new RoleDtlModel();
					roleDtlModel.setRoleId((BigInteger) row.get("role_id"));
					roleDtlModel.setRoleCode((String) row.get("role_code"));
					roleDtlModel.setRoleName((String) row.get("role_name"));
					Character isActive = (Character) row.get("is_active");
					if (isActive != null && isActive.toString().equals("Y")) {
						roleDtlModel.setIsActive(true);
					} else {
						roleDtlModel.setIsActive(false);
					}
					
					roleList.add(roleDtlModel);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return roleList;
	}

	public UserActivityLogResponseModel updateAccessLog(String userCode, AccessLogUpdateRequestModel requestModel) {
		Session session = null;
		Transaction tr = null;
		UserActivityLogResponseModel responseModel = new UserActivityLogResponseModel();
		try {
			logger.info("Login-User-Id : ", userCode);
			logger.info(requestModel != null ? requestModel.toString() : null);
			session = sessionFactory.openSession();
			AccessLogEntity accessLogEntity = (AccessLogEntity) session.get(AccessLogEntity.class,
					requestModel.getUserLoginId());
			if (accessLogEntity != null) {
				tr = session.beginTransaction();
				Date d = new Date();
				accessLogEntity.setLastAccessTime(d);
				accessLogEntity.setLogoutTime(d);
				session.merge(accessLogEntity);
				tr.commit();
				responseModel.setId(requestModel.getUserLoginId());
				responseModel.setMsg("User Access Logged Updated Successfuly.");
				responseModel.setStatusCode(200);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			responseModel.setMsg("Error While Updating User Access Log.");
			responseModel.setStatusCode(500);
		} finally {
			session.close();
		}
		return responseModel;
	}

	public Boolean updateLastLoginDate(String userCode, String fromWhere) {
		Session session = null;
		Boolean isUpdated = false;
		Transaction tr = null;
		try {
			session = sessionFactory.openSession();
			tr = session.beginTransaction();
			isUpdated = updateLastLoginDate(session, tr, userCode, fromWhere);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return isUpdated;

	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Boolean updateLastLoginDate(Session session, Transaction tr, String userCode, String fromWhere) {
		Boolean isUpdated = false;
		String sqlQuery = "UPDATE ADM_USER SET LastLogin_Web = getDate() where UserCode =:userCode";
		try {
			if (fromWhere != null) {
				if (fromWhere.equals("APP")) {
					sqlQuery = "UPDATE ADM_USER SET LastLogin_App = getDate() where UserCode =:userCode";
				}
				Query query = session.createNativeQuery(sqlQuery);
				query.setParameter("userCode", userCode);
				int k = query.executeUpdate();
				isUpdated = true;
				tr.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			isUpdated = false;
			tr.rollback();
		}
		return isUpdated;
	}

	@SuppressWarnings("rawtypes")
	private Integer userId(Session session, String userCode) {
		Integer userId = null;
		String hqlQuery = "Select userId from UserEntity where userCode =:userCode";
		try {
			Query query = session.createQuery(hqlQuery);
			query.setParameter("userCode", userCode);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				userId = (Integer) data.get(0);
			}
		} catch (HibernateException he) {
			he.printStackTrace();
		}
		return userId;
	}

	@Override
	public List<UserMenu> fetchPermissionsForUser(String userCode, String pmodule) {
		Session session = null;
		List<UserMenu> menuDetailList = null;
		try {
			session = sessionFactory.openSession();
			Integer userId = userId(session, userCode);
			menuDetailList = getPermissionList(session, userId, pmodule);
		} catch (HibernateException he) {
			he.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return menuDetailList;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	private List<UserMenu> getPermissionList(Session session, Integer userId, String pmodule) {
		List<UserMenu> menuDetailList = null;
		UserMenu menuDetails = null;
		String sqlQuery = "select menu_id, MenuName, MenuCode,  JSPName   from  ADM_MENU_MST   where "
				+ "	IsActive = 'Y'   and  PageTitle = 'Permission'   and JSPName=:pmodule " + "	and menu_id in ( "
				+ "	select  menu_id  from  ADM_MENU_ROLE_DTL  where " + "	role_id in  "
				+ "	(  select role_id  from ADM_MENU_ROLE_USER_HDR  where  user_id=:userId ) " + "	)  ";
		try {
			Query query = session.createNativeQuery(sqlQuery);
			query.setParameter("userId", userId);
			query.setParameter("pmodule", pmodule);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				menuDetailList = new ArrayList<UserMenu>();
				for (Object object : data) {
					Map row = (Map) object;
					menuDetails = new UserMenu();
					menuDetails.setMenuName((String) row.get("MenuName"));
					menuDetails.setMenuCode((String) row.get("MenuCode"));
					menuDetails.setJspName((String) row.get("JSPName"));
					menuDetailList.add(menuDetails);
				}
			}
		} catch (HibernateException he) {
			he.printStackTrace();
		}
		return menuDetailList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public List<AppMenuModel> getUserMenu(String userName) {
		Session session = null;
		List<AppMenuModel> detailsList = null;
		List<?> userMenuDetailsList = null;
		AppMenuModel menuDetails = null;
		Query query = null;
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery("exec [SP_CM_GetMenuDetails] :UserCode");
			query.setParameter("UserCode", userName);
			userMenuDetailsList = query.list();
			if (null != userMenuDetailsList && !userMenuDetailsList.isEmpty()) {
				detailsList = new ArrayList<AppMenuModel>();
				for (int i = 0; i < userMenuDetailsList.size(); i++) {
					Object[] objectArray = (Object[]) userMenuDetailsList.get(i);
					menuDetails = new AppMenuModel();
					menuDetails.setId(((BigInteger) objectArray[0]).longValue());
					// menu Code
					menuDetails.setMenuCode((String) objectArray[1]);
					// menu Name
					menuDetails.setDisplayName((String) objectArray[2]);
					// jsp Name
//					menuDetails.setJspName((String) objectArray[3]);
					// page Title
					menuDetails.setPageTitle((String) objectArray[4]);
					// menu Status
					Character isActive = (Character) objectArray[5];
					if (isActive != null && isActive.toString().equalsIgnoreCase("Y")) {
						menuDetails.setMenuStatus(true);
					} else {
						menuDetails.setMenuStatus(false);
					}
					menuDetails.setRoute((String) objectArray[6]);
					menuDetails.setParentId(((BigInteger) objectArray[7]).longValue());
					menuDetails.setIconName((String) objectArray[8]);
					menuDetails.setMenuOrder((Integer) objectArray[9]);
					menuDetails.setBadge((String) objectArray[10]);
					menuDetails.setBadgeBg((String) objectArray[11]);
					detailsList.add(menuDetails);
				}
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
		} finally {
			session.close();
		}
		if (detailsList != null && !detailsList.isEmpty()) {
//			for (UserMenu details : detailsList) {
//				System.out.println("CNHI Menu : " + details.getMenuCode() + " : " + details.getMenuName());
//			}
		}
		return detailsList;
	}
}
