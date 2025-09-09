/**
 * 
 */
package com.hitech.dms.web.dao.dealer.assign.branch.search;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.utils.DateToStringParserUtils;
import com.hitech.dms.web.model.dealer.branchassign.search.request.AssignUserToBranchSearchRequestModel;
import com.hitech.dms.web.model.dealer.branchassign.search.response.AssignUserToBranchSearchMainResponseModel;
import com.hitech.dms.web.model.dealer.branchassign.search.response.AssignUserToBranchSearchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class AssignUserToBranchSearchDaoImpl implements AssignUserToBranchSearchDao {
	private static final Logger logger = LoggerFactory.getLogger(AssignUserToBranchSearchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DozerBeanMapper mapper;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public AssignUserToBranchSearchMainResponseModel searchAssignedUserBranchList(String userCode,
			AssignUserToBranchSearchRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("searchAssignedUserBranchList invoked.." + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		AssignUserToBranchSearchMainResponseModel responseMainModel = null;
		List<AssignUserToBranchSearchResponseModel> responseModelList = null;
		Integer recordCount = 0;
		String sqlQuery = "exec [SP_DLR_EMP_ASSIGN_SEARCH] :userCode, :pcID, :orgHierID, :dealerID, :branchID, :fromDate, :toDate, :includeInactive, :page, :size";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcID", requestModel.getPcID());
			query.setParameter("orgHierID", requestModel.getOrgHierID());
			query.setParameter("dealerID", requestModel.getDealerId());
			query.setParameter("branchID", requestModel.getBranchID());
			query.setParameter("fromDate", (requestModel.getFromDate() == null ? null
					: DateToStringParserUtils.addStratTimeOFTheDay(requestModel.getFromDate())));
			query.setParameter("toDate", (requestModel.getToDate() == null ? null
					: DateToStringParserUtils.addEndTimeOFTheDay(requestModel.getToDate())));
			query.setParameter("includeInactive", requestModel.getIncludeInActive());
			query.setParameter("page", requestModel.getPage());
			query.setParameter("size", requestModel.getSize());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseMainModel = new AssignUserToBranchSearchMainResponseModel();
				responseModelList = new ArrayList<AssignUserToBranchSearchResponseModel>();
				AssignUserToBranchSearchResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new AssignUserToBranchSearchResponseModel();
					responseModel.setBranchUserId((BigInteger) row.get("branch_emp_id"));
					responseModel.setDealerId((BigInteger) row.get("dealer_id"));
					responseModel.setEmpId((BigInteger) row.get("dealerEmployeeId"));
					responseModel.setEmpName((String) row.get("employeename"));
					responseModel.setBranchName((String) row.get("BranchName"));
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("COUNT");
					}
					Character isActive = (Character) row.get("isActive");
					if (isActive != null && isActive.toString().equals("Y")) {
						responseModel.setIsActive(isActive.toString());
					} else {
						responseModel.setIsActive("N");
					}

					responseModelList.add(responseModel);
				}

				responseMainModel.setRecordCount(recordCount);
				responseMainModel.setSearchList(responseModelList);

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseMainModel;
	}
}
