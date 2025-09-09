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

import com.hitech.dms.web.model.emp.response.DlrEmployeesUnderUserResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class DlrEmployeesUnderUserDaoImpl implements DlrEmployeesUnderUserDao {
	private static final Logger logger = LoggerFactory.getLogger(DlrEmployeesUnderUserDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<DlrEmployeesUnderUserResponseModel> fetchDlrEmpUnderUserList(String userCode, String includeInactive) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchDlrEmpUnderUserList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<DlrEmployeesUnderUserResponseModel> responseList = null;
		String sqlQuery = "Select * from [FN_GetDealerEmployeesUnderUser] (:userCode, :includeInactive)";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("includeInactive", includeInactive);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<DlrEmployeesUnderUserResponseModel>();
				DlrEmployeesUnderUserResponseModel pcForBranchDealerResponseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					pcForBranchDealerResponseModel = new DlrEmployeesUnderUserResponseModel();
					pcForBranchDealerResponseModel.setEmpId((BigInteger) row.get("EMP_ID"));
					pcForBranchDealerResponseModel.setEmpCode((String) row.get("EMP_CODE"));
					pcForBranchDealerResponseModel.setEmpName((String) row.get("EMP_NAME"));
					pcForBranchDealerResponseModel.setReportingId((BigInteger) row.get("REPORTING_ID"));
					responseList.add(pcForBranchDealerResponseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseList;
	}
}
