package com.hitech.dms.web.dao.admin.role.edit;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.dao.admin.role.create.AdminRoleCreateDaoImpl;
import com.hitech.dms.web.dao.admin.role.create.AdminRoleHierarchy;
import com.hitech.dms.web.dao.admin.role.create.AdminRoleMainTab;
import com.hitech.dms.web.dao.admin.role.create.AdminRoleTreeNode;
import com.hitech.dms.web.dao.admin.role.create.AdminRoleUnit;
import com.hitech.dms.web.dao.common.dao.CommonDao;
import com.hitech.dms.web.entity.user.RoleEntity;
import com.hitech.dms.web.entity.user.RoleMenuEntity;
import com.hitech.dms.web.model.admin.role.create.resquest.AdminRoleCreateRequestModel;

/**
 * @author vinay.gautam
 *
 */
@Repository
public class AdminRoleEditDaoImpl implements AdminRoleEditDao{
	
	
	private static final Logger logger = LoggerFactory.getLogger(AdminRoleEditDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private AdminRoleCreateDaoImpl adminRoleCreateDaoImpl;

	@Override
	public HashMap<String, Object> getRolesByRoleId(BigInteger roleId, Device device) {
		Session session = null;
		NativeQuery<?> query = null;
		System.out.println("dgsdgdsgfdsaaaasssaaaassa");
		List<AdminRoleUnit> roles = new LinkedList<>();
		String sqlQuery = null;
		AdminRoleCreateRequestModel req = new AdminRoleCreateRequestModel();
		Long id = roleId.longValue();
		req.setRoleId(id);
		HashMap<String, Object> hash_map = new HashMap<>();
		List<RoleEntity> role = null;
		sqlQuery = "Select * from ADM_MENU_ROLE_HDR (nolock) where role_id = :roleId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("roleId", roleId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			role = (List<RoleEntity>) query.list();
			if (!role.isEmpty()) {
				sqlQuery = "Select * from ADM_MENU_ROLE_DTL (nolock) where role_id = :roleId";
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("roleId", roleId);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List<RoleMenuEntity> mapping = (List<RoleMenuEntity>) query.list();
				//getAllRoles(req,device);
				//Collection<AdminRoleTreeNode<AdminRoleUnit>>trees = adminRoleCreateDaoImpl.getAssignedFunctionalityToRole(req, device);
				Collection<AdminRoleTreeNode<AdminRoleUnit>>trees = getAllRoles(req, device);

				System.out.println("at return the trees "+trees.toString());
				
				hash_map.put("roleMaster", role);
				hash_map.put("role", mapping);
				hash_map.put("functionality", trees);
			}

		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			ex.printStackTrace();
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session.isOpen())
				session.close();
		}
		return hash_map;
	}
	
	
	
	public  Collection<AdminRoleTreeNode<AdminRoleUnit>> getAllRoles(
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
										roles.add(new AdminRoleUnit(((BigInteger)row1.get("menuid")),(String)row1.get("functionality"),((BigInteger)row1.get("modulid")),(Character) row1.get("RoleMenu_IsActive")));
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
												roles.add(new AdminRoleUnit(((BigInteger)row2.get("menuid")),(String)row2.get("functionality"),((BigInteger)row2.get("modulid")),(Character) row2.get("RoleMenu_IsActive")));
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

		//System.out.println("roots "+roles.toString());
		AdminRoleHierarchy<AdminRoleUnit, BigInteger> service = new AdminRoleHierarchy<>(roles);
		
		Collection<AdminRoleTreeNode<AdminRoleUnit>> trees = service.getRoots().stream()
                .map(service::getTree)
                .collect(Collectors.toSet());
		
		//System.out.println("roots at end we get "+trees.toString());
		//System.out.println("at end   dfdsfds");
		return trees;
	}
	

}
