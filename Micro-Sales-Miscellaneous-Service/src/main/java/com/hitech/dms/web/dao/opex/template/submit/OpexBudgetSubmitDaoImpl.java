/**
 * 
 */
package com.hitech.dms.web.dao.opex.template.submit;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.opex.OpexBudgetAppEntity;
import com.hitech.dms.web.model.opex.template.submit.request.OpexBudgetSubmitRequestModel;
import com.hitech.dms.web.model.opex.template.submit.response.OpexBudgetSubmitResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class OpexBudgetSubmitDaoImpl implements OpexBudgetSubmitDao {
	private static final Logger logger = LoggerFactory.getLogger(OpexBudgetSubmitDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public OpexBudgetSubmitResponseModel submitOpexBudget(String authorizationHeader, String userCode,
			OpexBudgetSubmitRequestModel requestModel) {
		logger.debug("submitOpexBudget : ");
		logger.debug(userCode + " : " + requestModel.toString());

		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		OpexBudgetSubmitResponseModel responseModel = new OpexBudgetSubmitResponseModel();
		String sqlQuery = "exec [SP_SA_MIS_OPEX_Upload_BUDGET] :userCode, :stgOpexId, :stgOpexNumber, :aopId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				BigInteger userId = (BigInteger) mapData.get("userId");
				query = session.createNativeQuery(sqlQuery);
				query.setParameter("userCode", userCode);
				query.setParameter("stgOpexId", requestModel.getStgOpexId());
				query.setParameter("stgOpexNumber", requestModel.getStgOpexNumber());
				query.setParameter("aopId", requestModel.getOpexId());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						Map row = (Map) object;

						responseModel.setOpexId((BigInteger) row.get("OpexId"));
						responseModel.setOpexNumber((String) row.get("OpexNumber"));
						responseModel.setMsg((String) row.get("Msg"));
						responseModel.setStatusCode((int) row.get("Status"));
					}

					// insert into Approval Table
					mapData = saveIntoApproval(session, userId, null, responseModel.getOpexId());
					if (mapData != null && mapData.get("SUCCESS") != null) {
						transaction.commit();
					} else {
						responseModel.setMsg("Error While Inserting Into Opex Budget Approval Hier.");
						responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					}
				}
			}

		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (responseModel.getMsg() == null) {
				responseModel.setMsg("Error While Uploading Aop Target.");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}

	@SuppressWarnings({ "rawtypes" })
	private Map<String, Object> saveIntoApproval(Session session, BigInteger userId, BigInteger hoUserId,
			BigInteger aopId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("ERROR", "ERROR WHILE INSERTING INTO OPEX BUDGET APPROVAL TABLE.");
		try {
			List data = commonDao.fetchApprovalData(session, "SA_OPEX_BUDGET_APPROVAL");
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					OpexBudgetAppEntity approvalEntity = new OpexBudgetAppEntity();
					;
					approvalEntity.setOpexId(aopId);
					approvalEntity.setApproverLevelSeq((Integer) row.get("approver_level_seq"));
					approvalEntity.setApprovalStatus((String) row.get("approvalStatus"));
					approvalEntity.setDesignationLevelId((Integer) row.get("designation_level_id"));
					approvalEntity.setGrpSeqNo((Integer) row.get("grp_seq_no"));
					Character isFinalApprovalStatus = (Character) row.get("isFinalApprovalStatus");
					if (isFinalApprovalStatus != null && isFinalApprovalStatus.toString().equals("Y")) {
						approvalEntity.setIsFinalApprovalStatus('Y');
					} else {
						approvalEntity.setIsFinalApprovalStatus('N');
					}
					approvalEntity.setRejectedFlag('N');
					approvalEntity.setHoUserId(null);

					session.save(approvalEntity);
				}
			}
			mapData.put("SUCCESS", "Inserted Into AOP Target Approval Table.");
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
}
