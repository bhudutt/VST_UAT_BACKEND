/**
 * 
 */
package com.hitech.dms.web.dao.opex.view;

import java.math.BigDecimal;
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
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.model.opex.view.request.OpexBudgetViewRequestModel;
import com.hitech.dms.web.model.opex.view.response.OpexBudgetDtlViewResponseModel;
import com.hitech.dms.web.model.opex.view.response.OpexBudgetViewResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class OpexBudgetViewDaoImpl implements OpexBudgetViewDao {
	private static final Logger logger = LoggerFactory.getLogger(OpexBudgetViewDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public OpexBudgetViewResponseModel fetchOpexBudgetView(String userCode, OpexBudgetViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOpexBudgetView invoked.." + requestModel.toString());
		}
		logger.info("fetchOpexBudgetView : " + requestModel.toString());
		Session session = null;
		OpexBudgetViewResponseModel responseModel = null;
		List<OpexBudgetDtlViewResponseModel> opexBudgetDtlList;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchOpexBudgetView(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				responseModel = new OpexBudgetViewResponseModel();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel.setStateCode((String) row.get("StateCode"));
					responseModel.setStateId((BigInteger) row.get("StateId"));
					responseModel.setStateName((String) row.get("StateName"));
					responseModel.setPcId((Integer) row.get("PcId"));
					responseModel.setPcDesc((String) row.get("PcDesc"));
					responseModel.setOpexId((BigInteger) row.get("OpexId"));
					responseModel.setOpexNumber((String) row.get("OpexNumber"));
					responseModel.setOpexDate((String) row.get("OpexDate"));
					responseModel.setOpexFinYr((String) row.get("OpexFinYr"));
					responseModel.setRemarks((String) row.get("Remarks"));
					responseModel.setOpexStatus((String) row.get("OpexStatus"));
					responseModel.setOpexUpdatedDate((String) row.get("OpexUpdatedDate"));
					responseModel.setAction((String) row.get("Action"));
				}
				requestModel.setFlag(2);
				data = fetchOpexBudgetView(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					opexBudgetDtlList = new ArrayList<OpexBudgetDtlViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						OpexBudgetDtlViewResponseModel opexDtl = new OpexBudgetDtlViewResponseModel();
						opexDtl.setOpexDtlId((BigInteger) row.get("OpexDtlId"));
						opexDtl.setGlId((BigInteger) row.get("GlId"));
						opexDtl.setGlHeadCode((String) row.get("GlHeadCode"));
						opexDtl.setGlHeadName((String) row.get("GlHeadName"));
						opexDtl.setMonth1((BigDecimal) row.get("M1"));
						opexDtl.setMonth2((BigDecimal) row.get("M2"));
						opexDtl.setMonth3((BigDecimal) row.get("M3"));
						opexDtl.setMonth4((BigDecimal) row.get("M4"));
						opexDtl.setMonth5((BigDecimal) row.get("M5"));
						opexDtl.setMonth6((BigDecimal) row.get("M6"));
						opexDtl.setMonth7((BigDecimal) row.get("M7"));
						opexDtl.setMonth8((BigDecimal) row.get("M8"));
						opexDtl.setMonth9((BigDecimal) row.get("M9"));
						opexDtl.setMonth10((BigDecimal) row.get("M10"));
						opexDtl.setMonth11((BigDecimal) row.get("M11"));
						opexDtl.setMonth12((BigDecimal) row.get("M12"));

						opexBudgetDtlList.add(opexDtl);
					}
					responseModel.setOpexBudgetDtlList(opexBudgetDtlList);
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
		return responseModel;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List fetchOpexBudgetView(Session session, String userCode, OpexBudgetViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOpexBudgetView invoked...");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_MIS_OPEX_BUDGET_DTL] :userCode, :opexId, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("opexId", requestModel.getOpexId());
			query.setParameter("flag", requestModel.getFlag());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			data = query.list();
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		}
		return data;
	}
}
