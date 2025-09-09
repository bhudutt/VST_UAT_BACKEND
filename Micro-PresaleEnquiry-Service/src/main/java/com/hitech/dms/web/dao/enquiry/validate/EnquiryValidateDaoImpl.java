package com.hitech.dms.web.dao.enquiry.validate;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.config.publisher.PublishModel;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.enquiry.validate.response.EnquiryValidateResponseModel;

import reactor.core.publisher.Mono;

@Repository
public class EnquiryValidateDaoImpl implements EnquiryValidateDao {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryValidateDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Value("${mail_create_enq.routing_key}")
	private String routingKey;
	//@Autowired
	//private RabbitTemplate rabbitTemplate;
//	@Autowired
//	private TopicExchange senderValidatedEnqTopicExchange;
	@Autowired
	private CommonUtils commonUtils;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public EnquiryValidateResponseModel validateEnquiry(String userCode, BigInteger enquiryId, String validationStatus,
			Date validationDate, String remarks) {
		Session session = null;
		EnquiryValidateResponseModel model = null;
		try {
			session = sessionFactory.openSession();
			NativeQuery query = session.createSQLQuery(
					"Exec SP_ENQ_validateEnquiry :userCode, :enquiryId, :validationStatus, :validationDate, :remarks");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			Object obj = query.setParameter("userCode", userCode).setParameter("enquiryId", enquiryId)
					.setParameter("validationStatus", validationStatus).setParameter("validationDate", validationDate)
					.setParameter("remarks", remarks).uniqueResult();
			if (obj != null) {
				Map map = (Map) obj;
				model = new EnquiryValidateResponseModel();
				model.setMsg((String) map.get("msg"));
				model.setSuccessFlag((String) map.get("successFlag"));
			}
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			session.close();
			try {
				updateEnqMail(userCode, WebConstants.VALIDATED_ENQUIRY, enquiryId).subscribe(e -> {
					logger.info(e.toString());
				});
			} catch (Exception exp) {
				logger.error(this.getClass().getName(), exp);
			}
		}
		return model;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	private Mono<Map<String, Object>> updateEnqMail(String userCode, String eventName, BigInteger enqHDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		BigInteger mailItemId = null;
		String sqlQuery = "exec [SP_MAIL_ENQ_VALIDATE] :userCode, :eventName, :enqHDRId, :isIncludeActive";
		mapData.put("ERROR", "ERROR WHILE INSERTING VALIDATED ENQUIRY MAIL TRIGGERS..");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("eventName", eventName);
			query.setParameter("enqHDRId", enqHDRId);
			query.setParameter("isIncludeActive", "N");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String msg = null;
				for (Object object : data) {
					Map row = (Map) object;
					msg = (String) row.get("msg");
					mailItemId = (BigInteger) row.get("mailItemId");
				}
				mapData.put("msg", msg);
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING VALIDATED ENQUIRY MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING VALIDATED ENQUIRY MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (mailItemId != null && mailItemId.compareTo(BigInteger.ZERO) > 0) {
				PublishModel publishModel = new PublishModel();
				publishModel.setId(mailItemId);
				//publishModel.setTopic(senderValidatedEnqTopicExchange.getName());
				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					// Simulate a long-running Job
					try {
//						rabbitTemplate.convertAndSend(senderValidatedEnqTopicExchange.getName(), routingKey,
//								commonUtils.objToJson(publishModel).toString());
//						logger.info("Published message for validated enquiry '{}'", publishModel.toString());

					} catch (Exception exp) {
						logger.error(this.getClass().getName(), exp);
					}
					System.out.println("I'll run in a separate thread than the main thread.");
				});
			}
		}
		return Mono.just(mapData);
	}

}
