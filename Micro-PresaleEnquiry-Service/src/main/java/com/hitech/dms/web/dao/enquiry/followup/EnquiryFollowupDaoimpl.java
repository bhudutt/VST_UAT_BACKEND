package com.hitech.dms.web.dao.enquiry.followup;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.hitech.dms.app.config.publisher.PublishModel;
import com.hitech.dms.app.utils.CommonUtils;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.enquiry.EnquiryFollowupEntity;
import com.hitech.dms.web.model.enquiry.followup.request.EnquiryFollowupResponse;
import com.hitech.dms.web.model.enquiry.followup.request.FollowupCreateRequestModel;

import reactor.core.publisher.Mono;

@Repository
public class EnquiryFollowupDaoimpl implements EnquiryFollowupDao {
	private static final Logger logger = LoggerFactory.getLogger(EnquiryFollowupDaoimpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Value("${mail_followup_enq.routing_key}")
	private String routingKey;
	//@Autowired
	//private RabbitTemplate rabbitTemplate;
//	@Autowired
//	private TopicExchange senderfollowupEnqTopicExchange;
	@Autowired
	private CommonUtils commonUtils;

	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	public List<EnquiryFollowupResponse> getFollowupDetailsByEnquiryId(Integer enquiryId) {
		Session session = null;
		List<Object> map = null;
		List<EnquiryFollowupResponse> result = null;
		EnquiryFollowupResponse model = null;
		try {
			session = sessionFactory.openSession();
			NativeQuery query = session.createSQLQuery("Exec sp_ENQ_getEnquiryFollowUpDtls :enquiryId");
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			map = query.setParameter("enquiryId", enquiryId).list();
			if (map != null && !map.isEmpty()) {
				result = new ArrayList<>();
				for (Object obj : map) {
					Map<String, Object> m = (Map) obj;
					model = new EnquiryFollowupResponse();
					model.setEnquiryNumber((String) m.get("enquiry_number"));
					model.setEnqStage((String) m.get("enq_stage"));
					model.setEnquiryType((String) m.get("enquiry_type"));
					model.setExpectedPurchaseDate((String) m.get("expected_purchase_date"));
					model.setFollowUpBy((String) m.get("FollowUpBy"));
					model.setFollowUpDate((String) m.get("follow_up_date"));
					model.setNextFlwpActivityPlan((String) m.get("Next_Flwp_Activity_Plan"));
					model.setNextFollowUpDate((String) m.get("next_follow_up_date"));
					model.setRemarks((String) m.get("remarks"));
					model.setUpdationDate((String) m.get("UpdationDate"));
					result.add(model);
				}
				;
			}
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			session.close();
		}
		return result;
	}

	public String createEnquiryFollowup(String userCode, EnquiryFollowupEntity entity) {
		Session session = null;
		Transaction tr = null;
		String result = "Failed";
		try {
			session = sessionFactory.openSession();
			tr = session.beginTransaction();

			BigInteger userId = null;
			String sqlQuery = "Select user_id from ADM_USER (nolock) u where u.UserCode =:userCode";
			userId = (BigInteger) session.createSQLQuery(sqlQuery).setParameter("userCode", userCode).uniqueResult();
			entity.setCreatedBy(userId);
			entity.setCreatedDate(new Date());
			session.save(entity);
			tr.commit();
			result = "Success";
		} catch (Exception ex) {
			if (tr != null && tr.isActive())
				tr.rollback();
			logger.error(this.getClass().getName(), ex);
		} finally {
			session.close();

			try {
				updateEnqMail(userCode, WebConstants.FOLLOWUP_ENQUIRY, entity.getEnquiryHdr().getEnquiryHdrId())
						.subscribe(e -> {
							logger.info(e.toString());
						});
			} catch (Exception exp) {
				logger.error(this.getClass().getName(), exp);
			}
		}
		return result;
	}

	public String updateFollowup(String usercode, FollowupCreateRequestModel followupCreateRequestModel) {
		Session session = null;
		String result = "Failed";
		try {
			session = sessionFactory.openSession();

			BigInteger userId = null;
			String sqlQuery = "Select user_id from ADM_USER (nolock) u where u.UserCode =:userCode";
			userId = (BigInteger) session.createSQLQuery(sqlQuery).setParameter("userCode", usercode).uniqueResult();

			session.createSQLQuery("Exec SP_ENQ_Followup_Update :userid, :enquiry_id, :Followup_Type_id, "
					+ ":Followup_Action, :curr_followup_Date, :Expected_Date_Of_Purchase, :Next_Followup_Date, "
					+ ":Next_Followup_Activity, :enq_stage_id, :Curr_Remarks, :Lost_Drop_Reason, :Lost_Drop_Remarks, :BrandID, :model")
					.setParameter("userid", userId)
					.setParameter("enquiry_id", followupCreateRequestModel.getEnquiryHdr().getEnquiryHdrId())
					.setParameter("Followup_Type_id", followupCreateRequestModel.getFollowupTypeId())
					.setParameter("Followup_Action", followupCreateRequestModel.getFollowupAction())
					.setParameter("curr_followup_Date", followupCreateRequestModel.getCurrentFollowupDate())
					.setParameter("Expected_Date_Of_Purchase", followupCreateRequestModel.getExpectedPurchaseDate())
					.setParameter("Next_Followup_Date", followupCreateRequestModel.getNextFollowUpDate())
					.setParameter("Next_Followup_Activity", followupCreateRequestModel.getNextFollowupActivityId())
					.setParameter("enq_stage_id", followupCreateRequestModel.getEnquiryStageId())
					.setParameter("Curr_Remarks", followupCreateRequestModel.getRemarks())
					.setParameter("Lost_Drop_Reason", followupCreateRequestModel.getLostDropReason())
					.setParameter("Lost_Drop_Remarks", followupCreateRequestModel.getLostDropRemark())
					.setParameter("BrandID", followupCreateRequestModel.getBrandId())
					.setParameter("model", followupCreateRequestModel.getModel()).list();

			result = "Success";

		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			session.close();

			try {
				updateEnqMail(usercode, WebConstants.FOLLOWUP_ENQUIRY,
						followupCreateRequestModel.getEnquiryHdr().getEnquiryHdrId()).subscribe(e -> {
							logger.info(e.toString());
						});
			} catch (Exception exp) {
				logger.error(this.getClass().getName(), exp);
			}
		}
		return result;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	private Mono<Map<String, Object>> updateEnqMail(String userCode, String eventName, BigInteger enqHDRId) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		BigInteger mailItemId = null;
		String sqlQuery = "exec [SP_MAIL_ENQ_FOLLOWUP] :userCode, :eventName, :enqHDRId, :isIncludeActive";
		mapData.put("ERROR", "ERROR WHILE INSERTING ENQUIRY FOLLOWUP MAIL TRIGGERS..");
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
			mapData.put("ERROR", "ERROR WHILE INSERTING ENQUIRY FOLLOWUP MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING ENQUIRY FOLLOWUP MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (session != null) {
				session.close();
			}
			if (mailItemId != null && mailItemId.compareTo(BigInteger.ZERO) > 0) {
				PublishModel publishModel = new PublishModel();
				publishModel.setId(mailItemId);
				//publishModel.setTopic(senderfollowupEnqTopicExchange.getName());
				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
					// Simulate a long-running Job
					try {
//						rabbitTemplate.convertAndSend(senderfollowupEnqTopicExchange.getName(), routingKey,
//								commonUtils.objToJson(publishModel).toString());
//						logger.info("Published message for followup enquiry '{}'", publishModel.toString());

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
