/**
 * 
 */
package com.hitech.dms.web.dao.machinepo.calculation;

import java.math.BigDecimal;
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

import com.hitech.dms.web.model.machinepo.calculation.request.MachinePOItemAmntRequestModel;
import com.hitech.dms.web.model.machinepo.calculation.request.MachinePOTotalAmntRequestModel;
import com.hitech.dms.web.model.machinepo.calculation.response.MachinePOItemAmntResponseModel;
import com.hitech.dms.web.model.machinepo.calculation.response.MachinePOTotalAmntResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MachinePOCalculationDaoImpl implements MachinePOCalculationDao {
	private static final Logger logger = LoggerFactory.getLogger(MachinePOCalculationDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<MachinePOItemAmntResponseModel> calculateMachineItemAmount(String userCode,
			MachinePOItemAmntRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("calculateMachineItemAmount invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<MachinePOItemAmntResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SAORD_CALCULATEPOItemAmnt] :userCode, :pcId, :dealerId, :branchId, :itemId, :qty ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("itemId", requestModel.getItemId());
			query.setParameter("qty", requestModel.getQty());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<MachinePOItemAmntResponseModel>();
				MachinePOItemAmntResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new MachinePOItemAmntResponseModel();
					responseModel.setNetAmount((BigDecimal) row.get("NetAmount"));
					responseModel.setItemGstPer((Double) row.get("ItemGSTPer"));
					responseModel.setItemGstAmount((BigDecimal) row.get("ItemGstAmount"));
					responseModel.setTotalAmount((BigDecimal) row.get("TotalItemAmount"));

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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<MachinePOTotalAmntResponseModel> calculateMachinePOTotalAmount(String userCode,
			MachinePOTotalAmntRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("calculateMachinePOTotalAmount invoked.." + userCode + " " + requestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<MachinePOTotalAmntResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SAORD_CALCULATEPOAMNT] :userCode, :pcId, :dealerId, :branchId, :tcsPer, :totalBaseAmount, :totalGstAmount ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("tcsPer", requestModel.getTcsPer());
			query.setParameter("totalBaseAmount", requestModel.getTotalBaseAmount());
			query.setParameter("totalGstAmount", requestModel.getTotalGstAmount());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<MachinePOTotalAmntResponseModel>();
				MachinePOTotalAmntResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new MachinePOTotalAmntResponseModel();
					responseModel.setTotalTcsAmount((BigDecimal) row.get("TotalTcsAmount"));
					responseModel.setTotalPoAmnt((BigDecimal) row.get("TotalPOAmount"));

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
