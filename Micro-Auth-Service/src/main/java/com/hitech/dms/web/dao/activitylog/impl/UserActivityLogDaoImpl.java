package com.hitech.dms.web.dao.activitylog.impl;

import java.time.LocalDateTime;

import org.dozer.Mapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.dao.activitylog.UserActivityLogDao;
import com.hitech.dms.web.entity.user.accesslog.UserActivityLog;
import com.hitech.dms.web.model.user.activitylog.request.UserActivityLogRequestModel;
import com.hitech.dms.web.model.user.activitylog.response.UserActivityLogResponseModel;

@Repository
public class UserActivityLogDaoImpl implements UserActivityLogDao {
	private static final Logger logger = LoggerFactory.getLogger(UserActivityLogDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private Mapper mapper;

	public UserActivityLogResponseModel addActivityLog(String userCode, UserActivityLogRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("addActivityLog invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
//		Query query = null;
		UserActivityLog userActivityLog = null;
		boolean isSuccess = true;
		UserActivityLogResponseModel responseModel = new UserActivityLogResponseModel();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			userActivityLog = mapper.map(requestModel, UserActivityLog.class, "UserActivityLogMapId");
			if(userActivityLog.getUserCode() == null) {
				userActivityLog.setUserCode(userCode);
			}
			userActivityLog.setLoggedTime(LocalDateTime.now());

			session.save(userActivityLog);
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
			if (isSuccess) {

			} else {

			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}
}
