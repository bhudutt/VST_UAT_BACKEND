package com.hitech.dms.web.dao.partrequisition.issue.create;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
import com.hitech.dms.web.entity.partrequisition.SparePartIssueDTLEntity;
import com.hitech.dms.web.model.partrequisition.issue.create.request.SpareParSearchtJobCardRequestModel;
import com.hitech.dms.web.model.partrequisition.issue.create.request.SparePartIssueUpdateSingleBinModel;
import com.hitech.dms.web.model.partrequisition.issue.create.request.SparePartIssueUpdateSingleBinRequestModel;
import com.hitech.dms.web.model.partrequisition.issue.create.request.SparePartIssueUpdateStockRequestModel;
import com.hitech.dms.web.model.partrequisition.issue.create.request.SparePartIssueUpdateStockUpdateModel;
import com.hitech.dms.web.model.partrequisition.issue.create.request.SparePartRequisitionIssueRequestModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.PartRequisitionDetailsResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.PartRequisitionNumberResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SpareParSearchtJobCardResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartDetailResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartIssureAvailableStockResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartRequestedByEmployeeListResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartRequisitionIssueResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartRequisitionIssueTypeResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartRequisitionNoSearchResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartSearchtDTLJobCardResponseModel;
	

@Repository
public class PartRequisitionIssueCreateDaoImpl implements PartRequisitionIssueCreateDao{

private static final Logger logger = LoggerFactory.getLogger(PartRequisitionIssueCreateDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;	
	@Autowired
	private DocumentNumberGenerator docNumber;
	
	@Override
	public SparePartRequisitionIssueResponseModel createPartIssue(SparePartRequisitionIssueRequestModel requestModel, String userCode,
			Device device) {
		
		
		if (logger.isDebugEnabled()) {
			logger.debug("createStore invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		Map<String, Object> mapData = null;
		Map<String, Object> mapData1 = null;
		SparePartRequisitionIssueResponseModel responseModel=new SparePartRequisitionIssueResponseModel();
		String IssueNumber = null;
		boolean isSuccess = true;
		try {
			BigInteger userId = null;
			BigInteger issueId=null;
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
			IssueNumber = docNumber.getDocumentNumber("RIS", requestModel.getSparePartIssueEntity().getBranchId(), session);
		    requestModel.getSparePartIssueEntity().setIssueNumber(IssueNumber);
		    saveIssueNumberInCmDocBranch("Part Issue",requestModel.getSparePartIssueEntity().getBranchId(), IssueNumber, session);
		    BigInteger save =(BigInteger)session.save(requestModel.getSparePartIssueEntity());
			 mapData1 = fetchIssueByissueId(session, requestModel.getSparePartIssueEntity().getIssueNumber());
			 issueId = (BigInteger) mapData1.get("issueId");
			 System.out.println(issueId);
			 if(save!=null) {
				 responseModel = fetchUpdateSingleBinStockQty(session,userCode,  requestModel.getSingleBinModel());
			 }
			
				if(save!=null && responseModel!=null && responseModel.getStatusCode()!=null && responseModel.getStatusCode()==200) {
					transaction.commit();
				}else {
					transaction.rollback();
				}
			}
			if (isSuccess) {
				mapData = fetchIssueNoByIssueId(session, requestModel.getSparePartIssueEntity().getIssueId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setIssueId(requestModel.getSparePartIssueEntity().getIssueId());
					responseModel.setIssueNumber(msg);
					responseModel.setIssueNumber((String)mapData.get("IssueNumber"));
					responseModel
							.setMsg((String) mapData.get("IssueNumber") + " " + "Part Issue Created Successfully.");
					if (mapData != null && mapData.get("status") != null) {
						logger.info(mapData.toString());
					}
					responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				}
				session.close();
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
	
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select user_id from ADM_USER (nolock) u where u.UserCode =:userCode";
		mapData.put("ERROR", "USER DETAILS NOT FOUND");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger userId = null;
				for (Object object : data) {
					Map row = (Map) object;
					userId = (BigInteger) row.get("user_id");
				}
				mapData.put("userId", userId);
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
	
	private void saveIssueNumberInCmDocBranch(String documentType, Integer branchId, String issueNumber,
			Session session) {
		
		Query query = null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		String currentDate = null;
		currentDate=formatter.format(new Date());
		String sqlQuery = "exec [Update_INV_Doc_No] :LastDocumentNumber, :DocumentTypeDesc,'"+currentDate +"',:BranchID";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("LastDocumentNumber", issueNumber);
		query.setParameter("DocumentTypeDesc", documentType);
		query.setParameter("BranchID", branchId);
//		query.setParameter("DocumentType", documentType);

		int k = query.executeUpdate();
		
	}

	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchIssueByissueId(Session session, String issueNumber) {
		Map<String, Object> mapData1 = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select issue_id from pa_stock_issue  where  IssueNumber=:issueNumber";
		mapData1.put("ERROR", "ISSUE Details Not Found");
		
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("issueNumber", issueNumber);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger issueId = null;
				for (Object object : data) {
					Map row = (Map) object;
					issueId = (BigInteger) row.get("issue_id");
					
				}
				mapData1.put("issueId", issueId);
				mapData1.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData1;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchIssueNoByIssueId(Session session, BigInteger issueId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select pi.issue_id issue_id, pi.IssueNumber IssueNumber from PA_STOCK_ISSUE pi where pi.issue_id =:issueId";
		mapData.put("ERROR", "Issue Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("issueId", issueId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String IssueNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					IssueNumber = (String) row.get("IssueNumber");
				}
				mapData.put("IssueNumber", IssueNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING ISSUE DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<SparePartRequisitionIssueTypeResponseModel> fetchIssueType(String userCode, Device device) {
		
		Session session = null;
		List<SparePartRequisitionIssueTypeResponseModel> responseModelList = null;
		SparePartRequisitionIssueTypeResponseModel responseModel = null;
		Query<SparePartRequisitionIssueTypeResponseModel> query = null;
		
		String sqlQuery = "select Id, IssueType, IsActive from PA_Issue_Type";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SparePartRequisitionIssueTypeResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new SparePartRequisitionIssueTypeResponseModel();
					responseModel.setId((Integer) row.get("Id"));
					responseModel.setIssueType((String) row.get("IssueType"));
					responseModel.setIsActive((Character) row.get("IsActive"));
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
	public List<PartRequisitionNumberResponseModel> fetchPartRequisitionNo(String userCode, Device device,
			Integer roId) {
		
		Session session = null;
	    List<PartRequisitionNumberResponseModel> responseList = null;
	    PartRequisitionNumberResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [sp_get_RequisitionNumber] :RoId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("RoId", roId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<PartRequisitionNumberResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new PartRequisitionNumberResponseModel();
				    response.setRequisitionId((BigInteger) row.get("Requisition_Id"));
				    response.setRequisitionNo((String) row.get("RequisitionNumber"));
				    response.setRequisitionDate((Date) row.get("Requisition_Date"));
					responseList.add(response);
					
					}
				}
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
			return responseList;
	}

	@Override
	public List<SparePartRequisitionNoSearchResponseModel> fetchRequisitionByDTLList(String userCode, Device device,
			Integer requisitionId) {
		
		Session session = null;
	    List<SparePartRequisitionNoSearchResponseModel> responseList = null;
	    SparePartRequisitionNoSearchResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [sp_get_RequisitionNumber_Details] :RequisitionId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("RequisitionId", requisitionId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SparePartRequisitionNoSearchResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SparePartRequisitionNoSearchResponseModel();
					response.setRequisitionNo((String) row.get("ROnumber"));
					response.setJobCardDate((Date) row.get("CreatedDate"));
					response.setChassisNo((String) row.get("chassis_no"));
					response.setRegistrationNumber((String) row.get("registration_number"));
					response.setModelName((String) row.get("model_name"));
					response.setFirstname((String) row.get("firstname"));
					responseList.add(response);
					
					}
				}
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
			return responseList;
	 }

	@Override
	public List<SparePartDetailResponseModel> fetchPartDetailsList(String userCode, Device device, Integer requisitionId) {
		
		Session session = null;
	    List<SparePartDetailResponseModel> responseList = null;
	    SparePartDetailResponseModel response = null;
	    List list = new ArrayList<>();
	    NativeQuery<?> query = null;
		String sqlQuery = "exec [sp_get_pa_Requisition_part_detail] :RequisitionId, :UserCode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("RequisitionId",requisitionId);
			query.setParameter("UserCode",userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SparePartDetailResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SparePartDetailResponseModel();
					response.setPartRequisitionNo((String) row.get("requisitionnumber"));
					response.setPartNo((String) row.get("partnumber"));
					response.setPartDesc((String) row.get("partdesc"));
					response.setPendingReQty((BigDecimal) row.get("pendingrequestedqty"));
					response.setRequestedQty((BigDecimal) row.get("requestqty"));
					response.setIssuedQty((BigDecimal) row.get("issuedqty"));
					response.setTotalBranchStock((Integer) row.get("totalbranchstock"));
					//response.setIssueQty((BigDecimal) row.get("currentstock"));
					response.setMrp((BigDecimal) row.get("mrp"));
					//response.setCurrentStock((BigDecimal) row.get("currentstock"));
					response.setFromStore((String) row.get("fromstore"));
					//response.setBinLocation((Integer) row.get("binlocation"));
					//response.setStockBinid((BigInteger) row.get("stock_bin_id"));
					if((Integer) row.get("partbranch_id")!=null) {
					response.setRefpartbranchId((Integer) row.get("partbranch_id"));
					}
					response.setRequisitionId((BigInteger) row.get("Requisition_Id"));
					response.setBinLocationName((String) row.get("binlocationName"));
//					list.add((String) row.get("binlocationName"));
//					Map<String,Long> countingMap = (Map<String, Long>) list.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting())); 
//					boolean anyDuplicate = countingMap.entrySet().stream().anyMatch(entry -> entry.getValue() > 1); System.out.println("Are there any duplicates? " + anyDuplicate);
//					response.setBinFlag(anyDuplicate);
					responseList.add(response);
					
					}
				    

				}
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
			return responseList;
	}

	@Override
	public List<SpareParSearchtJobCardResponseModel> fetchjobCardlist(String userCode, Device device,
			SpareParSearchtJobCardRequestModel requestModel) {
		
		Session session = null;
	    List<SpareParSearchtJobCardResponseModel> responseList = null;
	    SpareParSearchtJobCardResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [sp_get_jobcard_search] :SearchId,:BranchId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("SearchId", requestModel.getSearchIssueTypeId());
			query.setParameter("BranchId", requestModel.getBranchId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SpareParSearchtJobCardResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SpareParSearchtJobCardResponseModel();
					response.setRequisitionId((BigInteger) row.get("Requisition_Id"));
					response.setJobCardNumber((String) row.get("RONumber"));
					response.setJobCardId((BigInteger) row.get("ro_id"));
					response.setRequisitionNo((String) row.get("RequisitionNumber"));
					response.setStatus((String) row.get("status"));
					response.setRequisitionStatus((String) row.get("RequisitionStatus"));
					responseList.add(response);
					
					}
				}
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
			return responseList;
	  }


	@Override
	public List<SparePartSearchtDTLJobCardResponseModel> fetchjobCardDTLlist(String userCode, Device device,
			Integer roId, String flag,Integer requisitionId) {
		Session session = null;
	    List<SparePartSearchtDTLJobCardResponseModel> responseList = null;
	    SparePartSearchtDTLJobCardResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [sp_get_job_card_details] :RoId, :flag, :RequisitionId";
		try {
			
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("RoId", roId);
			query.setParameter("flag", flag);
			query.setParameter("RequisitionId", requisitionId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SparePartSearchtDTLJobCardResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SparePartSearchtDTLJobCardResponseModel();
					response.setRequisitionId((BigInteger) row.get("Requisition_Id"));
					response.setJobCardNo((String) row.get("ROnumber"));
					response.setJobcarddate((String) row.get("CreatedDate"));
					response.setChassisNo((String) row.get("chassis_no"));
					response.setRegistrationNumber((String) row.get("registration_number"));
					response.setModelVariant((String) row.get("model_name"));
					response.setFirstname((String) row.get("firstname"));
					response.setRequisitionNo((String) row.get("RequisitionNumber"));
					response.setRequisitionDate((String) row.get("Requisition_Date"));
					responseList.add(response);
					
					}
				}
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
			return responseList;
	  
	}


	@Override
	public List<PartRequisitionDetailsResponseModel> fetchRequisitionDTLlist(String userCode, Device device,
			Integer roId, String flag,Integer requisitionId) {
		Session session = null;
	    List<PartRequisitionDetailsResponseModel> responseList = null;
	    PartRequisitionDetailsResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [sp_get_job_card_details] :RoId, :flag, :RequisitionId";
		try {
			
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("RoId",roId);
			query.setParameter("flag", flag);
			query.setParameter("RequisitionId", requisitionId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<PartRequisitionDetailsResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new PartRequisitionDetailsResponseModel();
					response.setRequisitionId((BigInteger) row.get("Requisition_Id"));
					response.setPartRequisitionNo((String) row.get("RequisitionNumber"));
					response.setPartRequisitionDate((String) row.get("Requisition_Date"));
					response.setChassisNo((String) row.get("chassis_no"));
					response.setRegistrationNumber((String) row.get("registration_number"));
					response.setModelVariant((String) row.get("model_name"));
					response.setFirstname((String) row.get("firstname"));
					responseList.add(response);
					
					}
				}
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
			return responseList;
	}


	@Override
	public List<SparePartIssureAvailableStockResponseModel> fetchAvailableStockList(String userCode, Device device,
			BigInteger requisitionId,String partBranchId) {
		
		Session session = null;
	    List<SparePartIssureAvailableStockResponseModel> responseList = null;
	    SparePartIssureAvailableStockResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [sp_get_spares_PriceWiseAvailableStock] :RequisitionId,:PartbranchId, :UserCode";
		try {
			
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("RequisitionId", requisitionId);	
			query.setParameter("PartbranchId", partBranchId!=null? new BigInteger(partBranchId):null);	
			query.setParameter("UserCode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SparePartIssureAvailableStockResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new SparePartIssureAvailableStockResponseModel();
					response.setAvailableQty((BigDecimal) row.get("availableQuantity"));
					response.setStockBinid((BigInteger) row.get("stock_bin_id"));
					response.setStoreId((Integer) row.get("storeId"));
					response.setIssueId((BigInteger) row.get("issue_id"));				
					response.setPartBranchId((Integer) row.get("partBranchId"));
					response.setCurrentStock((BigDecimal) row.get("currentstock"));
					response.setStoreName((String) row.get("storeName"));
					response.setBinId((BigInteger) row.get("binId"));
					response.setBinLocation((String) row.get("binLocation"));
					response.setMrpPrice((BigDecimal) row.get("MRPPrice"));
					response.setCgst((BigDecimal) row.get("CGST"));
					response.setSgst((BigDecimal) row.get("SGST"));
					response.setPartNumber((String) row.get("partnumber"));
					response.setPartDescription((String) row.get("partdesc"));
					response.setRequestedQty((BigDecimal) row.get("requestqty"));
					response.setPendingQty((BigDecimal) row.get("pendingrequestedqty"));
					response.setTotalBranchStock((Integer) row.get("totalbranchstock"));
					response.setRequisitionId((BigInteger) row.get("requisition_id"));
					response.setRequisitionDtlId((BigInteger) row.get("Requisition_Dtl_Id"));
					response.setStockStoreId((Integer) row.get("stock_store_id"));
					response.setPartId((Integer) row.get("part_id"));
					response.setBranchId((Integer) row.get("branch_id"));
					responseList.add(response);
					
					}
				}
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
			return responseList;
	}


	@Override
	public SparePartRequisitionIssueResponseModel fetchUpdateStockQuantity(String userCode,
			SparePartIssueUpdateStockRequestModel requestModel) {
		
			Session session = null;
			Transaction transaction = null;
			String msg = null;
			SparePartRequisitionIssueResponseModel responseModel=new SparePartRequisitionIssueResponseModel();
			
			Query query = null;
			boolean isFailure = false;
			boolean isSuccess = true;
			try {
				
				session = sessionFactory.openSession();
				transaction = session.beginTransaction();
				
				 List<SparePartIssueUpdateStockUpdateModel> list=requestModel.getSparePartIssueUpdateStockUpdateModel();
				System.out.println(list);
				 for(SparePartIssueUpdateStockUpdateModel obj:list) {
					 String sqlQuery = "exec [SP_GET_StockQuantity_Update] :partBranchId, :availableStock, :issueQty, :pendingQty, :BeforePendingQty,:binId";
						query = session.createSQLQuery(sqlQuery);
					    query.setParameter("partBranchId", obj.getPartBranchId());
		       			query.setParameter("availableStock", obj.getAvailableStock());
		       			query.setParameter("issueQty", obj.getIssueQty());
		       			query.setParameter("pendingQty", obj.getPendingQty());
		       			query.setParameter("BeforePendingQty", obj.getBeforePendingQty());
		       			query.setParameter("binId", obj.getBinId());
						int k = query.executeUpdate();
				 }
       			
				
			if (isSuccess) {
				transaction.commit();
				session.close();
				//sessionFactory.close();
				responseModel.setStatusCode(WebConstants.STATUS_OK_200);
				
				msg = "Stock Quantity Upadte Successfully";
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


	
	private SparePartRequisitionIssueResponseModel fetchUpdateSingleBinStockQty(Session session, String userCode,
			List<SparePartIssueUpdateSingleBinModel> list) {
		
		//Session session = null;
		Transaction transaction = null;
		String msg = null;
		SparePartRequisitionIssueResponseModel responseModel=new SparePartRequisitionIssueResponseModel();
		
		Query query = null;
		boolean isFailure = false;
		boolean isSuccess = true;
		try {
			
			//session = sessionFactory.openSession();
			//transaction = session.beginTransaction();
			
			if(list!=null && !list.isEmpty())
			{
			 for(SparePartIssueUpdateSingleBinModel obj:list) {
				 
				 String sqlQuery = "exec [SP_ISSUE_BIN_UPDATE_STOCK] :Qty, :stockbinid, :partbranchid, :stockstoreid, :partid,:branchId, :basicUnitPrice, :modifiedBy, :branch_store_id, :requisitionId, :issueId, :requisitionDTLId, :RequestedQty";
					query = session.createSQLQuery(sqlQuery);
					query.setParameter("Qty", obj.getQuantity());
					query.setParameter("stockbinid", obj.getStockBinId());
				    query.setParameter("partbranchid", obj.getPartBranchId());
				    query.setParameter("stockstoreid", obj.getStockStoreId());
				    query.setParameter("partid", obj.getPartId());
				    query.setParameter("branchId", obj.getBranchId());
				    query.setParameter("basicUnitPrice", obj.getBasicUnitPrice());
				    query.setParameter("modifiedBy", userCode);
				    query.setParameter("branch_store_id", obj.getBranchstoreId());
				    query.setParameter("requisitionId", obj.getRequisitionId());
				    query.setParameter("issueId", obj.getIssueId());
				    query.setParameter("requisitionDTLId", obj.getRequisitionDTLId());
				    query.setParameter("RequestedQty", obj.getRequestedQty());
					int k = query.executeUpdate();
			 }
			}
			
		if (isSuccess) {
		//	transaction.commit();			session.close();
			//sessionFactory.close();
			responseModel.setStatusCode(WebConstants.STATUS_OK_200);
			
			msg = "Stock Quantity Upadte Successfully";
			responseModel.setMsg(msg);
		}else {
			//transaction.rollback();
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
		} 
     return responseModel;
	}


	@Override
	public List<SparePartRequestedByEmployeeListResponseModel> fetchRequestedByEmpList(String userCode, Device device) {
		
		Session session = null;
	    List<SparePartRequestedByEmployeeListResponseModel> responseList = null;
	    SparePartRequestedByEmployeeListResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GET_Employee_IssueBy_List] :usercode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("usercode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<SparePartRequestedByEmployeeListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					response = new SparePartRequestedByEmployeeListResponseModel();
					response.setEmpId((BigInteger) row.get("emp_id"));
					response.setDealerId((BigInteger) row.get("parent_dealer_id"));
					response.setCustomerName((String) row.get("EMP_Name"));
					response.setBranchId((BigInteger) row.get("branch_id"));
					//responseModel.setEmpCode((String) row.get("EmpCode"));
					responseList.add(response);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		}finally {
			if (session.isOpen())
				session.close();
		}

		return responseList;

	}


	@Override
	public SparePartRequisitionIssueResponseModel fetchUpdateSingleBinStockQty(String userCode,
			SparePartIssueUpdateSingleBinRequestModel requestModel) {
		Session session = null;
		Transaction transaction = null;
		boolean isSuccess =true;
		SparePartRequisitionIssueResponseModel responseModel = null;
	
		try {
		 session = sessionFactory.openSession();
		 transaction = session.beginTransaction();
		 responseModel = fetchUpdateSingleBinStockQty(session, userCode,  requestModel.getSparePartIssueUpdateSingleBinModel());
	  if(responseModel!=null && responseModel.getStatusCode()!=null && responseModel.getStatusCode()==200) {
			transaction.commit();
		}else {
			transaction.rollback();
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
	
	}

