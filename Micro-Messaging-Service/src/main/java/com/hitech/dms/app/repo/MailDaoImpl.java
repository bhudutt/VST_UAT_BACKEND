/**
 * 
 */
package com.hitech.dms.app.repo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
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
public class MailDaoImpl implements MailDao {
	private static final Logger logger = LoggerFactory.getLogger(MailDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Override
	public boolean insert(MsgLog msgLog) {
		Session session = null;
		Transaction transaction = null;
		boolean isInserted = false;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.save(msgLog);
			transaction.commit();
			
			isInserted = true;
		}catch (SQLGrammarException e) {
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
		return isInserted;
	}
	
}
