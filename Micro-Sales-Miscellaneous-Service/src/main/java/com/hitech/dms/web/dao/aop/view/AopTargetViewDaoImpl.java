/**
 * 
 */
package com.hitech.dms.web.dao.aop.view;

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

import com.hitech.dms.web.model.aop.view.request.AopTargetViewRequestModel;
import com.hitech.dms.web.model.aop.view.response.AopTargetDtlViewResponseModel;
import com.hitech.dms.web.model.aop.view.response.AopTargetViewResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class AopTargetViewDaoImpl implements AopTargetViewDao {
	private static final Logger logger = LoggerFactory.getLogger(AopTargetViewDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("rawtypes")
	public AopTargetViewResponseModel fetchAopTargetView(String userCode, AopTargetViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchAopTargetView invoked.." + requestModel.toString());
		}
		logger.info("fetchAopTargetView : " + requestModel.toString());
		Session session = null;
		AopTargetViewResponseModel responseModel = null;
		List<AopTargetDtlViewResponseModel> aopTargetDtlList;
		try {
			session = sessionFactory.openSession();
			requestModel.setFlag(1);
			List data = fetchAopTargetView(session, userCode, requestModel);
			if (data != null && !data.isEmpty()) {
				responseModel = new AopTargetViewResponseModel();
				for (Object object : data) {
					Map row = (Map) object;
					responseModel.setDealerCode((String) row.get("DealerCode"));
					responseModel.setDealerId((BigInteger) row.get("DealerId"));
					responseModel.setDealerName((String) row.get("DealerName"));
					responseModel.setPcId((Integer) row.get("PcId"));
					responseModel.setPcDesc((String) row.get("PcDesc"));
					responseModel.setAopId((BigInteger) row.get("AopId"));
					responseModel.setAopNumber((String) row.get("AopNumber"));
					responseModel.setAopDate((String) row.get("AopDate"));
					responseModel.setAopFinYr((String) row.get("AopFinYr"));
					responseModel.setRemarks((String) row.get("Remarks"));
					responseModel.setAopStatus((String) row.get("AopStatus"));
					responseModel.setAopUpdatedDate((String) row.get("AopUpdatedDate"));
					responseModel.setAction((String) row.get("Action"));
				}
				requestModel.setFlag(2);
				data = fetchAopTargetView(session, userCode, requestModel);
				if (data != null && !data.isEmpty()) {
					aopTargetDtlList = new ArrayList<AopTargetDtlViewResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						AopTargetDtlViewResponseModel aopDTl = new AopTargetDtlViewResponseModel();
						aopDTl.setAopDtlId((BigInteger) row.get("AopDtlId"));
						aopDTl.setMachineItemId((BigInteger) row.get("MachineItemId"));
						aopDTl.setItem((String) row.get("ItemNo"));
						aopDTl.setItemDesc((String) row.get("ItemDesc"));
						aopDTl.setModel((String) row.get("Model"));
						aopDTl.setVariant((String) row.get("Variant"));
						aopDTl.setSegment((String) row.get("Segment"));
						aopDTl.setSeries((String) row.get("Series"));
						aopDTl.setMonth1((BigDecimal) row.get("M1"));
						aopDTl.setMonth2((BigDecimal) row.get("M2"));
						aopDTl.setMonth3((BigDecimal) row.get("M3"));
						aopDTl.setMonth4((BigDecimal) row.get("M4"));
						aopDTl.setMonth5((BigDecimal) row.get("M5"));
						aopDTl.setMonth6((BigDecimal) row.get("M6"));
						aopDTl.setMonth7((BigDecimal) row.get("M7"));
						aopDTl.setMonth8((BigDecimal) row.get("M8"));
						aopDTl.setMonth9((BigDecimal) row.get("M9"));
						aopDTl.setMonth10((BigDecimal) row.get("M10"));
						aopDTl.setMonth11((BigDecimal) row.get("M11"));
						aopDTl.setMonth12((BigDecimal) row.get("M12"));

						aopTargetDtlList.add(aopDTl);
					}
					responseModel.setAopTargetDtlList(aopTargetDtlList);
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
	public List fetchAopTargetView(Session session, String userCode, AopTargetViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchAopTargetView invoked...");
		}
		List data = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_MIS_AOP_TARGET_DTL] :userCode, :aopId, :flag";
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("aopId", requestModel.getAopId());
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
