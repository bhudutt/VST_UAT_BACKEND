/**
 * 
 */
package com.hitech.dms.web.dao.admin.create.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.core.util.PasswordDecryptor;
import org.dozer.DozerBeanMapper;
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
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.RandomPasswordGenerator;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.dao.CommonDao;
import com.hitech.dms.web.entity.user.HoUserVsOrgEntity;
import com.hitech.dms.web.entity.user.UserEntity;
import com.hitech.dms.web.entity.user.UserHoEntity;
import com.hitech.dms.web.entity.user.UserRoleEntity;
import com.hitech.dms.web.entity.user.UserTypeEntity;
import com.hitech.dms.web.model.admin.create.request.HoUserCreateRequestModel;
import com.hitech.dms.web.model.admin.create.request.HoUserRoleRequestModel;
import com.hitech.dms.web.model.admin.create.request.HoUserVsOrgRequestModel;
import com.hitech.dms.web.model.admin.create.response.HoUserCreateResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class HoUserCreateDaoImpl implements HoUserCreateDao {
	private static final Logger logger = LoggerFactory.getLogger(HoUserCreateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public HoUserCreateResponseModel updateOneTimePasswordForAllUsers(String userCode,
			HoUserCreateRequestModel requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("updateOneTimePasswordForAllUsers invoked.." + userCode);
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		HoUserCreateResponseModel responseModel = new HoUserCreateResponseModel();
		boolean isSuccess = true;
		String sqlQuery = "select * from ADM_USER (nolock)";
		if (requestModel != null && requestModel.getUserCode() != null && !requestModel.getUserCode().equals("")) {
			sqlQuery = sqlQuery + " where UserCode=:userCode";
		}
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			// current date
//			Date currDate = new Date();
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");

				query = session.createNativeQuery(sqlQuery);
				if (requestModel != null && requestModel.getUserCode() != null
						&& !requestModel.getUserCode().equals("")) {
					query.setParameter("userCode", requestModel.getUserCode());
				}
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						Map row = (Map) object;
						String decoded = requestModel.getPassword();
						String encoded = null;
						if (requestModel != null && requestModel.getUserCode() != null
								&& !requestModel.getUserCode().equals("") && requestModel.getPassword() != null
								&& !requestModel.getPassword().equals("")) {
							encoded = passwordEncoder.encode(decoded);
						} else {
							int len = 10;
							int randNumOrigin = 48, randNumBound = 122;
							try {
								decoded = RandomPasswordGenerator.generateRandomPassword(len, randNumOrigin,
										randNumBound);
								encoded = passwordEncoder.encode(decoded);
							} catch (Exception ex) {
								logger.error(this.getClass().getName(), ex);
							}
							if (encoded == null) {
								decoded = (String) row.get("UserCode") + "@123";
								encoded = passwordEncoder.encode(decoded);
							}
						}
						String username = (String) row.get("UserCode");

						sqlQuery = "Update ADM_USER Set Password =:password, LASTPASSWORDRESETDATE =GetDate(), ModifiedBy=:modifiedBy, ModifiedDate=GetDate() where UserCode=:userCode";
						query = session.createNativeQuery(sqlQuery);
						query.setParameter("userCode", username);
						query.setParameter("password", encoded);
						query.setParameter("modifiedBy", userCode);
						int k = query.executeUpdate();
						if (k > 0) {
							sqlQuery = "exec [SP_ADM_RANDOM_PASSWORD_INSERT] :userCode, :password";
							query = session.createNativeQuery(sqlQuery);
							query.setParameter("userCode", username);
							query.setParameter("password", decoded);
							query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
							data = query.list();
							if (data != null && !data.isEmpty()) {
								for (Object obj : data) {
									Map row1 = (Map) obj;
									logger.info(username + " : " + (String) row1.get("status"));
								}
							}
						}
					}
				}
			} else {
				// user not found
				responseModel.setMsg("User Not Found.");
				isSuccess = false;
			}
			if (isSuccess) {
				transaction.commit();
				responseModel.setMsg("User's Password Updated Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (!isSuccess) {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Updating User's Password.");
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public HoUserCreateResponseModel createUpdateHoUser(String userCode, HoUserCreateRequestModel requestModel,
			Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("createHoUser invoked.." + userCode);
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		HoUserCreateResponseModel responseModel = new HoUserCreateResponseModel();
		UserHoEntity hoUserEntity = null;
		// RoleUserMapId
		List<UserRoleEntity> userRoleList = null;
		List<HoUserRoleRequestModel> hoUserRoleList = requestModel.getHoUserRoleList();
		// HoUserVsOrgMapId
		List<HoUserVsOrgEntity> hoUserVsOrgEntityList = null;
		List<HoUserVsOrgRequestModel> hoUserVsOrgList = requestModel.getHoUserVsOrgList();
		UserEntity userEntity = null;
		boolean isSuccess = true;
		boolean isUpdate = false;
		String sqlQueryEmail = "select * from adm_ho_user where Emp_Mail=:emailId and IsActive='Y'";
		String sqlQuery = "select * from ADM_HO_USER where employee_code =:employeeCode";
		try {
			logger.debug(requestModel != null ? requestModel.toString() : null);
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			//System.out.println("mapDate"+mapData.toString());
			String hoCode = null;
			BigInteger hoUserId = null;
			String loginUserCode = null;
			BigInteger loginUserId = null;
			// current date
			Date currDate = new Date();
			
			
				if (mapData != null && mapData.get("SUCCESS") != null ) {
					userId = (BigInteger) mapData.get("userId");

					hoUserEntity = mapper.map(requestModel,UserHoEntity.class, "HoUserMapId");
					// map filed role org entity
					hoUserVsOrgEntityList = new ArrayList<HoUserVsOrgEntity>();
					for (HoUserVsOrgRequestModel orgRequestModel : hoUserVsOrgList) {
						HoUserVsOrgEntity orgEntity = mapper.map(orgRequestModel, HoUserVsOrgEntity.class,
								"HoUserVsOrgMapId");
						hoUserVsOrgEntityList.add(orgEntity);
					}
					hoUserVsOrgEntityList = hoUserVsOrgEntityList.stream().distinct().collect(Collectors.toList());
					userRoleList = new ArrayList<UserRoleEntity>();
					for (HoUserRoleRequestModel roleRequestModel : hoUserRoleList) {
						UserRoleEntity roleEntity = mapper.map(roleRequestModel, UserRoleEntity.class, "RoleUserMapId");
						userRoleList.add(roleEntity);
					}
					query = session.createNativeQuery(sqlQuery).addEntity(UserHoEntity.class);
					query.setParameter("employeeCode", requestModel.getEmployeeCode());
					// Validating HO USER Existence in HO table
					UserHoEntity dbUserHOEntity = (UserHoEntity) query.uniqueResult();
				
					if (dbUserHOEntity != null) {
						boolean okFlag = false;

						String dbEmpMail = dbUserHOEntity.getEmpMail();
						String reqEmpMail = hoUserEntity.getEmpMail();
						if (dbEmpMail.equalsIgnoreCase(reqEmpMail)) {
						    okFlag = true;
						} else {
						    query = session.createNativeQuery(sqlQueryEmail).addEntity(UserHoEntity.class);
						    query.setParameter("emailId", hoUserEntity.getEmpMail());
						    // Validating HO USER Existence in HO table
						    UserHoEntity dbUserHOEntityOnEmail = (UserHoEntity) query.uniqueResult();
						    if (dbUserHOEntityOnEmail != null) {
						        // user not found
						        responseModel.setMsg("This email Id is already in use");
						        isSuccess = false;
						        okFlag = false;
						    } else {
						        okFlag = true;
						    }
						}

						if (okFlag) {
						    hoCode = dbUserHOEntity.getEmployeeCode();
						    hoUserId = dbUserHOEntity.getHoUserId();
						    // validate dbUserHOEntity is equal to UI UserHo
						    if (requestModel.getHoUserId() != null && dbUserHOEntity.getHoUserId().compareTo(requestModel.getHoUserId()) == 0) {
						        // update existed Ho User
						        int updateUserStatus = updateHoUser(requestModel, userCode, hoUserId);
						        if (updateUserStatus >= 1) {
						            // User details updated successfully
						        } else {
						            responseModel.setMsg("Ho Users Detail Not Updated ");
						            isSuccess = false;
						        }

						        // CORRECTED TERRITORY MANAGER VALIDATION LOGIC
						        boolean hasViolation = false;
						        boolean checkForTMRole = false;
						        
						        // Check if user has Territory Manager role (roleId = 7) and it's active
						        Optional<UserRoleEntity> tmRoleResult = userRoleList.stream()
						                .filter(role -> role.getRoleId().equals(BigInteger.valueOf(7)) && Boolean.TRUE.equals(role.getIsActive()))
						                .findFirst();

						        if (tmRoleResult.isPresent()) {
						            checkForTMRole = true;
						        } else {
						            System.out.println("No active Territory Manager role (roleId 7) found for this user.");
						            checkForTMRole = false;
						        }

						        // If user is being assigned Territory Manager role, validate business rule
						        if (checkForTMRole) {
						        	List<HoUserVsOrgEntity> activeEntities = hoUserVsOrgEntityList.stream()
						        		    .filter(entity -> entity.getIsActive() != null && entity.getIsActive())
						        		    .collect(Collectors.toList());
						            for (HoUserVsOrgEntity hoUserVsOrgEntity : activeEntities) {
						                // CORRECTED SQL QUERY - Fixed syntax errors and added proper count logic
						                String sqlQueryForTMValidation = "SELECT COUNT(DISTINCT b.ho_usr_id) as tm_count " +
						                        "FROM ADM_HO_USER_ORGHIER a " +
						                        "INNER JOIN ADM_USER b ON a.ho_usr_id = b.ho_usr_id " +
						                        "INNER JOIN ADM_MENU_ROLE_USER_HDR c ON c.user_id = b.user_id " +
						                        "WHERE a.org_hierarchy_id = :orgId " +
						                        "AND c.role_id = 7 " +
						                        "AND c.is_active = 'Y' " +
						                        "AND b.IsActive = 'Y' " +
						                        "AND a.IsActive = 'Y' " +
						                        "AND b.ho_usr_id != :currentUserId"; // Exclude current user being updated

						                query = session.createNativeQuery(sqlQueryForTMValidation);
						                System.out.println("Validating org_hierarchy_id: " + hoUserVsOrgEntity.getOrgHierarchyId());
						                query.setParameter("orgId", hoUserVsOrgEntity.getOrgHierarchyId());
						                query.setParameter("currentUserId", requestModel.getHoUserId()); // Exclude current user
						                
						                Object result = query.uniqueResult();
						                Integer existingTMCount = 0;
						                if (result != null) {
						                    existingTMCount = ((Number) result).intValue();
						                }
						                
						                System.out.println("Existing TM count for org_hierarchy_id " + 
						                                 hoUserVsOrgEntity.getOrgHierarchyId() + ": " + existingTMCount);

						                // Business rule: Maximum 1 active Territory Managers per org_hierarchy
						                if (existingTMCount >= 1) {
						                    hasViolation = true;
						                    break; // Exit loop on first violation found
						                }
						            }
						        }

						        // ENFORCED BUSINESS RULE VALIDATION
						        if (hasViolation) {
						            responseModel.setMsg("Cannot assign Territory Manager role. Maximum 1 Territory Managers allowed per organizational hierarchy.");
						            isSuccess = false;
						            return responseModel;
						        } else {
						            // Proceed with normal flow - no violation found
						            
						            // get existing org heir Ids
						            sqlQuery = "Select * from ADM_HO_USER_ORGHIER (nolock) where ho_usr_id = :houserId";
						            query = session.createNativeQuery(sqlQuery).addEntity(HoUserVsOrgEntity.class);
						            query.setParameter("houserId", requestModel.getHoUserId());
						            List<HoUserVsOrgEntity> hoUserVsOrgDBList = query.list();
						            
						            if (hoUserVsOrgDBList != null && !hoUserVsOrgDBList.isEmpty()) {
						                List<HoUserVsOrgEntity> filteredOrgList = filterDistinctList(hoUserVsOrgDBList,
						                        hoUserVsOrgEntityList, userCode, currDate);
						                for (HoUserVsOrgEntity hoUserVsOrgEntity : filteredOrgList) {
						                    hoUserVsOrgEntity.setHoUserId(dbUserHOEntity.getHoUserId());
						                    session.merge(hoUserVsOrgEntity);
						                }
						            } else {
						                for (HoUserVsOrgEntity hoUserVsOrgEntity : hoUserVsOrgEntityList) {
						                    hoUserVsOrgEntity.setHoUserId(dbUserHOEntity.getHoUserId());
						                    if (hoUserVsOrgEntity.getIsActive() == null) {
						                        hoUserVsOrgEntity.setIsActive(true);
						                    }
						                    hoUserVsOrgEntity.setCreatedBy(userCode);
						                    hoUserVsOrgEntity.setCreatedDate(currDate);
						                    session.saveOrUpdate(hoUserVsOrgEntity);
						                }
						            }

						            // update User
						            sqlQuery = "Select * from ADM_USER (nolock) where ho_usr_id = :houserId";
						            query = session.createNativeQuery(sqlQuery).addEntity(UserEntity.class);
						            query.setParameter("houserId", requestModel.getHoUserId());
						            userEntity = (UserEntity) query.uniqueResult();
						            
						            if (userEntity != null) {
						                // update existing user
						                if (requestModel.getPassword() != null
						                        && !requestModel.getPassword().equals(userEntity.getPassword())) {
						                    String encoded = passwordEncoder.encode(requestModel.getPassword());
						                    userEntity.setPassword(encoded);
						                    userEntity.setLastPasswordResetDate(currDate);
						                }
						                if (requestModel.getIsActive()) {
						                    userEntity.setIsActive(true);
						                } else {
						                    userEntity.setIsActive(false);
						                }
						                userEntity.setModifiedBy(userCode);
						                userEntity.setModifiedDate(currDate);
						                session.merge(userEntity);
						                loginUserCode = userEntity.getUsername();
						                loginUserId = userEntity.getUserId();

						                // added by Vivek Kumar Gupta
						                try {
						                    sqlQuery = "if exists(SELECT * from ADM_USER_VS_PASSWORD where UserCode=:userCode) "
						                            + " BEGIN "
						                            + " update ADM_USER_VS_PASSWORD set Password=:password where UserCode=:userCode "
						                            + " End " + " else " + " begin "
						                            + " insert into ADM_USER_VS_PASSWORD values(:userCode, :password)"
						                            + " end ";
						                    query = session.createNativeQuery(sqlQuery);
						                    query.setParameter("userCode", userEntity.getUsername());
						                    query.setParameter("password", requestModel.getPassword());
						                    int k = query.executeUpdate();
						                } catch (Exception exp) {
						                    logger.error(this.getClass().getName(), exp);
						                }
						                // Ended code
						            } else {
						                // create new user
						                sqlQuery = "Select * from ADM_MST_USER_TYPE where USER_TYPE =:USER_TYPE";
						                query = session.createNativeQuery(sqlQuery).addEntity(UserTypeEntity.class);
						                query.setParameter("USER_TYPE", WebConstants.ADMIN);
						                UserTypeEntity userTypeEntity = (UserTypeEntity) query.uniqueResult();
						                if (userTypeEntity == null) {
						                    // user type not found
						                    responseModel.setMsg("User Type Not Found. Contact Your System Administrator.");
						                    isSuccess = false;
						                }
						                if (isSuccess) {
						                    userEntity = new UserEntity();
						                    userEntity.setUserType(userTypeEntity);
						                    userEntity.setUsername(requestModel.getUserCode().trim());
						                    if (requestModel.getPassword() == null || requestModel.getPassword().equals("")) {
						                        requestModel.setPassword(requestModel.getUserCode() + "@123#");
						                    }
						                    logger.error(this.getClass().getName(), " While Updating : " + requestModel.getPassword());
						                    String encoded = passwordEncoder.encode(requestModel.getPassword());
						                    userEntity.setPassword(encoded);
						                    userEntity.setLastPasswordResetDate(currDate);
						                    userEntity.setStatus(WebConstants.USER_STATUS);
						                    userEntity.setHoUserId(dbUserHOEntity.getHoUserId());
						                    if (requestModel.getIsActive()) {
						                        userEntity.setAccountNonExpired(true);
						                        userEntity.setAccountNonLocked(true);
						                        userEntity.setCredentialsNonExpired(true);
						                        userEntity.setEnabled(true);
						                        userEntity.setIsAccountValidated(true);
						                        userEntity.setIsActive(true);
						                    }
						                    userEntity.setCreatedBy(userCode);
						                    userEntity.setCreatedDate(currDate);
						                    session.save(userEntity);
						                    loginUserCode = userEntity.getUsername();
						                    loginUserId = userEntity.getUserId();

						                    try {
						                        sqlQuery = "if exists(SELECT * from ADM_USER_VS_PASSWORD where UserCode=:userCode) "
						                                + " BEGIN "
						                                + " update ADM_USER_VS_PASSWORD set Password=:password where UserCode=:userCode "
						                                + " End " + " else " + " begin "
						                                + " insert into ADM_USER_VS_PASSWORD values(:userCode, :password)"
						                                + " end ";
						                        query = session.createNativeQuery(sqlQuery);
						                        query.setParameter("userCode", userEntity.getUsername());
						                        query.setParameter("password", requestModel.getPassword());
						                        int k = query.executeUpdate();
						                    } catch (Exception exp) {
						                        logger.error(this.getClass().getName(), exp);
						                    }
						                }
						            }
						            
						            if (isSuccess) {
						                // save user roles
						                sqlQuery = "Select * from ADM_MENU_ROLE_USER_HDR (nolock) where user_id = :userId";
						                query = session.createNativeQuery(sqlQuery).addEntity(UserRoleEntity.class);
						                query.setParameter("userId", userEntity.getUserId());
						                List<UserRoleEntity> userRoleDBList = query.list();
						                
						                if (userRoleDBList != null && !userRoleDBList.isEmpty()) {
						                    List<UserRoleEntity> filteredRoleList = filterDistinctRoleList(userRoleDBList,
						                            userRoleList, userCode, currDate);

						                    // ram added
						                    Integer roleUpdateStatus = updateHoUserRoleList(filteredRoleList, userEntity.getUserId(), userCode);
						                    if (roleUpdateStatus >= 1) {
						                        // Roles updated successfully
						                    } else {
						                        responseModel.setMsg("Users Role Not Updated Properly ");
						                        isSuccess = false;
						                    }
						                } else {
						                    // inserting new roles
						                    for (UserRoleEntity userRoleEntity : userRoleList) {
						                        userRoleEntity.setUserId(userEntity.getUserId());
						                        userRoleEntity.setCreatedBy(userCode);
						                        userRoleEntity.setCreatedDate(currDate);
						                        session.saveOrUpdate(userRoleEntity);
						                    }
						                }
						                isUpdate = true;
						                // End Of Ho User updated
						            }
						        }
						    } else {
						        // Ho User Id Altered
						        responseModel.setMsg("Some Issue Found In Employee Id.");
						        isSuccess = false;
						    }
						}
					} else {query = session.createNativeQuery(sqlQueryEmail).addEntity(UserHoEntity.class);
					query.setParameter("emailId", hoUserEntity.getEmpMail());
					// Validating HO USER Existence in HO table
					UserHoEntity dbUserHOEntityOnEmail = (UserHoEntity) query.uniqueResult();
					if (dbUserHOEntityOnEmail != null) {
					    // user email already exists
					    responseModel.setMsg("This email Id is already in use");
					    isSuccess = false;
					} else {
					    // CORRECTED TERRITORY MANAGER VALIDATION FOR NEW USER CREATION
					    boolean hasViolation = false;
					    boolean checkForTMRole = false;
					    
					    // Check if new user is being assigned Territory Manager role (roleId = 7)
					    Optional<UserRoleEntity> tmRoleResult = userRoleList.stream()
					            .filter(role -> role.getRoleId().equals(BigInteger.valueOf(7)) && Boolean.TRUE.equals(role.getIsActive()))
					            .findFirst();

					    if (tmRoleResult.isPresent()) {
					        checkForTMRole = true;
					        System.out.println("New user is being assigned Territory Manager role. Validating business rules...");
					    } else {
					        System.out.println("No active Territory Manager role (roleId 7) found for new user.");
					        checkForTMRole = false;
					    }

					    // If new user is being assigned Territory Manager role, validate business rule
					    if (checkForTMRole) {
					    	List<HoUserVsOrgEntity> activeEntitiesNew = hoUserVsOrgEntityList.stream()
				        		    .filter(entity -> entity.getIsActive() != null && entity.getIsActive())
				        		    .collect(Collectors.toList());
					        for (HoUserVsOrgEntity hoUserVsOrgEntity : activeEntitiesNew) {
					            // CORRECTED SQL QUERY FOR NEW USER VALIDATION
					            // Count existing active Territory Managers for this org_hierarchy
					            String sqlQueryForTMValidation = "SELECT COUNT(DISTINCT b.ho_usr_id) as tm_count " +
					                    "FROM ADM_HO_USER_ORGHIER a " +
					                    "INNER JOIN ADM_USER b ON a.ho_usr_id = b.ho_usr_id " +
					                    "INNER JOIN ADM_MENU_ROLE_USER_HDR c ON c.user_id = b.user_id " +
					                    "WHERE a.org_hierarchy_id = :orgId " +
					                    "AND c.role_id = 7 " +
					                    "AND c.is_active = 'Y' " +
					                    "AND b.IsActive = 'Y' " +
					                    "AND a.IsActive = 'Y'";
					            // Note: No need to exclude current user since this is new user creation

					            query = session.createNativeQuery(sqlQueryForTMValidation);
					            System.out.println("Validating TM count for org_hierarchy_id: " + hoUserVsOrgEntity.getOrgHierarchyId());
					            query.setParameter("orgId", hoUserVsOrgEntity.getOrgHierarchyId());
					            
					            Object result = query.uniqueResult();
					            Integer existingTMCount = 0;
					            if (result != null) {
					                existingTMCount = ((Number) result).intValue();
					            }
					            
					            System.out.println("Existing TM count for org_hierarchy_id " + 
					                             hoUserVsOrgEntity.getOrgHierarchyId() + ": " + existingTMCount);

					            // Business rule: Maximum 1 active Territory Managers per org_hierarchy
					            if (existingTMCount >= 1) {
					                System.out.println("VIOLATION: org_hierarchy_id " + hoUserVsOrgEntity.getOrgHierarchyId() + 
					                                 " already has " + existingTMCount + " Territory Managers");
					                hasViolation = true;
					                break; // Exit loop on first violation found
					            }
					        }
					    }

					    // ENFORCED BUSINESS RULE VALIDATION FOR NEW USER
					    if (hasViolation) {
					        responseModel.setMsg("Cannot create user with Territory Manager role. One or more organizational hierarchies already have the maximum limit of 1 Territory Managers.");
					        isSuccess = false;
					        return responseModel;
					    } else {
					        // Proceed with new user creation - no violation found
					        System.out.println("TM validation passed. Proceeding with user creation...");
					        
					        // create new Ho Employee / User
					        hoUserEntity.setCreatedBy(userCode);
					        hoUserEntity.setCreatedDate(currDate);
					        if (requestModel.getIsActive() == null) {
					            hoUserEntity.setIsActive(true);
					        }
					        session.save(hoUserEntity);
					        hoCode = hoUserEntity.getEmployeeCode();
					        hoUserId = hoUserEntity.getHoUserId();

					        // inserting into org
					        for (HoUserVsOrgEntity hoUserVsOrgEntity : hoUserVsOrgEntityList) {
					            hoUserVsOrgEntity.setHoUserId(hoUserId);
					            if (hoUserVsOrgEntity.getIsActive() == null) {
					                hoUserVsOrgEntity.setIsActive(true);
					            }
					            hoUserVsOrgEntity.setCreatedBy(userCode);
					            hoUserVsOrgEntity.setCreatedDate(currDate);
					            session.saveOrUpdate(hoUserVsOrgEntity);
					        }

					        // creating new User
					        sqlQuery = "Select * from ADM_MST_USER_TYPE where USER_TYPE =:USER_TYPE";
					        query = session.createNativeQuery(sqlQuery).addEntity(UserTypeEntity.class);
					        
					        if (hoUserEntity.getUserForId() != null && hoUserEntity.getUserForId() > 0) {
					            query.setParameter("USER_TYPE", WebConstants.DIGITAL);
					        } else {
					            query.setParameter("USER_TYPE", WebConstants.ADMIN);
					        }
					        
					        UserTypeEntity userTypeEntity = (UserTypeEntity) query.uniqueResult();
					        if (userTypeEntity == null) {
					            // user type not found
					            responseModel.setMsg("User Type Not Found. Contact Your System Administrator.");
					            isSuccess = false;
					        }
					        
					        if (isSuccess) {
					            userEntity = new UserEntity();
					            userEntity.setUserType(userTypeEntity);
					            userEntity.setUsername(requestModel.getUserCode().trim());
					            if (requestModel.getPassword() == null || requestModel.getPassword().equals("")) {
					                requestModel.setPassword(requestModel.getUserCode() + "@123#");
					            }
					            logger.error(this.getClass().getName(), " While Creating : " + requestModel.getPassword());
					            String encoded = passwordEncoder.encode(requestModel.getPassword());
					            userEntity.setPassword(encoded);
					            userEntity.setLastPasswordResetDate(currDate);
					            userEntity.setStatus(WebConstants.USER_STATUS);
					            userEntity.setHoUserId(hoUserEntity.getHoUserId());
					            if (requestModel.getIsActive()) {
					                userEntity.setAccountNonExpired(true);
					                userEntity.setAccountNonLocked(true);
					                userEntity.setCredentialsNonExpired(true);
					                userEntity.setEnabled(true);
					                userEntity.setIsAccountValidated(true);
					                userEntity.setIsActive(true);
					            }
					            userEntity.setCreatedBy(userCode);
					            userEntity.setCreatedDate(currDate);
					            session.save(userEntity);
					            loginUserCode = userEntity.getUsername();
					            loginUserId = userEntity.getUserId();

					            // inserting new roles
					            for (UserRoleEntity userRoleEntity : userRoleList) {
					                userRoleEntity.setUserId(userEntity.getUserId());
					                if (userRoleEntity.getIsActive() == null) {
					                    userRoleEntity.setIsActive(true);
					                }
					                userRoleEntity.setCreatedBy(userCode);
					                userRoleEntity.setCreatedDate(currDate);
					                session.saveOrUpdate(userRoleEntity);
					            }
					            
					            // Insert into password table
					            try {
					                sqlQuery = "insert into ADM_USER_VS_PASSWORD values(:userCode, :password)";
					                query = session.createNativeQuery(sqlQuery);
					                query.setParameter("userCode", userEntity.getUsername());
					                query.setParameter("password", requestModel.getPassword());
					                int k = query.executeUpdate();
					                System.out.println("Password record inserted successfully");
					            } catch (Exception exp) {
					                logger.error(this.getClass().getName() + " - Error inserting password", exp);
					                // Consider if this should be a fatal error or just logged
					            }
					        }
					    }
					}
					}

				} else {
					// user not found
					responseModel.setMsg("User Not Found.");
					isSuccess = false;
				}
			
			
			
			
			if (isSuccess) {
				transaction.commit();
				if (isUpdate) {
					responseModel.setMsg("HO Employee Updated Successfully.");
				} else {
					responseModel.setMsg("HO Employee Created Successfully.");
				}
				responseModel.setEmployeeCode(hoCode);
				responseModel.setHoUserId(hoUserId);
				responseModel.setUserCode(loginUserCode);
				responseModel.setUserId(loginUserId);
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (!isSuccess) {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Creating/Updating HO Employee."+this.getClass().getName());
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}

	private Integer updateHoUserRoleList(List<UserRoleEntity> filteredRoleList, BigInteger userId, String userCode) {

		Session session=null;
		Transaction tx = null;
		int updateStatus=0;
		try
		{
			
			session = sessionFactory.openSession();
		    tx = session.beginTransaction();
		    for(UserRoleEntity entity:filteredRoleList)
		    {
		    	//System.out.println("entity is "+entity);
		    	entity.setUserId(userId);
		    	if(entity.getIsActive()) {
		            entity.setIsActive(true);

		    	}
		    	else
		    	{
		            entity.setIsActive(false);

		    	}
	            session.merge(entity);

		    }
            tx.commit();
		    updateStatus=1;
			
		}catch (Exception e) {
		    if (tx != null) {
		        tx.rollback();
		    }
		    e.printStackTrace();
		} finally {
		    if (session != null) {
		        session.close();
		    }
		}
		return updateStatus;
	}

	private int updateHoUser(HoUserCreateRequestModel requestModel, String userCode,BigInteger hoUserId) {
		
		//System.out.println("UserEntity  11  "+requestModel.getDepartmentId()+" "+requestModel.getHoDesignationId()+" "+requestModel.getHoDesignationLevelId());
		Session session=null;
		Transaction tx = null;
		int updateStatus=0;
		try {
		    session = sessionFactory.openSession();
		    tx = session.beginTransaction();
		    BigInteger hoUsrId=hoUserId;

		    UserHoEntity existingUser = session.get(UserHoEntity.class,hoUsrId);

		    if (existingUser != null) {
		        existingUser.setDepartmentId(requestModel.getDepartmentId());
		        existingUser.setEmpContactNo(requestModel.getEmpContactNo());
		        existingUser.setEmployeeName(requestModel.getEmployeeName());
		        existingUser.setEmpMail(requestModel.getEmpMail());
		        existingUser.setHoDesignationId(requestModel.getHoDesignationId());
		        existingUser.setHoDesignationLevelId(requestModel.getHoDesignationLevelId());
		        existingUser.setIsActive(requestModel.getIsActive());
		        existingUser.setPFirstName(requestModel.getPFirstName());
		        existingUser.setPLastName(requestModel.getPLastName());
		        existingUser.setPMiddleName(requestModel.getPMiddleName());
		        existingUser.setCreatedBy(userCode);
		        existingUser.setCreatedDate(new Date());
		        existingUser.setModifiedBy(userCode);
		        existingUser.setModifiedDate(null);
		        
		       // System.out.println("Existing user "+existingUser.toString());

		        tx.commit();
		        updateStatus = 1;
		       // System.out.println("User updated successfully.");
		    } else {
		       // System.out.println("User with ID " + hoUserId + " not found.");
		    }
		} catch (Exception e) {
		    if (tx != null) {
		        tx.rollback();
		    }
		    e.printStackTrace();
		} finally {
		    if (session != null) {
		        session.close();
		    }
		}
		
		return updateStatus;
	}

	public static List<HoUserVsOrgEntity> filterDistinctList(List<HoUserVsOrgEntity> hoUserVsOrgDBList,
			List<HoUserVsOrgEntity> hoUserVsOrgList, String userCode, Date currDate) {
		List<HoUserVsOrgEntity> listOneList = new ArrayList<HoUserVsOrgEntity>();
		for (HoUserVsOrgEntity orgEntity : hoUserVsOrgList) {
			for (HoUserVsOrgEntity orgDBEntity : hoUserVsOrgDBList) {
				if (orgDBEntity.getOrgHierarchyId() != null && orgEntity.getOrgHierarchyId() != null
						&& orgDBEntity.getOrgHierarchyId().compareTo(orgEntity.getOrgHierarchyId()) == 0) {
					orgEntity.setHoUserId(orgDBEntity.getHoUserId());
					orgEntity.setHoUserOrgId(orgDBEntity.getHoUserOrgId());
					orgEntity.setModifiedBy(userCode);
					orgEntity.setModifiedDate(currDate);
				} else {
					orgEntity.setCreatedBy(userCode);
					orgEntity.setCreatedDate(currDate);
				}
			}
			listOneList.add(orgEntity);
		}
		// We return the collected list.
		return listOneList;
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

	public static List<UserRoleEntity> sharedListViaStream(List<UserRoleEntity> userRoleDBList,
			List<UserRoleEntity> userRoleList) {
		// We create a stream of elements from the first list.
		List<UserRoleEntity> listOneList = userRoleDBList.stream()
				// We select any elements such that in the stream of elements from the second
				// list
				.filter(two -> userRoleList.stream()
						// there is an element that has the same part as this element,
						.anyMatch(one -> one.getRoleId().compareTo(two.getRoleId()) == 0))
				// and collect all matching elements from the first list into a new list.
				.collect(Collectors.toList());
		// We return the collected list.
		return listOneList;
	}

	
}
