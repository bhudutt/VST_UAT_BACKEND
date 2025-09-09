/**
 * 
 */
package com.hitech.dms.web.dao.admin.autosearch;

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

import com.hitech.dms.web.model.admin.search.request.HoUserListRequestModel;
import com.hitech.dms.web.model.admin.search.response.HoUserListResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class HoUserListDaoImpl implements HoUserListDao {
	private static final Logger logger = LoggerFactory.getLogger(HoUserListDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<HoUserListResponseModel> fetchAutoHoUserList(String userCode, HoUserListRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchAutoHoUserList invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<HoUserListResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_ADM_GETHOUSRLIST_FOR_AUTOSEARCH] :userCode, :searchText ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("searchText", requestModel.getSearchText());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<HoUserListResponseModel>();
				HoUserListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new HoUserListResponseModel();
					responseModel.setHoUserId((BigInteger) row.get("HoUserId"));
					responseModel.setEmployeeName((String) row.get("EmployeeName"));
					responseModel.setEmployeeCode((String) row.get("EmployeeCode"));
					responseModel.setEmpContactNo((String) row.get("EmpContactNo"));
					responseModel.setDeisplayValue((String) row.get("displayValue"));

					responseModelList.add(responseModel);
				}
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
		return responseModelList;
	}
}
