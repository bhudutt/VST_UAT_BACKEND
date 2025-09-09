/**
 * 
 */
package com.hitech.dms.web.dao.forgot.password;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.config.publisher.PublishModel;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.user.UserEntity;
import com.hitech.dms.web.model.forgot.password.request.ChangePasswordRequestModel;
import com.hitech.dms.web.model.forgot.password.response.ChangePasswordResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class PasswordDaoImp implements PasswordDao {
	private static final Logger logger = LoggerFactory.getLogger(PasswordDaoImp.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Value("${mail_change_password.routing_key}")
	private String routingKey;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private TopicExchange senderChangePasswordTopicExchange;
	@Autowired
	private CommonUtils commonUtils;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public ChangePasswordResponseModel changePassword(ChangePasswordRequestModel requestModel) {
		Session session = null;
		UserEntity userEntity = null;
		Transaction transaction = null;
		ChangePasswordResponseModel responseModel = new ChangePasswordResponseModel();
		boolean isSuccess = false;
		responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		String decryptedPassword = null;
		BigInteger mailItemId = null;
		String hqlQuery = "from UserEntity where userCode =:userCode";
		try {
			// Current Password
			String oldPassword = requestModel.getPassword();
			String newPassword = requestModel.getNewPassword();
			if (newPassword == null || newPassword.equals("") || oldPassword == null || oldPassword.equals("")) {
				responseModel.setMsg("Please contact System Administrator.");

			} else {
				session = sessionFactory.openSession();
				transaction = session.beginTransaction();
				String decryptedOldPassword = new String(java.util.Base64.getDecoder().decode(oldPassword));
				// new Password
				decryptedPassword = new String(java.util.Base64.getDecoder().decode(newPassword));
				Query query = session.createQuery(hqlQuery);
				query.setParameter("userCode", requestModel.getUserCode());
				userEntity = (UserEntity) query.uniqueResult();
				if (userEntity != null) {
					if (passwordEncoder.matches(decryptedOldPassword, userEntity.getPassword())) {
						String encoded = passwordEncoder.encode(decryptedPassword);
						userEntity.setPassword(encoded);
						userEntity.setLastPasswordResetDate(new Date());
						session.merge(userEntity);

						transaction.commit();
						isSuccess = true;
						responseModel.setMsg("Password Has Been Changed Successfully.");
					} else {
						responseModel.setMsg("Not Able To Change Password.");
					}
				} else {
					responseModel.setMsg("Please contact System Administrator.");
				}
			}
		} catch (SQLGrammarException exp) {
			if (transaction != null) {
				transaction.rollback();
			}
			responseModel.setMsg("Please contact System Administrator.");
			exp.printStackTrace();
		} catch (IllegalArgumentException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			responseModel.setMsg("Please contact System Administrator.");
			ex.printStackTrace();
		} catch (Exception exp) {
			if (transaction != null) {
				transaction.rollback();
			}
			exp.printStackTrace();
			responseModel.setMsg("Please contact System Administrator.");
		} finally {
			if (isSuccess) {
				hqlQuery = "exec [ADM_CHNG_PASS_GEN] :usercode, :newPassword";
				NativeQuery<?> query = session.createSQLQuery(hqlQuery);
				query.setParameter("usercode", userEntity.getUsername());
				query.setParameter("newPassword", decryptedPassword);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						Map row = (Map) object;
						responseModel.setMsg((String) row.get("msg"));
						responseModel.setStatusCode((Integer) row.get("statuscode"));
						mailItemId = (BigInteger) row.get("mailItemId");
					}
				} else {
					responseModel.setMsg("Please contact System Administrator.");
				}
			}
			if (session != null) {
				session.close();
			}
			if (responseModel.getStatusCode() == 200) {
				PublishModel publishModel = new PublishModel();
				publishModel.setId(mailItemId);
				publishModel.setTopic(senderChangePasswordTopicExchange.getName());
				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					// Simulate a long-running Job
					try {
						rabbitTemplate.convertAndSend(senderChangePasswordTopicExchange.getName(), routingKey,
								commonUtils.objToJson(publishModel).toString());
						logger.info("Published message for Forgot Password '{}'", publishModel.toString());

					} catch (Exception exp) {
						logger.error(this.getClass().getName(), exp);
					}
					System.out.println("I'll run in a separate thread than the main thread.");
				});
			}
		}
		return responseModel;
	}
}
