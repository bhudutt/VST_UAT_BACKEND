package com.hitech.dms.web.dao.dealer.user.create;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.core.util.PasswordDecryptor;
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
import com.hitech.dms.web.dao.dealer.employee.create.DealerEmployeeDaoImpl;
import com.hitech.dms.web.entity.user.UserEntity;
import com.hitech.dms.web.entity.user.UserRoleEntity;
import com.hitech.dms.web.entity.user.UserTypeEntity;
import com.hitech.dms.web.model.dealer.user.create.request.DealerUserCreateRequestModel;
import com.hitech.dms.web.model.dealer.user.create.request.DealerUserRoleRequestModel;
import com.hitech.dms.web.model.dealer.user.create.response.DealerUserCreateResponseModel;

/**
 * @author vinay.gautam
 *
 */
@Repository
public class DealerUserCreateDaoImpl implements DealerUserCreateDao {

	private static final Logger logger = LoggerFactory.getLogger(DealerEmployeeDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public DealerUserCreateResponseModel createDealerUser(String userCode, DealerUserCreateRequestModel requestModel,
			Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("createDealerEmployee invoked..");
		}

		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		DealerUserCreateResponseModel responseModel = new DealerUserCreateResponseModel();
		boolean isSuccess = true;
		String msg = null;
		UserEntity userEntity = null;
		NativeQuery<?> query = null;
		List<UserRoleEntity> userRoleEntityList = null;
		Date currDate = new Date();
		List<DealerUserRoleRequestModel> userRoleList = requestModel.getRoleList();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				userEntity = mapper.map(requestModel, UserEntity.class, "dealerUserEntityMapId");

				userRoleEntityList = new ArrayList<>();
				for (DealerUserRoleRequestModel roleRequestModel : userRoleList) {
					UserRoleEntity roleEntity = mapper.map(roleRequestModel, UserRoleEntity.class,
							"dealerUserRoleEntityMapId");
					if (roleEntity != null) {
						roleEntity.setCreatedBy(userCode);
						roleEntity.setCreatedDate(currDate);
						userRoleEntityList.add(roleEntity);
					}
				}

				String sqlQuery = "Select * from ADM_USER where dlr_emp_id =:dlrEmpId";
				query = session.createNativeQuery(sqlQuery).addEntity(UserEntity.class);
				query.setParameter("dlrEmpId", requestModel.getDlrEmpId());
				UserEntity checkUser = (UserEntity) query.uniqueResult();
				if (checkUser == null) {
					if (requestModel.getPassword() != null) {
						String encoded = passwordEncoder.encode(requestModel.getPassword());
						userEntity.setPassword(encoded);
						userEntity.setCreatedBy(userCode);
						userEntity.setCreatedDate(currDate);
						String sqlQuery1 = "Select * from ADM_MST_USER_TYPE where USER_TYPE =:USER_TYPE";
						query = session.createNativeQuery(sqlQuery1).addEntity(UserTypeEntity.class);
						query.setParameter("USER_TYPE", WebConstants.DEALER);
						UserTypeEntity userTypeEntity = (UserTypeEntity) query.uniqueResult();
						if (userTypeEntity == null) {
							responseModel.setMsg("User Type Not Found. Contact Your System Administrator.");
							isSuccess = false;
						} else {
							userEntity.setUserType(userTypeEntity);
						}

						userEntity.setStatus(WebConstants.USER_STATUS);
						if (requestModel.getIsActive() != null && requestModel.getIsActive().equals(true)) {
							userEntity.setAccountNonExpired(true);
							userEntity.setAccountNonLocked(true);
							userEntity.setCredentialsNonExpired(true);
							userEntity.setEnabled(true);
							userEntity.setIsAccountValidated(true);
							userEntity.setIsActive(true);
						}
					}
					session.save(userEntity);

					if (userRoleEntityList != null) {
						for (UserRoleEntity role : userRoleEntityList) {
							UserRoleEntity setRole = new UserRoleEntity();
							setRole.setRoleId(role.getRoleId());
							setRole.setUserId(userEntity.getUserId());
							setRole.setIsActive(role.getIsActive());
							setRole.setCreatedBy(userCode);
							setRole.setCreatedDate(currDate);
							session.save(setRole);
						}
					}
					
					sqlQuery = "insert into ADM_USER_VS_PASSWORD values(:userCode, :password)";
					query = session.createNativeQuery(sqlQuery);
					query.setParameter("userCode", userEntity.getUsername());
					query.setParameter("password", requestModel.getPassword());
					int k = query.executeUpdate();

				} else {
					isSuccess = false;
					responseModel.setMsg("User already exist");
				}
//				sqlQuery = "Select * from ADM_USER_TYPE where USER_TYPE =:USER_TYPE";
//				query = session.createNativeQuery(sqlQuery).addEntity(UserTypeEntity.class);
//				query.setParameter("USER_TYPE", WebConstants.DEALER);
//				UserTypeEntity userTypeEntity = (UserTypeEntity) query.uniqueResult();
//				if (userTypeEntity == null) {
//					responseModel.setMsg("User Type Not Found. Contact Your System Administrator.");
//					isSuccess = false;
//				}

			}
			if (isSuccess) {
				responseModel.setMsg("Dealer User Created Successfully.");
				responseModel.setUserCode(userEntity.getUsername());
				responseModel.setUserId(userEntity.getUserId());
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
					responseModel.setMsg("Error While Creating Dealer User.");
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

}
