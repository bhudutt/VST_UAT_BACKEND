/**
 * 
 */
package com.hitech.dms.app.repo;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.entity.MsgLog;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class MsgLogDaoImpl implements MsgLogDao {
	private static final Logger logger = LoggerFactory.getLogger(MsgLogDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@Override
	public boolean updateStatus(String msgId, Integer status) {
		Session session = null;
		Transaction transaction = null;
		NativeQuery<?> query = null;
		boolean isUpdated = false;
		String sqlQuery = "update ADM_MSG_LOG set status =:status, update_time = GetDate()"
				+ "        where msg_id =:msgId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("status", status);
			query.setParameter("msgId", msgId);
			int k = query.executeUpdate();
			if (k > 0) {
				isUpdated = true;
				logger.info("Msg Log Updated Status For  ", msgId);
			}
			transaction.commit();
		} catch (SQLGrammarException e) {
			logger.error(this.getClass().getName(), e);
			if (transaction.isActive())
				transaction.rollback();
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
			if (transaction.isActive())
				transaction.rollback();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return isUpdated;
	}

	@Override
	public MsgLog selectByMsgId(String msgId) {
		Session session = null;
		NativeQuery<?> query = null;
		MsgLog msgLog = null;
		String sqlQuery = "Select * from ADM_MSG_LOG (nolock) where msg_id =:msgId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery).addEntity(MsgLog.class);
			query.setParameter("msgId", msgId);
			msgLog = (MsgLog) query.uniqueResult();
		} catch (SQLGrammarException sqlge) {
			sqlge.printStackTrace();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return msgLog;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MsgLog> selectTimeoutMsg() {
		Session session = null;
		NativeQuery<?> query = null;
		List<MsgLog> msgLogList = null;
		String sqlQuery = "select * from ADM_MSG_LOG where status = 0 and next_try_time <= getDate() ";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery).addEntity(MsgLog.class);
			msgLogList = (List<MsgLog>) query.list();
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return msgLogList;
	}

	@Override
	public void updateTryCount(String msgId, Date tryTime) {
		// TODO Auto-generated method stub
		Session session = null;
		Transaction transaction = null;
		NativeQuery<?> query = null;
		String sqlQuery = "update ADM_MSG_LOG set try_count = try_count + 1, next_try_time =:nextTryTime, update_time = GetDate()"
				+ "        where msg_id =:msgId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("nextTryTime", tryTime);
			query.setParameter("msgId", msgId);
			int k = query.executeUpdate();
			if (k > 0) {
				logger.info("Msg Log Updated Try Count For  ", msgId);
			}
			transaction.commit();
		} catch (SQLGrammarException e) {
			logger.error(this.getClass().getName(), e);
			if (transaction.isActive())
				transaction.rollback();
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
			if (transaction.isActive())
				transaction.rollback();
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

}
