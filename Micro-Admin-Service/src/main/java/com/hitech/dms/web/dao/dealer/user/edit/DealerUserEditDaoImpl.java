package com.hitech.dms.web.dao.dealer.user.edit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.dao.CommonDao;
import com.hitech.dms.web.entity.user.UserEntity;
import com.hitech.dms.web.entity.user.UserRoleEntity;
import com.hitech.dms.web.model.admin.create.request.HoUserRoleRequestModel;
import com.hitech.dms.web.model.dealer.user.create.request.DealerUserCreateRequestModel;
import com.hitech.dms.web.model.dealer.user.create.request.DealerUserRoleRequestModel;
import com.hitech.dms.web.model.dealer.user.create.response.DealerUserCreateResponseModel;

/**
 * @author vinay.gautam
 *
 */
@Repository
public class DealerUserEditDaoImpl implements DealerUserEditDao {

	private static final Logger logger = LoggerFactory.getLogger(DealerUserEditDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@SuppressWarnings("unchecked")
	@Override
	public DealerUserCreateResponseModel editDealerUser(String userCode, DealerUserCreateRequestModel requestModel,
			Device device) {
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		DealerUserCreateResponseModel responseModel = new DealerUserCreateResponseModel();
		boolean isSuccess = true;
		NativeQuery<?> query = null;
		Date currDate = new Date();
		String sqlQuery = "Select * from ADM_USER (nolock) where user_id = :userId";
		UserEntity dbUserEntity = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			// BigInteger userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				// userId = (BigInteger) mapData.get("userId");
				query = session.createNativeQuery(sqlQuery).addEntity(UserEntity.class);
				query.setParameter("userId", requestModel.getUserId());
				dbUserEntity = (UserEntity) query.uniqueResult();
				if (dbUserEntity != null && dbUserEntity.getUsername() != null) {
					if (requestModel.getPassword() != null
							&& !requestModel.getPassword().equals(dbUserEntity.getPassword())) {
						String encoded = passwordEncoder.encode(requestModel.getPassword());
						dbUserEntity.setPassword(encoded);
						dbUserEntity.setLastPasswordResetDate(currDate);
					}
					if (requestModel.getIsActive() != null) {
						dbUserEntity.setIsActive(requestModel.getIsActive());
					}
					if (requestModel.getIsActive() != null && !requestModel.getIsActive()) {
						sqlQuery = "select isActive from ADM_BP_Dealer_EMP(NOLOCK) where EmpCode=:userCode";
						query = session.createNativeQuery(sqlQuery);
						query.setParameter("userCode", dbUserEntity.getUsername());
						List<?> data = (List<?>) query.list();
						// System.out.println("data::::"+data);
						if (!data.isEmpty()) {
							if (data.get(0).equals('N')) {
								dbUserEntity.setStatus("Blocked");
							} else {
								dbUserEntity.setStatus("LIVE");
							}
						}
					} else if (requestModel.getIsActive() != null && requestModel.getIsActive()) {
						dbUserEntity.setStatus("LIVE");
					}
					dbUserEntity.setModifiedBy(userCode);
					dbUserEntity.setModifiedDate(currDate);
					session.merge(dbUserEntity);

					List<UserRoleEntity> userRoleList = new ArrayList<UserRoleEntity>();
					for (DealerUserRoleRequestModel roleRequestModel : requestModel.getRoleList()) {
						UserRoleEntity roleEntity = mapper.map(roleRequestModel, UserRoleEntity.class,
								"RoleDLRUserMapId");
						userRoleList.add(roleEntity);
					}

					sqlQuery = "Select * from ADM_MENU_ROLE_USER_HDR (nolock) where user_id = :userId";
					query = session.createNativeQuery(sqlQuery).addEntity(UserRoleEntity.class);
					query.setParameter("userId", dbUserEntity.getUserId());
					List<UserRoleEntity> userRoleDBList = (List<UserRoleEntity>) query.list();
					if (userRoleDBList != null && !userRoleDBList.isEmpty()) {
						List<UserRoleEntity> filteredRoleList = filterDistinctRoleList(userRoleDBList, userRoleList,
								userCode, currDate);
						for (UserRoleEntity userRoleEntity : filteredRoleList) {

							userRoleEntity.setUserId(dbUserEntity.getUserId());
							if (userRoleEntity.getUserRoleId() != null) {
								session.merge(userRoleEntity);
							} else {
								session.saveOrUpdate(userRoleEntity);
							}
						}
					} else {
						// inserting new roles
						for (UserRoleEntity userRoleEntity : userRoleList) {
							userRoleEntity.setUserId(dbUserEntity.getUserId());
							userRoleEntity.setCreatedBy(userCode);
							userRoleEntity.setCreatedDate(currDate);
							session.saveOrUpdate(userRoleEntity);
						}
					}
				} else {
					responseModel.setMsg("User Type Not Found. Contact Your System Administrator.");
					isSuccess = false;
				}

			} else {
				responseModel.setMsg("User Not Found.");
				isSuccess = false;
			}
			if (isSuccess) {
				responseModel.setMsg("Dealer User Updated Successfully.");
				responseModel.setUserCode(dbUserEntity.getUsername());
				responseModel.setUserId(dbUserEntity.getUserId());
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				transaction.commit();
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
			if (!isSuccess) {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Updating Dealer User.");
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	public static List<UserRoleEntity> filterDistinctRoleList(List<UserRoleEntity> userRoleDBList,
			List<UserRoleEntity> userRoleList, String userCode, Date currDate) {
		List<UserRoleEntity> listOneList = new ArrayList<UserRoleEntity>();
		for (UserRoleEntity roleEntity : userRoleList) {
			for (UserRoleEntity roleDBEntity : userRoleDBList) {
				if (roleDBEntity.getRoleId() != null && roleEntity.getRoleId() != null
						&& roleDBEntity.getRoleId().compareTo(roleEntity.getRoleId()) == 0) {
					roleEntity.setUserRoleId(roleDBEntity.getUserRoleId());
					roleEntity.setRoleId(roleDBEntity.getRoleId());
					roleEntity.setIsActive(roleEntity.getIsActive());
					roleEntity.setModifiedBy(userCode);
					roleEntity.setModifiedDate(currDate);
				} else {
					roleEntity.setCreatedBy(userCode);
					roleEntity.setCreatedDate(currDate);
				}
			}
			listOneList.add(roleEntity);
		}
		// We return the collected list.
		return listOneList;
	}

}
