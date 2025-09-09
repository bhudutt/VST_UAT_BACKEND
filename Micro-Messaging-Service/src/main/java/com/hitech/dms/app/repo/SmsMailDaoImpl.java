/**
 * 
 */
package com.hitech.dms.app.repo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
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

import com.hitech.dms.app.model.mail.SmsMailRequestModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class SmsMailDaoImpl implements SmsMailDao {
	private static final Logger logger = LoggerFactory.getLogger(SmsMailDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public SmsMailRequestModel fetchEventMailByMailItemId(BigInteger mailItemId) {
		Session session = null;
		NativeQuery<?> query = null;
		SmsMailRequestModel requestModel = null;
		String sqlQuery = "select csm.*, csem.TemplateName " + " from CM_SMSMAIL_MAIL (nolock) csm "
				+ " inner join CM_SMSMAIL_EVENT_MST (nolock) csem on csm.event_id = csem.event_id "
				+ " where NULLIF(LTRIM(RTRIM(ToMailId)), '') IS NOT NULL and csm.mailitem_id =:mailItemId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("mailItemId", mailItemId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					requestModel = new SmsMailRequestModel();
					requestModel.setBccMailId((String) row.get("BCCMailId"));
					requestModel.setCcMailId((String) row.get("CCMailId"));
					requestModel.setCreatedBy((BigInteger) row.get("Createdby"));
					requestModel.setCreatedDate((Date) row.get("CreatedDate"));
					requestModel.setEvent_id((Integer) row.get("event_id"));
					requestModel.setFromMailId((String) row.get("FromMailId"));
					requestModel.setMailBodyTxt((String) row.get("MailBodyTxt"));
					requestModel.setMailitem_id((BigInteger) row.get("mailitem_id"));
					requestModel.setMailSentDate((Date) row.get("MailSentDate"));
					requestModel.setMailSubject((String) row.get("MailSubject"));
					requestModel.setReferenceId((BigInteger) row.get("ReferenceId"));
					requestModel.setMailStatus((String) row.get("MailStatus"));
					requestModel.setTemplateName((String) row.get("TemplateName"));
					requestModel.setToMailId((String) row.get("ToMailId"));

				}
			}
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return requestModel;
	}

	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	@Override
	public List<SmsMailRequestModel> fetchEventMailsForTrigger(String eventName, String status) {
		Session session = null;
		NativeQuery<?> query = null;
		List<SmsMailRequestModel> msgLogList = null;
		String sqlQuery = "select csm.*, csem.TemplateName " + " from CM_SMSMAIL_MAIL (nolock) csm "
				+ " inner join CM_SMSMAIL_EVENT_MST (nolock) csem on csm.event_id = csem.event_id and csem.EventName =:eventName "
				+ " where MailStatus =:status and NULLIF(LTRIM(RTRIM(ToMailId)), '') IS NOT NULL";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("eventName", eventName);
			query.setParameter("status", status);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				msgLogList = new ArrayList<SmsMailRequestModel>();
				for (Object object : data) {
					Map row = (Map) object;
					SmsMailRequestModel requestModel = new SmsMailRequestModel();
					requestModel.setBccMailId((String) row.get("BCCMailId"));
					requestModel.setCcMailId((String) row.get("CCMailId"));
					requestModel.setCreatedBy((BigInteger) row.get("Createdby"));
					requestModel.setCreatedDate((Date) row.get("CreatedDate"));
					requestModel.setEvent_id((Integer) row.get("event_id"));
					requestModel.setFromMailId((String) row.get("FromMailId"));
					requestModel.setMailBodyTxt((String) row.get("MailBodyTxt"));
					requestModel.setMailitem_id((BigInteger) row.get("mailitem_id"));
					requestModel.setMailSentDate((Date) row.get("MailSentDate"));
					requestModel.setMailSubject((String) row.get("MailSubject"));
					requestModel.setReferenceId((BigInteger) row.get("ReferenceId"));
					requestModel.setMailStatus((String) row.get("MailStatus"));
					requestModel.setTemplateName((String) row.get("TemplateName"));
					requestModel.setToMailId((String) row.get("ToMailId"));

					msgLogList.add(requestModel);

				}
			}
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
	public void updateStatusBasedOnMsgId(String msgId, String status) {
		Session session = null;
		Transaction transaction = null;
		NativeQuery<?> query = null;
		String sqlQuery = "update CM_SMSMAIL_MAIL set MailStatus=:status, MailSentDate = GetDate()"
				+ "        where msg_id =:msgId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("status", status);
			query.setParameter("msgId", msgId);
			int k = query.executeUpdate();
			if (k > 0) {
				logger.info("Sms Mail Queue Updated Status For Msg-Id  ", msgId);
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

	@Override
	public void updateStatusForQueue(BigInteger mailitemId, String msgId, String status) {
		Session session = null;
		Transaction transaction = null;
		NativeQuery<?> query = null;
		String sqlQuery = "update CM_SMSMAIL_MAIL set MailStatus=:status, msg_id=:msgId"
				+ "        where mailitem_id =:mailitemId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("status", status);
			query.setParameter("msgId", msgId);
			query.setParameter("mailitemId", mailitemId);
			int k = query.executeUpdate();
			if (k > 0) {
				logger.info("Sms Mail Queue Updated Status For  ", mailitemId);
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

	@Override
	public void updateStatus(BigInteger mailitemId, String status) {
		Session session = null;
		Transaction transaction = null;
		NativeQuery<?> query = null;
		String sqlQuery = "update CM_SMSMAIL_MAIL set MailStatus=:status, MailSentDate = GetDate()"
				+ "        where mailitem_id =:mailitemId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("status", status);
			query.setParameter("mailitemId", mailitemId);
			int k = query.executeUpdate();
			if (k > 0) {
				logger.info("Sms Mail Updated Status For  ", mailitemId);
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

	@Override
	public void updateStatusBasedOnMsgLLogStatus(BigInteger mailitemId, String status, int msgLogStatus) {
		Session session = null;
		Transaction transaction = null;
		NativeQuery<?> query = null;
		String sqlQuery = "update SM set MailStatus =:status, MailSentDate= GetDate() "
				+ "  from CM_SMSMAIL_MAIL SM inner join ADM_MSG_LOG (nolock) ML on SM.msg_id = ML.msg_id and ML.status =:msgLogStatus where SM.mailitem_id =:mailitemId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("status", status);
			query.setParameter("msgLogStatus", msgLogStatus);
			query.setParameter("mailitemId", mailitemId);
			int k = query.executeUpdate();
			if (k > 0) {
				logger.info("Sms Mail Updated Status Based on Msg Log For  ", mailitemId);
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
