/**
 * 
 */
package com.hitech.dms.web.dao.pcforbranchDealer;

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

import com.hitech.dms.web.model.pcforbranchDealer.request.PcForBranchDealerRequestModel;
import com.hitech.dms.web.model.pcforbranchDealer.response.PcForBranchDealerResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class PcForBranchDealerDaoImpl implements PcForBranchDealerDao {
	private static final Logger logger = LoggerFactory.getLogger(PcForBranchDealerDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<PcForBranchDealerResponseModel> fetchPcForBranchDealerList(String userCode, String isApplicableFor,
			Long dealerOrBranchId, String forSalesFlag) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPcForBranchDealerList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<PcForBranchDealerResponseModel> pcForBranchDealerResponseList = null;
		String sqlQuery = "exec [SP_CM_getPCForBranchDealer] :userCode, :isApplicableFor, :dealerOrBranchId, :forSalesFlag";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("isApplicableFor", isApplicableFor);
			query.setParameter("dealerOrBranchId", dealerOrBranchId);
			query.setParameter("forSalesFlag", forSalesFlag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				pcForBranchDealerResponseList = new ArrayList<PcForBranchDealerResponseModel>();
				PcForBranchDealerResponseModel pcForBranchDealerResponseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					pcForBranchDealerResponseModel = new PcForBranchDealerResponseModel();
					pcForBranchDealerResponseModel.setPcId((Integer) row.get("pc_id"));
					pcForBranchDealerResponseModel.setPcCode((String) row.get("pc_code"));
					pcForBranchDealerResponseModel.setPcName((String) row.get("pc_name"));
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<PcForBranchDealerResponseModel> fetchPcForBranchDealerList(String userCode,
			PcForBranchDealerRequestModel pcForBranchDealerRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPcForBranchDealerList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<PcForBranchDealerResponseModel> pcForBranchDealerResponseList = null;
		String sqlQuery = "exec [SP_CM_getPCForBranchDealer] :userCode, :isApplicableFor, :dealerOrBranchId, :forSalesFlag";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("isApplicableFor", pcForBranchDealerRequestModel.getIsApplicableFor());
			query.setParameter("dealerOrBranchId", pcForBranchDealerRequestModel.getDealerOrBranchID());
			query.setParameter("forSalesFlag", pcForBranchDealerRequestModel.getForSalesFlag());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				pcForBranchDealerResponseList = new ArrayList<PcForBranchDealerResponseModel>();
				PcForBranchDealerResponseModel pcForBranchDealerResponseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					pcForBranchDealerResponseModel = new PcForBranchDealerResponseModel();
					pcForBranchDealerResponseModel.setPcId((Integer) row.get("pc_id"));
					pcForBranchDealerResponseModel.setPcCode((String) row.get("pc_code"));
					pcForBranchDealerResponseModel.setPcName((String) row.get("pc_name"));
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
