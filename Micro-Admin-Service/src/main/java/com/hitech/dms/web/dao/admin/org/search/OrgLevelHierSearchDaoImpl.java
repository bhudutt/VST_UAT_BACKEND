/**
 * 
 */
package com.hitech.dms.web.dao.admin.org.search;

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

import com.hitech.dms.web.model.admin.org.search.request.OrgLevelHierSearchRequestModel;
import com.hitech.dms.web.model.admin.org.search.response.OrgLevelHierSearchMainResponseModel;
import com.hitech.dms.web.model.admin.org.search.response.OrgLevelHierSearchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class OrgLevelHierSearchDaoImpl implements OrgLevelHierSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(OrgLevelHierSearchDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("deprecation")
	@Override
	public OrgLevelHierSearchMainResponseModel fetchOrgHierSearchList(String userCode, OrgLevelHierSearchRequestModel requestModel) {
		NativeQuery<?> query = null;
		Session session = null;
		OrgLevelHierSearchResponseModel responseModel = null;
		List<OrgLevelHierSearchResponseModel> responseModelList = null;
		OrgLevelHierSearchMainResponseModel mainSearchResponse = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_ADM_ORGHIER_SEARCH] :userCode, :dealerId, :departmentId, :includeInactive, :page, :size";
			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("userCode", userCode);
				query.setParameter("dealerId", requestModel.getDealerId());
				query.setParameter("departmentId", requestModel.getDepartmentId());
				query.setParameter("includeInactive", requestModel.getIncludeInActive());
				query.setParameter("page", requestModel.getPage());
				query.setParameter("size", requestModel.getSize());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List<?> result =  query.list();
				if (result != null && !result.isEmpty()) {
					responseModelList = new ArrayList<OrgLevelHierSearchResponseModel>();
					mainSearchResponse = new OrgLevelHierSearchMainResponseModel();
					if (result!= null && result.size()>0 ) {
							for (Object object : result) {
								responseModel = new OrgLevelHierSearchResponseModel();
								Map<?, ?> row = (Map<?, ?>) object;
								responseModel.setAction("edit");
								responseModel.setId((BigInteger) row.get("id")); // dealerId
								responseModel.setId1((Integer) row.get("id1")); // pcId
								responseModel.setId2((Integer) row.get("id2")); // departmentId
								responseModel.setId3((BigInteger) row.get("id3")); // orgHierId
								responseModel.setDealerCode((String) row.get("dealerCode"));
								responseModel.setDealerName((String) row.get("dealerName"));
								responseModel.setDepartmentName((String) row.get("departmentName"));
								responseModel.setHierCode((String) row.get("hierarchyCode"));
								responseModel.setHierDesc((String) row.get("hierarchyDesc"));
								responseModel.setPcDesc((String) row.get("pc_desc"));
								
								if (recordCount.compareTo(0) == 0) {
									recordCount = (Integer) row.get("COUNT");
								}
								responseModelList.add(responseModel);
							}
							mainSearchResponse.setSearchList(responseModelList);
							mainSearchResponse.setRecordCount(recordCount);
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

	
		return mainSearchResponse;
	}
}
