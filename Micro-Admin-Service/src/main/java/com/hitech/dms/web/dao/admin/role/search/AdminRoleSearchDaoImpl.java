package com.hitech.dms.web.dao.admin.role.search;

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

import com.hitech.dms.web.model.admin.role.search.response.AdminRoleSearchMainResponse;
import com.hitech.dms.web.model.admin.role.search.response.AdminRoleSearchResponse;
import com.hitech.dms.web.model.admin.role.search.resquest.AdminRoleSearchRequest;

/**
 * @author vinay.gautam
 *
 */
@Repository
public class AdminRoleSearchDaoImpl implements AdminRoleSearchDao{
	
	private static final Logger logger = LoggerFactory.getLogger(AdminRoleSearchDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;


	@SuppressWarnings("deprecation")
	@Override
	public AdminRoleSearchMainResponse fetchAdminRoleSearch(String userCode, AdminRoleSearchRequest requestModel) {
		NativeQuery<?> query = null;
		Session session = null;
		AdminRoleSearchResponse responseModel = null;
		List<AdminRoleSearchResponse> responseModelList = null;
		AdminRoleSearchMainResponse mainSearchResponse = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_ADM_SEARCH_ROLE] :dealerId, :branchId, :userCode, :roleCode, :roleName,:isActive, :applicableTo,:fromDate, :toDate ,:orgHierId, :includeInactive, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("userCode", userCode);
			query.setParameter("roleCode", requestModel.getRoleCode());
			query.setParameter("roleName", requestModel.getRoleName());
			query.setParameter("isActive", requestModel.getIsActive());
			query.setParameter("applicableTo", requestModel.getApplicableTo());
			query.setParameter("fromDate", requestModel.getFromDate());
			query.setParameter("toDate", requestModel.getToDate());
			query.setParameter("orgHierId", requestModel.getOrgHierId());
			query.setParameter("includeInactive", requestModel.getIncludeInactive());
		   //query.setParameter("status", requestModel.getIncludeInactive());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<AdminRoleSearchResponse>();
				mainSearchResponse = new AdminRoleSearchMainResponse();
				for (Object object : data) {
					Map<?, ?> row = (Map<?, ?>) object;
					responseModel = new AdminRoleSearchResponse();
					responseModel.setId((BigInteger) row.get("id"));
					responseModel.setAction("edit");
					responseModel.setRoleCode((String) row.get("roleCode"));
					responseModel.setRoleName((String) row.get("roleName"));
					responseModel.setCreatedDate((String) row.get("created_date"));
					responseModel.setStatus((char) row.get("roleActive"));
					responseModel.setApplicableTo((String) row.get("applicableTo"));

					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
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
