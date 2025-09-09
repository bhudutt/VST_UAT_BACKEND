/**
 * 
 */
package com.hitech.dms.web.dao.admin.search;

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

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.admin.search.request.HoUserSearchRequestModel;
import com.hitech.dms.web.model.admin.search.response.HoUserSearchMainResponseModel;
import com.hitech.dms.web.model.admin.search.response.HoUserSearchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class HoUserSearchDaoImpl implements HoUserSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(HoUserSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("deprecation")
	@Override
	public HoUserSearchMainResponseModel fetchHoUserSearch(String userCode, HoUserSearchRequestModel requestModel) {
		NativeQuery<?> query = null;
		Session session = null;
		HoUserSearchResponseModel responseModel = null;
		List<HoUserSearchResponseModel> responseModelList = null;
		HoUserSearchMainResponseModel mainSearchResponse = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_ADM_HoUser_SEARCH] :dealerId, :branchId, :userCode, :employeeCode, :employeeName, :fromDate, :toDate ,:orgHierId, :includeInactive, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("userCode", userCode);
			query.setParameter("employeeCode", requestModel.getEmpCode());
			query.setParameter("employeeName", requestModel.getEmpName());
			query.setParameter("fromDate", (requestModel.getFromDate() == null ? null
					: DateToStringParserUtils.addStratTimeOFTheDay(requestModel.getFromDate())));
			query.setParameter("toDate", (requestModel.getToDate() == null ? null
					: DateToStringParserUtils.addEndTimeOFTheDay(requestModel.getToDate())));
			query.setParameter("orgHierId", requestModel.getOrgHierId());
			query.setParameter("includeInactive", 'N');
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> result = query.list();
			if (result != null && !result.isEmpty()) {
				responseModelList = new ArrayList<HoUserSearchResponseModel>();
				mainSearchResponse = new HoUserSearchMainResponseModel();
				if (result != null && result.size() > 0) {
					for (Object object : result) {
						responseModel = new HoUserSearchResponseModel();
						Map<?, ?> row = (Map<?, ?>) object;
						responseModel.setAction("action");
						responseModel.setId((BigInteger) row.get("id"));
						responseModel.setEmpCode((String) row.get("empCode"));
						responseModel.setEmpName((String) row.get("empName"));
						responseModel.setEmpStatus((String) row.get("IsActive"));
						responseModel.setLoginId((String) row.get("LoginId"));
						responseModel.setLoginIdStatus((String) row.get("LoginIdStatus"));

						if (recordCount.compareTo(0) == 0) {
							recordCount = (Integer) row.get("count");
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
