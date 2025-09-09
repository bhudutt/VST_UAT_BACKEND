package com.hitech.dms.web.dao.admin.role.create;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.dao.CommonDao;
import com.hitech.dms.web.entity.user.RoleEntity;
import com.hitech.dms.web.entity.user.RoleMenuEntity;
import com.hitech.dms.web.model.admin.role.create.response.AdminRoleCreateResponseModel;
import com.hitech.dms.web.model.admin.role.create.resquest.AdminRoleCreateRequestModel;
import com.hitech.dms.web.model.admin.role.create.resquest.AdminRoleFunctionRequestModel;
import com.hitech.dms.web.model.admin.role.create.resquest.RoleMasterRequestModel;
import com.hitech.dms.web.model.admin.role.create.resquest.RoleMenuRequestModel;
import org.apache.commons.lang3.StringUtils;
/**
 * @author vinay.gautam
 *
 */
@Repository
@Transactional
public class AdminRoleCreateDaoImpl implements AdminRoleCreateDao{
	
	private static final Logger logger = LoggerFactory.getLogger(AdminRoleCreateDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
	private CommonDao commonDao;
	
	@Override
	public AdminRoleCreateResponseModel createAdminRole(String userCode, AdminRoleFunctionRequestModel requestModel, Device device) {
	    if (logger.isDebugEnabled()) {
	        logger.debug("createAdminRole invoked..");
	    }
	    
	    Session session = null;
	    Transaction transaction = null;
	    Map<String, Object> mapData = null;
	    AdminRoleCreateResponseModel responseModel = new AdminRoleCreateResponseModel();
	    boolean isSuccess = true;
	    RoleEntity roleEntity = null;
	    Date currDate = new Date();
	    Long roleId = null;
	    
	    try {
	        session = sessionFactory.openSession();
	        transaction = session.beginTransaction();
	        
	        mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
	        if (mapData != null && mapData.get("SUCCESS") != null) {
	            
	            // Handle Role Master (Create or Update)
	            if (requestModel.getRole().getRoleId() == null) {
	                // CREATE NEW ROLE
	                roleId = createNewRole(session, requestModel.getRole(), userCode, currDate);
	                responseModel.setMsg("Role created and functionality assigned successfully.");
	            } else {
	                // UPDATE EXISTING ROLE
	                roleId = requestModel.getRole().getRoleId();
	                updateExistingRole(session, requestModel.getRole(), userCode, currDate);
	                responseModel.setMsg("Role updated and functionality modified successfully.");
	            }
	            
	            if (roleId != null) {
	                // Handle Role Menu Assignment
	                processRoleMenuAssignment(session, roleId, requestModel.getRoleMenu(), currDate);
	            } else {
	                responseModel.setMsg("Role processing failed - Role ID is null");
	                isSuccess = false;
	            }
	            
	        } else {
	            isSuccess = false;
	            responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	            responseModel.setMsg("User Not Found.");
	        }
	        
	        if (isSuccess) {
	            transaction.commit();
	        }
	        
	    } catch (Exception ex) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        isSuccess = false;
	        responseModel.setMsg("Error: " + ex.getMessage());
	        logger.error(this.getClass().getName(), ex);
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	        
	        if (isSuccess && roleId != null) {
	            mapData = fetchRoleCode(roleId);
	            if (mapData != null && mapData.get("SUCCESS") != null) {
	                responseModel.setRoleCode((String) mapData.get("roleCode"));
	                responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
	                responseModel.setRoleId(roleId);
	            }
	        } else {
	            responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
	            if (responseModel.getMsg() == null) {
	                responseModel.setMsg("Error in role processing");
	            }
	        }
	    }
	    return responseModel;
	}
	
	private void updateExistingRole(Session session, RoleMasterRequestModel roleRequest, String userCode, Date currDate) {
	    RoleEntity existingRole = session.get(RoleEntity.class, roleRequest.getRoleId());
	    if (existingRole != null) {
	        existingRole.setRoleName(roleRequest.getRoleName());
	        existingRole.setIsActive(roleRequest.getIsActive());
	        existingRole.setIsFor(roleRequest.getIsFor());
	        existingRole.setModifiedBy(userCode);
	        existingRole.setModifiedDate(currDate);
	        session.merge(existingRole);
	    }
	}


	private void processRoleMenuAssignment(Session session, Long roleId, List<RoleMenuRequestModel> roleMenuList, Date currDate) {
	    // First, deactivate all existing role menus for this role
	    Query deactivateQuery = session.createNativeQuery(
	        "UPDATE ADM_MENU_ROLE_DTL SET isActive = 'N', lastUpdatedOn = :currDate WHERE role_id = :roleId");
	    deactivateQuery.setParameter("currDate", currDate);
	    deactivateQuery.setParameter("roleId", roleId);
	    deactivateQuery.executeUpdate();
	    
	    // Process each menu item
	    for (RoleMenuRequestModel menu : roleMenuList) {
	        RoleMenuEntity roleMenuEntity;
	        
	        if (menu.getRoleMenuId() != null) {
	            // Update existing role menu
	            roleMenuEntity = session.get(RoleMenuEntity.class, menu.getRoleMenuId().longValue());
	            if (roleMenuEntity == null) {
	                // Create new if not found
	                roleMenuEntity = new RoleMenuEntity();
	                roleMenuEntity.setRoleId(roleId);
	                roleMenuEntity.setMenuId(menu.getMenuId());
	            }
	        } else {
	            // Check if role-menu combination already exists
	            Query checkQuery = session.createNativeQuery(
	                "SELECT role_menu_id FROM ADM_MENU_ROLE_DTL WHERE role_id = :roleId AND menu_id = :menuId");
	            checkQuery.setParameter("roleId", roleId);
	            checkQuery.setParameter("menuId", menu.getMenuId());
	            
	            @SuppressWarnings("unchecked")
	            List<BigInteger> existingIds = checkQuery.getResultList();
	            
	            if (!existingIds.isEmpty()) {
	                // Update existing
	                roleMenuEntity = session.get(RoleMenuEntity.class, existingIds.get(0).longValue());
	            } else {
	                // Create new
	                roleMenuEntity = new RoleMenuEntity();
	                roleMenuEntity.setRoleId(roleId);
	                roleMenuEntity.setMenuId(menu.getMenuId());
	            }
	        }
	        
	        roleMenuEntity.setIsActive(menu.getIsActive());
	        roleMenuEntity.setLastUpdatedOn(currDate);
	        session.saveOrUpdate(roleMenuEntity);
	    }
	}

