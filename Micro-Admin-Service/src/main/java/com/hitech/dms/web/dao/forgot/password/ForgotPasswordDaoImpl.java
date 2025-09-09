/**
 * 
 */
package com.hitech.dms.web.dao.forgot.password;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.hibernate.Criteria;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitech.dms.app.config.publisher.PublishModel;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.app.utils.RandomPasswordGenerator;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.forgot.password.request.ForgotPasswordRequestModel;
import com.hitech.dms.web.model.forgot.password.response.ForgotPasswordResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class ForgotPasswordDaoImpl implements ForgotPasswordDao {
	private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Value("${mail_forgot_password.routing_key}")
	private String routingKey;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private TopicExchange senderForgotPasswordTopicExchange;
	@Autowired
	private CommonUtils commonUtils;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public ForgotPasswordResponseModel resetPassword(ForgotPasswordRequestModel requestModel) {
		Session session = null;
		Transaction transaction = null;
		ForgotPasswordResponseModel responseModel = new ForgotPasswordResponseModel();
		responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
		boolean isSuccess = false;
		BigInteger mailItemId = null;
		String sqlQuery = "exec [ADM_FORGOT_PASS_GEN] :usercode, :userMail, :newPassword, :newEncodedPassword";
		try {
			if (requestModel.getUsercode() == null || requestModel.getUsercode().equals("")) {
				responseModel.setMsg("User Code not Found");
				return responseModel;
			}
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
//			Date currDate = new Date();
			String newPassword = null;
			String newEncodedPassword = null;
			int len = 10;
			int randNumOrigin = 48, randNumBound = 122;
			try {
				newPassword = RandomPasswordGenerator.generateRandomPassword(len, randNumOrigin, randNumBound);
				newEncodedPassword = passwordEncoder.encode(newPassword);
			} catch (Exception ex) {
				logger.error(this.getClass().getName(), ex);
			}
			if (newEncodedPassword == null) {
				newPassword = requestModel.getUsercode() + "@123";
				newEncodedPassword = passwordEncoder.encode(newPassword);
			}

			NativeQuery<?> query = session.createSQLQuery(sqlQuery);
			query.setParameter("usercode", requestModel.getUsercode());
			query.setParameter("userMail", requestModel.getUsermail());
			query.setParameter("newPassword", newPassword);
			query.setParameter("newEncodedPassword", newEncodedPassword);
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
				responseModel.setMsg("User Email is not valid");
			}
			transaction.commit();

		} catch (SQLGrammarException exp) {
			if (transaction != null) {
				transaction.rollback();
			}
			responseModel.setMsg("Please contact System Administrator.");
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), exp);
			responseModel.setMsg("Please contact System Administrator.");
		} finally {
			if (session != null) {
				session.close();
			}
			if (responseModel.getStatusCode() == 200) {
				PublishModel publishModel = new PublishModel();
				publishModel.setId(mailItemId);
				publishModel.setTopic(senderForgotPasswordTopicExchange.getName());
				
				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					// Simulate a long-running Job
					try {
						rabbitTemplate.convertAndSend(senderForgotPasswordTopicExchange.getName(), routingKey,
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
