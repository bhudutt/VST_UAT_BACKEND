package com.hitech.dms.web.dao.partreturn.create;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.doc.generator.DocumentNumberGenerator;
import com.hitech.dms.web.entity.partrequisition.PartReturnDTLEntity;
import com.hitech.dms.web.model.partreturn.create.request.PartReturnCreateRequestModel;
import com.hitech.dms.web.model.partreturn.create.request.PartReturnQtyRequestModel;
import com.hitech.dms.web.model.partreturn.create.request.PartReturnQtyUpdateModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnCreateResponseModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnIssueDetailsResponseModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnIssueSearchResponseModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnJobCardSearchResponseModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnReasionTypeResponseModel;
import com.hitech.dms.web.model.partreturn.response.RestrictSpareReturnListModel;

@Repository
public class PartReturnDaoImpl implements PartReturnDao{

	private static final Logger logger = LoggerFactory.getLogger(PartReturnDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private DocumentNumberGenerator docNumber;
	
	@Override
	public PartReturnCreateResponseModel createPartReturn(String userCode, Device device,
			PartReturnCreateRequestModel requestModel) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("createPartReturn invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		String msg = null;
		Map<String, Object> mapData = null;
		PartReturnCreateResponseModel responseModel= new PartReturnCreateResponseModel();
		boolean isSuccess = true;
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		String ReturnNumber=null;
		
		try {
			String createdBy = null;
			BigInteger returnId=null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				createdBy = (String) mapData.get("createdBy");
		       requestModel.getPartReturnHDREntity().setCreatedBy(createdBy);
			ReturnNumber = docNumber.getDocumentNumber("RET", requestModel.getPartReturnHDREntity().getBranch(), session);
			requestModel.getPartReturnHDREntity().setReturnNumber(ReturnNumber);
			saveReturnPartInCmDocBranch("Part Return",requestModel.getPartReturnHDREntity().getBranch(), ReturnNumber, session);		
		   BigInteger save =(BigInteger) session.save(requestModel.getPartReturnHDREntity());
		   mapData = fetchReturnNumberforByRequisitionId(session, requestModel.getPartReturnHDREntity().getReturnNumber());
		   returnId = (BigInteger) mapData.get("returnId");
		   System.out.println(returnId);
		   List<PartReturnDTLEntity> list=requestModel.getPartReturnDTLEntity();
		   for(PartReturnDTLEntity obj:list) {
			   obj.setReturnDtlId(save);
			   obj.setReturnId(returnId);
			   obj.setCreatedBy(createdBy);
			   session.save(obj);
		   }
	  
			} else {
				isSuccess = false;
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
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
				// insert
				mapData = fetchReturnPartByReturnId(session, requestModel.getPartReturnHDREntity().getReturnId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setReturnId(requestModel.getPartReturnHDREntity().getReturnId());
					responseModel.setReturnNumber(msg);
					 responseModel.setReturnNumber((String)mapData.get("ReturnNumber"));
					responseModel
							.setMsg((String) mapData.get("ReturnNumber") + " " + "Part Return Created Successfully.");
					if (mapData != null && mapData.get("status") != null) {
						logger.info(mapData.toString());
					}
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				}
			} else {
				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			}
			if (session != null) {
				session.close();
			}
		}
		return responseModel;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select CreatedBy from ADM_USER (nolock) u where u.UserCode =:userCode";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String createdBy = null;
				for (Object object : data) {
					Map row = (Map) object;
					createdBy = (String) row.get("CreatedBy");
				}
				mapData.put("createdBy", createdBy);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING USER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {

		}
		return mapData;
	}
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private void saveReturnPartInCmDocBranch(String documentType, Integer branchID,
			String RequisitionNumber, Session session) {
		Query query = null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		String currentDate = null;
		currentDate=formatter.format(new Date());
		String sqlQuery = "exec [Update_INV_Doc_No] :LastDocumentNumber, :DocumentTypeDesc,'"+currentDate +"',:BranchID";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("LastDocumentNumber", RequisitionNumber);
		query.setParameter("DocumentTypeDesc", documentType);
		query.setParameter("BranchID", branchID);
//		query.setParameter("DocumentType", documentType);

		int k = query.executeUpdate();

	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchReturnNumberforByRequisitionId(Session session, String returnNumber) {
		Map<String, Object> mapData1 = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select Wk_Return_Id from PA_WK_RETURN  where ReturnNumber =:returnNumber";
		mapData1.put("ERROR", "Return Part Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("returnNumber", returnNumber);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger returnId = null;
				for (Object object : data) {
					Map row = (Map) object;
					returnId = (BigInteger) row.get("Wk_Return_Id");
				}
				mapData1.put("returnId", returnId);
				mapData1.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING RETURN PART DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING RETURN PART DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData1;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchReturnPartByReturnId(Session session, BigInteger ReturnId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select rp.Wk_Return_Id Wk_Return_Id, rp.ReturnNumber ReturnNumber from PA_WK_RETURN RP where rp.Wk_Return_Id =:ReturnId";
		mapData.put("ERROR", "Requisition Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("ReturnId", ReturnId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String ReturnNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					ReturnNumber = (String) row.get("ReturnNumber");
				}
				mapData.put("ReturnNumber", ReturnNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING RETURN NUMBER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING RETURN NUMBER DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@Override
	public List<PartReturnReasionTypeResponseModel> fetchReasonType(String userCode, Device device) {
		Session session = null;
		List<PartReturnReasionTypeResponseModel> responseModelList = null;
		PartReturnReasionTypeResponseModel responseModel = null;
		Query<PartReturnReasionTypeResponseModel> query = null;
		
		String sqlQuery = "select delay_reason_id, delay_reason_desc from SV_REASON_TYPE";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<PartReturnReasionTypeResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new PartReturnReasionTypeResponseModel();
					responseModel.setReasonId((Integer) row.get("delay_reason_id"));
					responseModel.setReasonType((String) row.get("delay_reason_desc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	@Override
	public List<PartReturnJobCardSearchResponseModel> fetchJobCardWorkshopType(String userCode, Device device,Integer searchId, Integer branchId) {
		
		Session session = null;
		List<PartReturnJobCardSearchResponseModel> responseList = null;
		PartReturnJobCardSearchResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GET_JOBCARD_Search_By_PartReturn] :SearchId, :BranchId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("SearchId", searchId);
			query.setParameter("BranchId", branchId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<PartReturnJobCardSearchResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new PartReturnJobCardSearchResponseModel();
					response.setBranchstoreId((Integer) row.get("branch_store_id"));
					response.setIssueId((BigInteger) row.get("issue_id"));
					response.setJobCardNo((String) row.get("JobCardNo"));
					response.setJobCardDate((String) row.get("JobCarddate"));
					response.setBranchId((Integer) row.get("branch_id"));
					response.setRoId((BigInteger) row.get("ro_id"));
					response.setStatus((String) row.get("Status"));
					response.setChassisNo((String) row.get("chassis_no"));
					response.setRegistrationNumber((String) row.get("registration_number"));
					response.setModelVariant((String) row.get("model_name"));
					response.setFirstname((String) row.get("firstname"));
					responseList.add(response);
					
				}
			}
		 
		}catch (SQLGrammarException sqlge) {
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
		List<PartReturnJobCardSearchResponseModel> uniqueList = responseList.stream()
                .collect(Collectors.toMap(PartReturnJobCardSearchResponseModel::getJobCardNo, model -> model, (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());
		return uniqueList;
	}


	@Override
	public List<PartReturnIssueSearchResponseModel> fetchIssueSearchList(String userCode, Device device,Integer searchId, Integer branchId) {
		Session session = null;
		List<PartReturnIssueSearchResponseModel> responseList = null;
		PartReturnIssueSearchResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GET_IssueNo_Search_By_PartReturn] :SearchId, :BranchId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("SearchId", searchId);
			query.setParameter("BranchId", branchId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<PartReturnIssueSearchResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new PartReturnIssueSearchResponseModel();
					response.setBranchstoreId((Integer) row.get("branch_store_id"));
					response.setIssueNo((String) row.get("IssueNumber"));
					response.setIssuedate((String) row.get("IssueDate"));
					response.setIssueId((BigInteger) row.get("issue_Id"));
					response.setRoId((BigInteger) row.get("ro_id"));
					response.setStatus((String) row.get("status"));
					response.setChassisNo((String) row.get("chassis_no"));
					response.setRegistrationNumber((String) row.get("registration_number"));
					response.setModelVariant((String) row.get("model_name"));
					response.setFirstname((String) row.get("firstname"));
					responseList.add(response);
				}
			}
		 
		}catch (SQLGrammarException sqlge) {
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
		return responseList;
	}

	@Override
	public List<PartReturnIssueDetailsResponseModel> fetchPartReturnDetailsList(String userCode, Device device, Integer issueId,Integer roId) {
		Session session = null;
		List<PartReturnIssueDetailsResponseModel> responseList = null;
		PartReturnIssueDetailsResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [sp_get_pa_Return_part_detail] :IssueId, :RoId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("IssueId", issueId);
			query.setParameter("RoId", roId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<PartReturnIssueDetailsResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new PartReturnIssueDetailsResponseModel();
					response.setIssueDtlId((BigInteger) row.get("issue_dtl_id"));
					response.setStockBinId((BigInteger) row.get("stock_bin_id"));
					response.setPartBranchId((Integer) row.get("PartBranch_Id"));
					response.setBranchStoreId((Integer) row.get("branch_store_id"));
					response.setIssueId((BigInteger) row.get("issue_id"));
					response.setIssuenumber((String) row.get("Issuenumber"));
					response.setPartNumber((String) row.get("partnumber"));
					response.setPartdesc((String) row.get("partdesc"));
					response.setPendingQty((BigDecimal) row.get("BeforeIssue_PendingQty"));
					response.setRequestQty((BigDecimal) row.get("requestQty"));
					response.setIssuedqty((BigDecimal) row.get("issuedqty"));
					response.setFromstore((String) row.get("fromstore"));
					response.setBinlocation((String) row.get("binlocation"));
					response.setReturnRemark((String) row.get("ReturnRemark"));
					response.setUnitPrice((BigDecimal) row.get("mrp"));
					response.setStockStoreId((BigInteger) row.get("stock_store_id"));
					response.setPartId((Integer) row.get("PART_ID"));
					response.setRequisitionId((Integer) row.get("Requisition_id"));
					responseList.add(response);
				}
			}
		 
		}catch (SQLGrammarException sqlge) {
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
		
		
		return responseList;
	}

	@Override
	public PartReturnCreateResponseModel fetchPartReturnQtyUpdate(String userCode, Device device,
			PartReturnQtyRequestModel partReturnRequestModel) {
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		PartReturnCreateResponseModel responseModel=new PartReturnCreateResponseModel();
		Query query = null;
		boolean isFailure = false;
		boolean isSuccess = true;
        try {
			
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			List<PartReturnQtyUpdateModel>list=partReturnRequestModel.getPartReturnQtyUpdateModel();
			if(partReturnRequestModel.getPartReturnQtyUpdateModel()!=null)
			{
			for(PartReturnQtyUpdateModel obj:list)
			{
				String sqlQuery = "exec [SP_RETURN_BIN_UPDATE_STOCK] :Qty, :stockbinid, :partbranchid, :stockstoreid, :partid, :branchId, :basicUnitPrice, :modifiedBy, :branch_store_id, :issueId, :requisitionId";
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("Qty", obj.getReturnQty());
				query.setParameter("stockbinid", obj.getStockBinId());
				query.setParameter("partbranchid", obj.getPartBranchId());
				query.setParameter("stockstoreid", obj.getStockStoreId());
				query.setParameter("partid", obj.getPartId());
				query.setParameter("branchId", obj.getBranchId());
				query.setParameter("basicUnitPrice", obj.getBasicUnitPrice());
				query.setParameter("modifiedBy", userCode);
				query.setParameter("branch_store_id", obj.getBranchStoreId());
				query.setParameter("issueId", obj.getIssuedId());
				query.setParameter("requisitionId", obj.getRequisitionId());
				int k = query.executeUpdate();
			 }
			}
			
		if (isSuccess) {
			transaction.commit();
			session.close();
			//sessionFactory.close();
			responseModel.setStatusCode(WebConstants.STATUS_OK_200);
			
			msg = "Stock Return Quantity Upadte Successfully";
			responseModel.setMsg(msg);
		}else {
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			msg = "Bad Request";
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
	
     }
    return responseModel;
	}

	@Override
	public List<RestrictSpareReturnListModel> fetchRestrictSpareReturnList(String userCode, Device device, Integer roId,
			String flag) {
		
		Session session = null;
		List<RestrictSpareReturnListModel> responseList = null;
		RestrictSpareReturnListModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_Restrict_Spare_Return] :RoId, :FLAG";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("RoId", roId);
			query.setParameter("FLAG", flag);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<RestrictSpareReturnListModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new RestrictSpareReturnListModel();
					response.setStatus((String) row.get("msg"));
					responseList.add(response);
				}
			}
		 
		}catch (SQLGrammarException sqlge) {
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
		return responseList;
	}

	
}
