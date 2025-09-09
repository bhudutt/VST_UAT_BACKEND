/**
 * 
 */
package com.hitech.dms.web.dao.activityClaim.inv.approval.dao;

import java.math.BigInteger;
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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.activity.common.dao.ActivityCommonDao;
import com.hitech.dms.web.model.activityClaim.invoice.approve.request.ActivityClaimInvApprovalRequestModel;
import com.hitech.dms.web.model.activityClaim.invoice.approve.response.ActivityClaimInvApprovalResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ActivityClaimInvApprovalDaoImpl implements ActivityClaimInvApprovalDao {
	private static final Logger logger = LoggerFactory.getLogger(ActivityClaimInvApprovalDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ActivityCommonDao activityCommonDao;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public ActivityClaimInvApprovalResponseModel approveRejectActivityClaimInvoice(String userCode,
			ActivityClaimInvApprovalRequestModel requestModel, Device device) {
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		ActivityClaimInvApprovalResponseModel responseModel = new ActivityClaimInvApprovalResponseModel();
		boolean isSuccess = true;
		String sqlQuery = "exec [SP_SACT_ACTIVITYCLAIMINV_APPROVAL] :userCode, :hoUserId, :activityClaimInvHdrId, :approvalStatus, :remark";
		try {
			session = sessionFactory.openSession();	
			transaction = session.beginTransaction();
			BigInteger userId = null;
			String msg = null;
			String approvalStatus = null;
			mapData = activityCommonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				mapData = activityCommonDao.fetchHOUserDTLByUserCode(session, userCode);
				if (mapData != null && mapData.get("SUCCESS") != null) {
					BigInteger hoUserId = (BigInteger) mapData.get("hoUserId");
					query = session.createNativeQuery(sqlQuery);
					query.setParameter("userCode", userCode);
					query.setParameter("hoUserId", hoUserId);
					query.setParameter("activityClaimInvHdrId", requestModel.getActivityClaimInvHdrId());
					query.setParameter("approvalStatus", requestModel.getApprovalStatus());
					query.setParameter("remark", requestModel.getRemarks());
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					List data = query.list();
					if (data != null && !data.isEmpty()) {
						for (Object object : data) {
							Map row = (Map) object;
							msg = (String) row.get("msg");
							responseModel.setMsg(msg);
							approvalStatus = (String) row.get("approvalStatus");
							responseModel.setApprovalStatus(approvalStatus);
						}
					} else {
						isSuccess = false;
						responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
						responseModel.setMsg("Error While Validating Claim Invoice Approval.");
					}
				} else {
					isSuccess = false;
					responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					responseModel.setMsg("HO User Not Found.");
				}
			} else {
				isSuccess = false;
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
				transaction.commit();
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				responseModel.setApprovalStatus(approvalStatus);
				responseModel.setMsg(msg);
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}
}