	private String generateRoleCode(Long roleId) {
	    String roleCode = "RC";
	    String idStr = roleId.toString();
	    int length = idStr.length();
	    
	    if (length == 1) {
	        roleCode = roleCode + "000" + idStr;
	    } else if (length == 2) {
	        roleCode = roleCode + "00" + idStr;
	    } else if (length == 3) {
	        roleCode = roleCode + "0" + idStr;
	    } else {
	        roleCode = roleCode + idStr;
	    }
	    
	    return roleCode;
	}

	
	private Long createNewRole(Session session, RoleMasterRequestModel roleRequest, String userCode, Date currDate) {
	    RoleEntity roleEntity = mapper.map(roleRequest, RoleEntity.class, "roleEntity");
	    roleEntity.setCreatedBy(userCode);
	    roleEntity.setCreatedDate(currDate);
	    
	    // Generate role code after saving to get ID
	    Long roleId = (Long) session.save(roleEntity);
	    
	    String roleCode = generateRoleCode(roleId);
	    roleEntity.setRoleCode(roleCode);
	    session.merge(roleEntity);
	    
	    return roleId;
	}


	@SuppressWarnings("deprecation")
	@Override
	public Collection<AdminRoleTreeNode<AdminRoleUnit>> getAssignedFunctionalityToRole(
			AdminRoleCreateRequestModel requestModel, Device device) {
		Session session = null;
		NativeQuery<?> query = null;
		List<AdminRoleUnit> roles = new ArrayList<>();
		String sqlQuery = null;
		sqlQuery = "exec [SP_ADM_GET_TAB_MODULE] :user,:roleId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("user", null);
			query.setParameter("roleId", requestModel.getRoleId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			@SuppressWarnings("unchecked")
			List<AdminRoleMainTab> tabsList = (List<AdminRoleMainTab>) query.list();
			if (tabsList != null && !tabsList.isEmpty()) {
				for (Object object : tabsList) {
					Map<?, ?> row = (Map<?, ?>) object;
					roles.add(new AdminRoleUnit(((BigInteger)row.get("id")),((String)row.get("functionality")),null));
					sqlQuery = "exec [SP_ADM_GET_ASSIGNED_ROLE] :parantId, :roleId";
					query = session.createSQLQuery(sqlQuery);
					query.setParameter("parantId", row.get("id"));
					query.setParameter("roleId", requestModel.getRoleId());
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					List<?> data = query.list();
					if (data != null && !data.isEmpty()) {
						for (Object obj : data) {
							Map<?, ?> row1 = (Map<?, ?>) obj;
								if(row1.get("menuid")!=null){
									if (requestModel.getRoleId() == null) {
										roles.add(new AdminRoleUnit(((BigInteger)row1.get("menuid")),(String)row1.get("functionality"),((BigInteger)row1.get("modulid"))));
									}else {
										roles.add(new AdminRoleUnit((BigInteger)row1.get("menuid"),(String)row1.get("functionality"),(BigInteger)row1.get("modulid"),(Character) row1.get("RoleMenu_IsActive")));
									}
									sqlQuery = "exec [SP_ADM_GET_ASSIGNED_ROLE] :parantId, :roleId";
									query = session.createSQLQuery(sqlQuery);
									query.setParameter("parantId", row1.get("menuid"));
									query.setParameter("roleId", requestModel.getRoleId());
									query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
									List<?> data1 = query.list();
									if (data1 != null && !data1.isEmpty()) {
										for (Object obj1 : data1) {
											Map<?, ?> row2 = (Map<?, ?>) obj1;
											if(row2.get("menuid")!=null){
												roles.add(new AdminRoleUnit(((BigInteger)row2.get("menuid")),(String)row2.get("functionality"),((BigInteger)row2.get("modulid"))));
											}else {
												roles.add(new AdminRoleUnit((BigInteger)row2.get("menuid"),(String)row2.get("functionality"),(BigInteger)row2.get("modulid"),(Character) row2.get("RoleMenu_IsActive")));
											}
										}
									}
								}
						}
					}
				}
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

		AdminRoleHierarchy<AdminRoleUnit, BigInteger> service = new AdminRoleHierarchy<>(roles);
		
		Collection<AdminRoleTreeNode<AdminRoleUnit>> trees = service.getRoots().stream()
                .map(service::getTree)
                .collect(Collectors.toSet());
		
		return trees;
	}
	
	
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchRoleCode(Long roleId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select role_code from ADM_MENU_ROLE_HDR (nolock) role where role.role_id =:roleId";
		mapData.put("ERROR", "Role Details Not Found");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("roleId", roleId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String roleCode = null;
				for (Object object : data) {
					Map row = (Map) object;
					roleCode = (String) row.get("role_code");
				}
				mapData.put("roleCode", roleCode);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ROLE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ROLE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return mapData;
	}

}
