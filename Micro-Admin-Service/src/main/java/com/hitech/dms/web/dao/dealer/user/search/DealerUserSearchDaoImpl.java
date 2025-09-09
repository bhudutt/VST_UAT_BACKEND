/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.dao.dealer.user.search;

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
import com.hitech.dms.web.model.dealer.user.search.request.DealerUserSearchRequest;
import com.hitech.dms.web.model.dealer.user.search.response.DealerSearchResponseModel;
import com.hitech.dms.web.model.dealer.user.search.response.DealerUserSearchMainResponse;

/**
 * @author vinay.gautam
 *
 */
@Repository
public class DealerUserSearchDaoImpl implements DealerUserSearchDao {

	private static final Logger logger = LoggerFactory.getLogger(DealerUserSearchDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("deprecation")
	@Override
	public DealerUserSearchMainResponse fetchDealerUserSearch(String userCode, DealerUserSearchRequest requestModel) {
		NativeQuery<?> query = null;
		Session session = null;
		DealerSearchResponseModel responseModel = null;
		List<DealerSearchResponseModel> responseModelList = null;
		DealerUserSearchMainResponse mainSearchResponse = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_DLR_User_SEARCH] :dealerId, :branchId, :userCode, :employeeCode, :employeeName, :fromDate, :toDate ,:orgHierId, :pcId, :includeInactive,:status,:page, :size";
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
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("includeInactive", 'N');
			query.setParameter("status",requestModel.getStatus());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> result = query.list();
			if (result != null && !result.isEmpty()) {
				responseModelList = new ArrayList<DealerSearchResponseModel>();
				mainSearchResponse = new DealerUserSearchMainResponse();
				if (result != null && result.size() > 0) {
					for (Object object : result) {
						responseModel = new DealerSearchResponseModel();
						Map<?, ?> row = (Map<?, ?>) object;
						responseModel.setAction("edit");
						responseModel.setId((BigInteger) row.get("id"));
						responseModel.setEmpCode((String) row.get("empCode"));
						responseModel.setEmpName((String) row.get("empName"));
						responseModel.setStatus((String) row.get("status"));
						responseModel.setIsActive((Character) row.get("IsActive"));

						if (recordCount.compareTo(0) == 0) {
							recordCount = (Integer) row.get("count");
						}
						responseModelList.add(responseModel);
					}
					mainSearchResponse.setSearch(responseModelList);
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
