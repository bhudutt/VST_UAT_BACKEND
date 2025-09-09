/**
 * 
 */
package com.hitech.dms.web.dao.aop.template.submit;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.common.CommonDao;
import com.hitech.dms.web.entity.aop.AopTargetAppEntity;
import com.hitech.dms.web.model.aop.template.submit.request.AopTargetSubmitRequestModel;
import com.hitech.dms.web.model.aop.template.submit.request.AopTargetUpdateDetail;
import com.hitech.dms.web.model.aop.template.submit.request.AopTargetUpdateRequestModel;
import com.hitech.dms.web.model.aop.template.submit.response.AopTargetSubmitResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
@Repository
public class AopTargetSubmitDaoImpl implements AopTargetSubmitDao {
	private static final Logger logger = LoggerFactory.getLogger(AopTargetSubmitDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CommonDao commonDao;

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public AopTargetSubmitResponseModel submitAopTarget(String authorizationHeader, String userCode,
			AopTargetSubmitRequestModel requestModel) {
		logger.debug("submitAopTarget : ");
		logger.debug(userCode + " : " + requestModel.toString());

		Session session = null;
		Transaction transaction = null;
		Query query = null;
		Map<String, Object> mapData = null;
		AopTargetSubmitResponseModel responseModel = new AopTargetSubmitResponseModel();
		String sqlQuery = "exec [SP_SA_MIS_AOP_Upload_TARGET] :userCode, :stgAopId, :stgAopNumber, :aopId";
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				BigInteger userId = (BigInteger) mapData.get("userId");
				query = session.createNativeQuery(sqlQuery);
				query.setParameter("userCode", userCode);
				query.setParameter("stgAopId", requestModel.getStgAopId());
				query.setParameter("stgAopNumber", requestModel.getStgAopNumber());
				query.setParameter("aopId", requestModel.getAopId());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List data = query.list();
				if (data != null && !data.isEmpty()) {
					for (Object object : data) {
						Map row = (Map) object;
	
						responseModel.setAopId((BigInteger) row.get("AopId"));
						responseModel.setAopNumber((String) row.get("AopNumber"));
						responseModel.setMsg((String) row.get("Msg"));
						responseModel.setStatusCode((int) row.get("Status"));
					}
					
					// insert into Approval Table
					//System.out.println("aopid "+responseModel.getAopId());
					if(responseModel.getAopId() !=null) {
						mapData = saveIntoApproval(session, userId, null, responseModel.getAopId());
					}else {
						responseModel.setMsg(responseModel.getMsg());
						responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					}
					
					
					
					if (mapData != null && mapData.get("SUCCESS") != null) {
						transaction.commit();
					} else {
						responseModel.setMsg("Error While Inserting Into AOP Target Approval Hier.");
						responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
					}
				}
			}

		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
			if (responseModel.getMsg() == null) {
				responseModel.setMsg("Error While Uploading Aop Target.");
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}
	
	@SuppressWarnings({ "rawtypes" })
	private Map<String, Object> saveIntoApproval(Session session, BigInteger userId, BigInteger hoUserId,
			BigInteger aopId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("ERROR", "ERROR WHILE INSERTING INTO AOP TARGET APPROVAL TABLE.");
		try {
			List data = commonDao.fetchApprovalData(session, "SA_AOP_TARGET_APPROVAL");
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					Map row = (Map) object;
					AopTargetAppEntity approvalEntity = new AopTargetAppEntity();;
					approvalEntity.setAopId(aopId);
					approvalEntity.setApproverLevelSeq((Integer) row.get("approver_level_seq"));
					approvalEntity.setApprovalStatus((String) row.get("approvalStatus"));
					approvalEntity.setDesignationLevelId((Integer) row.get("designation_level_id"));
					approvalEntity.setGrpSeqNo((Integer) row.get("grp_seq_no"));
					Character isFinalApprovalStatus = (Character) row.get("isFinalApprovalStatus");
					if (isFinalApprovalStatus != null && isFinalApprovalStatus.toString().equals("Y")) {
						approvalEntity.setIsFinalApprovalStatus('Y');
					} else {
						approvalEntity.setIsFinalApprovalStatus('N');
					}
					approvalEntity.setRejectedFlag('N');
					approvalEntity.setHoUserId(null);

					session.save(approvalEntity);
				}
			}
			mapData.put("SUCCESS", "Inserted Into AOP Target Approval Table.");
		} catch (SQLGrammarException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@Override
	public AopTargetSubmitResponseModel updateAopTarget(String authorizationHeader, String userCode,
	        @Valid AopTargetUpdateRequestModel requestModel) {

	    Session session = null;
	    Transaction transaction = null;
	    AopTargetSubmitResponseModel response = new AopTargetSubmitResponseModel();

	    try {
	        session = sessionFactory.openSession();
	        transaction = session.beginTransaction();

	        // Fetch User Details
	        Map<String, Object> mapData = commonDao.fetchUserDTLByUserCode(session, userCode);
	        
	        // Step 1: Update Parent Table
	        String hdrQuery = "UPDATE AopHdrEntity SET remarks = :remark, aopStatus=:aop_status WHERE aop_Id = :aopHrdId";
	        
	        Query hdrUpdate = session.createQuery(hdrQuery);
	        hdrUpdate.setParameter("remark", requestModel.getRemark());
	        hdrUpdate.setParameter("aop_status", "Pending");
	        hdrUpdate.setParameter("aopHrdId", requestModel.getAopHrdId());
	        hdrUpdate.executeUpdate();

	        // Step 2: Update or Insert Child Table
	        for (AopTargetUpdateDetail detail : requestModel.getDetails()) {
	            
//	            String checkExistenceQuery = "SELECT COUNT(*) FROM SA_MIS_AOP_TARGET_DTL WHERE aopDtlId = :aopDtlId";
//	            Query checkQuery = session.createQuery(checkExistenceQuery);
//	            checkQuery.setParameter("aopDtlId", detail.getAopDtlId());
//
//	            Long count = (Long) checkQuery.uniqueResult();
//
//	            if (count != null && count > 0) {
	                // Record Exists - Update it
	                String dtlQuery = "UPDATE AopTargetDtlEntity " +
	                                  "SET month_1 = :month1, month_2 = :month2, month_3 = :month3, month_4 = :month4, " +
	                                  "month_5 = :month5, month_6 = :month6, month_7 = :month7, month_8 = :month8, " +
	                                  "month_9 = :month9, month_10 = :month10, month_11 = :month11, month_12 = :month12 " +
	                                  "WHERE aopDtlId = :aopDtlId";
	                
	                Query dtlUpdate = session.createQuery(dtlQuery);
	                dtlUpdate.setParameter("month1", detail.getMonth1());
	                dtlUpdate.setParameter("month2", detail.getMonth2());
	                dtlUpdate.setParameter("month3", detail.getMonth3());
	                dtlUpdate.setParameter("month4", detail.getMonth4());
	                dtlUpdate.setParameter("month5", detail.getMonth5());
	                dtlUpdate.setParameter("month6", detail.getMonth6());
	                dtlUpdate.setParameter("month7", detail.getMonth7());
	                dtlUpdate.setParameter("month8", detail.getMonth8());
	                dtlUpdate.setParameter("month9", detail.getMonth9());
	                dtlUpdate.setParameter("month10", detail.getMonth10());
	                dtlUpdate.setParameter("month11", detail.getMonth11());
	                dtlUpdate.setParameter("month12", detail.getMonth12());
	                dtlUpdate.setParameter("aopDtlId", detail.getAopDtlId());

	                dtlUpdate.executeUpdate();
//	            } else {
//	                // Record Does Not Exist - Insert New Record
//	                String insertQuery = "INSERT INTO SA_MIS_AOP_TARGET_DTL " +
//	                                     "(aopDtlId, item, itemDesc, machineItemId, model, segment, series, variant, " +
//	                                     "month1, month2, month3, month4, month5, month6, month7, month8, " +
//	                                     "month9, month10, month11, month12) " +
//	                                     "VALUES (:aopDtlId, :item, :itemDesc, :machineItemId, :model, :segment, :series, :variant, " +
//	                                     ":month1, :month2, :month3, :month4, :month5, :month6, :month7, :month8, " +
//	                                     ":month9, :month10, :month11, :month12)";
//	                
//	                Query dtlInsert = session.createQuery(insertQuery);
//	                dtlInsert.setParameter("aopDtlId", detail.getAopDtlId());
//	                dtlInsert.setParameter("item", detail.getItem());
//	                dtlInsert.setParameter("itemDesc", detail.getItemDesc());
//	                dtlInsert.setParameter("machineItemId", detail.getMachineItemId());
//	                dtlInsert.setParameter("model", detail.getModel());
//	                dtlInsert.setParameter("segment", detail.getSegment());
//	                dtlInsert.setParameter("series", detail.getSeries());
//	                dtlInsert.setParameter("variant", detail.getVariant());
//	                dtlInsert.setParameter("month1", detail.getMonth1());
//	                dtlInsert.setParameter("month2", detail.getMonth2());
//	                dtlInsert.setParameter("month3", detail.getMonth3());
//	                dtlInsert.setParameter("month4", detail.getMonth4());
//	                dtlInsert.setParameter("month5", detail.getMonth5());
//	                dtlInsert.setParameter("month6", detail.getMonth6());
//	                dtlInsert.setParameter("month7", detail.getMonth7());
//	                dtlInsert.setParameter("month8", detail.getMonth8());
//	                dtlInsert.setParameter("month9", detail.getMonth9());
//	                dtlInsert.setParameter("month10", detail.getMonth10());
//	                dtlInsert.setParameter("month11", detail.getMonth11());
//	                dtlInsert.setParameter("month12", detail.getMonth12());
//
//	                dtlInsert.executeUpdate();
//	            }
	        }

	        // Commit the transaction
	        transaction.commit();
	        response.setMsg("Update Successful");
	        response.setStatusCode(201);

	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        response.setMsg("Update Failed: " + e.getMessage());
	        response.setStatusCode(500);
	        e.printStackTrace();
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }

	    return response;
	}

}
