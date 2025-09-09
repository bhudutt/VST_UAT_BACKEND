/**
 * 
 */
package com.hitech.dms.web.dao.admin.dlrvstehsil.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
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
import com.hitech.dms.web.dao.common.dao.CommonDao;
import com.hitech.dms.web.entity.dlrvstehsil.DlrVsTehsilMapEntity;
import com.hitech.dms.web.entity.dlrvstehsil.DlrVsTehsilMapPEntity;
import com.hitech.dms.web.model.admin.dlrvstehsil.request.DlrVsTehsilListRequestModel;
import com.hitech.dms.web.model.admin.dlrvstehsil.request.DlrVsTehsilMapRequestModel;
import com.hitech.dms.web.model.admin.dlrvstehsil.response.DlrVsTehsilMapResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class DlrVsTehsilMapDaoImpl implements DlrVsTehsilMapDao {
	private static final Logger logger = LoggerFactory.getLogger(DlrVsTehsilMapDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public DlrVsTehsilMapResponseModel mapDealerVsTehsil(String userCode, DlrVsTehsilMapRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("mapDealerVsTehsil invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		DlrVsTehsilMapResponseModel responseModel = new DlrVsTehsilMapResponseModel();
		List<DlrVsTehsilListRequestModel> tehsilList = requestModel.getTehsilList();
		DlrVsTehsilMapEntity dlrVsTehsilMapEntity = null;
		boolean isSuccess = true;
		String sqlQuery = "delete from ADM_BP_DEALER_TEHSIL_MAP where parent_dealer_id =:dealerId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				// current date
				Date currDate = new Date();
				
				query = session.createNativeQuery(sqlQuery);
				query.setParameter("dealerId", requestModel.getDealerId());
				int k = query.executeUpdate();
				if(k >= 0 ) {
					for(int i =0; i < tehsilList.size(); i++) {
						DlrVsTehsilListRequestModel dlrVsTehsilListRequestModel =  tehsilList.get(i);
						
						dlrVsTehsilMapEntity = new DlrVsTehsilMapEntity();
						DlrVsTehsilMapPEntity dlrVsTehsilMapPEntity = new DlrVsTehsilMapPEntity();
						dlrVsTehsilMapPEntity.setDealerId(requestModel.getDealerId());
						dlrVsTehsilMapPEntity.setTehsilId(dlrVsTehsilListRequestModel.getTehsilId());
						
						dlrVsTehsilMapEntity.setDlrVsTehsilMapP(dlrVsTehsilMapPEntity);
						dlrVsTehsilMapEntity.setModifiedBy(userCode);
						dlrVsTehsilMapEntity.setModifiedDate(currDate);
						
						session.save(dlrVsTehsilMapEntity);
						
						if (i % 20 == 0) {
							// flush a batch of inserts and release memory:
							session.flush();
							session.clear();
						}
					}
				}
			} else {
				// user not found
				responseModel.setMsg("User Not Found.");
				isSuccess = false;
			}
			if (isSuccess) {
				responseModel.setMsg("Tehsils To Dealer Assigned Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				
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
			if (!isSuccess) {
				if (responseModel.getMsg() == null) {
					responseModel.setMsg("Error While Assiging Tehsils To Dealer.");
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}
}
