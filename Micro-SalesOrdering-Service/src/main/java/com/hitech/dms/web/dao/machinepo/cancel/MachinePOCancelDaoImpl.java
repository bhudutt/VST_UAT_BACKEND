/**
 * 
 */
package com.hitech.dms.web.dao.machinepo.cancel;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

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
import com.hitech.dms.web.dao.machinepo.common.MachinePOCommonDao;
import com.hitech.dms.web.entity.machinepo.MachinePOHdrEntity;
import com.hitech.dms.web.model.machinepo.cancel.request.MachinePOCancelRequestModel;
import com.hitech.dms.web.model.machinepo.cancel.response.MachinePOCancelResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MachinePOCancelDaoImpl implements MachinePOCancelDao {
	private static final Logger logger = LoggerFactory.getLogger(MachinePOCancelDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private MachinePOCommonDao machinePOCommonDao;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public MachinePOCancelResponseModel cancelMachinePO(String authorizationHeader, String userCode,
			MachinePOCancelRequestModel requestModel, Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("cancelMachinePO invked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		BigInteger userId = null;
		MachinePOCancelResponseModel responseModel = new MachinePOCancelResponseModel();
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			MachinePOHdrEntity poHdrDBEntity = null;
			mapData = machinePOCommonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				String sqlQuery = "Select * from SA_PO_HDR (nolock) SPH where po_hdr_id =:poHdrId";
				query = session.createNativeQuery(sqlQuery).addEntity(MachinePOHdrEntity.class);
				query.setParameter("poHdrId", requestModel.getPoHdrId());
				poHdrDBEntity = (MachinePOHdrEntity) query.uniqueResult();
				if (poHdrDBEntity != null) {
					if (!poHdrDBEntity.getPoStatus().equals(WebConstants.DRAFT)) {
						// dealer can not edit the Machine Po
						isSuccess = false;
						responseModel.setMsg("Machine PO Can not be Canceled.");
					}
					poHdrDBEntity.setPoStatus("Cancelled");
					poHdrDBEntity.setPoCancelReasonId(requestModel.getPoCancelReason());
					poHdrDBEntity.setPoCancelRemarks(requestModel.getPoCancelRemarks());
					poHdrDBEntity.setModifiedBy(userId);
					poHdrDBEntity.setModifiedDate(new Date());

					session.merge(poHdrDBEntity);
				} else {
					//
					isSuccess = false;
					responseModel.setMsg("Machine PO Number Not Found.");
				}
			} else {
				// User not found
				isSuccess = false;
				responseModel.setMsg("User Not Found.");
			}
			if (isSuccess) {
				transaction.commit();
			}
		} catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setPoHdrId(requestModel.getPoHdrId());
				responseModel.setPoNumber(requestModel.getPoNumber());
				responseModel.setMsg("Machine PO Canceled Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Canceling Machine PO.");
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}
}
