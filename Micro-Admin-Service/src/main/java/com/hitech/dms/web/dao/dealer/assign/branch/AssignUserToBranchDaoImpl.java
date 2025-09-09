/**
 * 
 */
package com.hitech.dms.web.dao.dealer.assign.branch;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.dao.CommonDao;
import com.hitech.dms.web.entity.branch.AssignUserToBranchEntity;
import com.hitech.dms.web.model.dealer.branchassign.dtl.request.AssignUserToBranchFormModel;
import com.hitech.dms.web.model.dealer.branchassign.dtl.request.AssignUserToBranchRequestModel;
import com.hitech.dms.web.model.dealer.branchassign.dtl.response.AssignUserToBranchResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class AssignUserToBranchDaoImpl implements AssignUserToBranchDao {
	private static final Logger logger = LoggerFactory.getLogger(AssignUserToBranchDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private CommonDao commonDao;

	public AssignUserToBranchResponseModel assignBranchesToUser(String userCode,
			AssignUserToBranchFormModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("assignBranchesToUser invoked..");
		}

		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		AssignUserToBranchResponseModel responseModel = new AssignUserToBranchResponseModel();
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				List<AssignUserToBranchRequestModel> userBranchList = requestModel.getBranchList();
				for (AssignUserToBranchRequestModel branchRequestModel : userBranchList) {
					AssignUserToBranchEntity branchEntity = mapper.map(branchRequestModel,
							AssignUserToBranchEntity.class, "assignBranchUserMapId");
					branchEntity.setEmpId(requestModel.getEmpId());
					branchEntity.setCreatedBy(userCode);
					branchEntity.setCreatedDate(new Date());

					session.saveOrUpdate(branchEntity);
				}
			} else {
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
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			isSuccess = false;
			responseModel.setMsg(ex.getMessage());
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (isSuccess) {
				responseModel.setMsg("Branches Assigned To user Successfully.");
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
		}
		return responseModel;
	}
}
