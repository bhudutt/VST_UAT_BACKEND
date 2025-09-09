/**
 * 
 */
package com.hitech.dms.web.dao.admin.org.edit;

import java.math.BigInteger;
import java.util.Date;
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
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.dao.CommonDao;
import com.hitech.dms.web.entity.admin.org.DealerOrgHierEntity;
import com.hitech.dms.web.model.admin.org.edit.request.OrgLevelHierEditRequestModel;
import com.hitech.dms.web.model.admin.org.response.OrgLevelHierResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class OrgLevelHierEditDaoImpl implements OrgLevelHierEditDao {
	private static final Logger logger = LoggerFactory.getLogger(OrgLevelHierEditDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public OrgLevelHierResponseModel editOrgLevelHier(String userCode, OrgLevelHierEditRequestModel requestModel,
			Device device) {
		if (logger.isDebugEnabled()) {
			logger.debug("editOrgLevelHier invoked..");
		}
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		OrgLevelHierResponseModel responseModel = new OrgLevelHierResponseModel();
		boolean isSuccess = true;
		String sqlQuery = "Select * from ADM_BP_DEALER_ORGHIER(nolock) where dealer_id =:dealerId and pc_id=:pcId and department_id =:departmentId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				Query query = session.createNativeQuery(sqlQuery).addEntity(DealerOrgHierEntity.class);
				query.setParameter("dealerId", requestModel.getDealerId());
				query.setParameter("pcId", requestModel.getPcId());
				query.setParameter("departmentId", requestModel.getDepartmentId());
				DealerOrgHierEntity dealerOrgHierDBEntity = (DealerOrgHierEntity) query.uniqueResult();
				if (dealerOrgHierDBEntity != null) {
					// update
					dealerOrgHierDBEntity.setOrgHierId(requestModel.getOrgHierId());
					dealerOrgHierDBEntity.setIsActive(requestModel.getIsActive());

					dealerOrgHierDBEntity.setModifiedBy(userCode);
					dealerOrgHierDBEntity.setModifiedDate(new Date());

					session.merge(dealerOrgHierDBEntity);
				} else {
					DealerOrgHierEntity dealerOrgHierEntity = mapper.map(requestModel, DealerOrgHierEntity.class,
							"AddOrgHierMapId");

					dealerOrgHierEntity.setCreatedBy(userCode);
					dealerOrgHierEntity.setCreatedDate(new Date());

					dealerOrgHierEntity.setIsActive(true);
					session.save(dealerOrgHierEntity);
				}
			} else {
				// user not found
				responseModel.setMsg("User Not Found.");
				isSuccess = false;
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
				responseModel.setMsg("Dealer Org. Hier. Updated Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			}else {
				if(responseModel.getMsg() == null) {
					responseModel.setMsg("Please Contact Your System Administrator.");
				}
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}
}
