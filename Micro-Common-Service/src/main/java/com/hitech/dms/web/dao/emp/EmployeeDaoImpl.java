/**
 * 
 */
package com.hitech.dms.web.dao.emp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.emp.request.EmployeeRequestModel;
import com.hitech.dms.web.model.emp.response.EmployeeResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class EmployeeDaoImpl implements EmployeeDao {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<EmployeeResponseModel> fetchEmployeeList(String userCode, EmployeeRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchEmployeeList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<EmployeeResponseModel> responseList = null;
		String sqlQuery = "exec [SP_DLR_EMP_LIST] :userCode, :dealerId, :branchId, :isFor, :includeInactive";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("isFor", requestModel.getIsFor());
			query.setParameter("includeInactive", requestModel.getIsIncludeActive());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<EmployeeResponseModel>();
				EmployeeResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new EmployeeResponseModel();
					responseModel.setEmpId((BigInteger) row.get("EMP_ID"));
					responseModel.setEmpCode((String) row.get("EMP_CODE"));
					responseModel.setEmpName((String) row.get("EMP_NAME"));
					responseModel.setDisplayValue((String) row.get("DisplayValue"));
					responseModel.setIsDefaultForBranch((String) row.get("IsDefaultForBranch"));
					responseModel.setReportingId((BigInteger) row.get("REPORTING_ID"));
					responseList.add(responseModel);
				}
			}
		} catch (SQLGrammarException e) {
			logger.error(this.getClass().getName(), e);
		} catch (HibernateException e) {
			logger.error(this.getClass().getName(), e);
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseList;
	}
}
