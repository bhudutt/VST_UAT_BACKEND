/**
 * 
 */
package com.hitech.dms.web.dao.pc;

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

import com.hitech.dms.web.model.pc.response.ProfitCenterUnderUserResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ProfitCenterDaoImpl implements ProfitCenterDao {
	private static final Logger logger = LoggerFactory.getLogger(ProfitCenterDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<ProfitCenterUnderUserResponseModel> fetchPcUnderUserList(String userCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPcUnderUserList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<ProfitCenterUnderUserResponseModel> pcForBranchDealerResponseList = null;
		String sqlQuery = "Select * from [FN_CM_getProfitCenterUnderUser] (:userCode)";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				pcForBranchDealerResponseList = new ArrayList<ProfitCenterUnderUserResponseModel>();
				ProfitCenterUnderUserResponseModel pcForBranchDealerResponseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					pcForBranchDealerResponseModel = new ProfitCenterUnderUserResponseModel();
					pcForBranchDealerResponseModel.setPcId((Integer) row.get("pc_id"));
					pcForBranchDealerResponseModel.setPcCode((String) row.get("pc_code"));
					pcForBranchDealerResponseModel.setPcDesc((String) row.get("pc_desc"));
					pcForBranchDealerResponseList.add(pcForBranchDealerResponseModel);
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
		return pcForBranchDealerResponseList;
	}
}
