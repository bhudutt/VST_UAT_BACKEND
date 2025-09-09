package com.hitech.dms.web.dao.partrequisition.create;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.hitech.dms.web.entity.partrequisition.PartDetailsEntity;
import com.hitech.dms.web.model.partrequisition.create.request.PartListRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.PartListUpdateModel;
import com.hitech.dms.web.model.partrequisition.create.request.RequisitionJobCardListRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.RequisitionPartDetailsRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.RequisitionPartListRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.RequitionJobCardNoByRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.SparePartRequisitionRequestModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionJobCardListResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionPartDetailsResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionPartListResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionTypeListModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionVehicleDetailsResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequitionJobCardNoByResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.SparePartRequestedByEmployeeListResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.SparePartRequisitionResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.SparePartRequisitionTypeResponseModel;

import net.sf.jasperreports.engine.JasperPrint;

@Repository
public class PartRequisitionCreateDaoImpl implements PartRequisitionCreateDao {

private static final Logger logger = LoggerFactory.getLogger(PartRequisitionCreateDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private DocumentNumberGenerator docNumber;
	
	@Override
	public SparePartRequisitionResponseModel createPartRequisition(String userCode,
			SparePartRequisitionRequestModel requestModel, Device device) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("createRequistion invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		Query query = null;
		String msg = null;
		Map<String, Object> mapData = null;
		Map<String, Object> mapData1 = null;
		//List<PartDetailsEntity> getPartdetailsEntity=null;
		SparePartRequisitionResponseModel responseModel = new SparePartRequisitionResponseModel();
		boolean isSuccess = true;
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		String RequisitionNumber=null;
		try {
			BigInteger userId = null;
			BigInteger requisitionId=null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
		       //requestModel.getPartRequisitionEntity().setCreatedBy(userId);
				RequisitionNumber = docNumber.getDocumentNumber("REQ", requestModel.getPartRequisitionEntity().getBranch(), session);
				requestModel.getPartRequisitionEntity().setRequisitionNo(RequisitionNumber);;
				saveRequisitionInCmDocBranch("Part Requisition",requestModel.getPartRequisitionEntity().getBranch(), RequisitionNumber, session);		
		   BigInteger save =(BigInteger) session.save(requestModel.getPartRequisitionEntity());
		   mapData1 = fetchRequisitionforByRequisitionId(session, requestModel.getPartRequisitionEntity().getRequisitionNo());
		   requisitionId = (BigInteger) mapData1.get("requisitionId");
		   System.out.println(requisitionId);
		   List<PartDetailsEntity> list=requestModel.getPartdetailsEntity();
		   for(PartDetailsEntity obj:list) {
			   obj.setRequisitionDtlId(save);
			   obj.setRequisitionId(requisitionId);
			   session.save(obj);
		   }
		   
//		   if(save>0) {
//			   isSuccess=true;
//		   }else {
//				isSuccess = false;
//				responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
//				responseModel.setMsg("some issue during save.");
//			}
		   
		   
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
				mapData = fetchRequisitionByRequisitionId(session, requestModel.getPartRequisitionEntity().getRequisitionId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setRequisitionId(requestModel.getPartRequisitionEntity().getRequisitionId());
					responseModel.setRequisitionNumber(msg);
					 responseModel.setRequisitionNumber((String)mapData.get("RequisitionNumber"));
					responseModel
							.setMsg((String) mapData.get("RequisitionNumber") + " " + "Requisition Number Created Successfully.");
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
	
	private SimpleDateFormat getSimpleDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private void saveRequisitionInCmDocBranch(String documentType, Integer branchID,
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
	public Map<String, Object> fetchRequisitionforByRequisitionId(Session session, String requisitionNo) {
		Map<String, Object> mapData1 = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select Requisition_Id from PA_WK_REQ  where RequisitionNumber =:requisitionNo";
		mapData1.put("ERROR", "Requisition Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("requisitionNo", requisitionNo);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger requisitionId = null;
				for (Object object : data) {
					Map row = (Map) object;
					requisitionId = (BigInteger) row.get("Requisition_Id");
				}
				mapData1.put("requisitionId", requisitionId);
				mapData1.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING REQUISITION DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING REQUISITION DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData1;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchRequisitionByRequisitionId(Session session, BigInteger RequisitionId) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select pr.Requisition_Id Requisition_Id, pr.RequisitionNumber RequisitionNumber from PA_WK_REQ pr where pr.Requisition_Id =:RequisitionId";
		mapData.put("ERROR", "Requisition Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("RequisitionId", RequisitionId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String RequisitionNumber = null;
				for (Object object : data) {
					Map row = (Map) object;
					RequisitionNumber = (String) row.get("RequisitionNumber");
				}
				mapData.put("RequisitionNumber", RequisitionNumber);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING REQUISITION DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING REQUISITION DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
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

	@Override
	public List<SparePartRequisitionTypeResponseModel> fetchServiceTypeList(String userCode, Device device) {
		// TODO Auto-generated method stub
		Session session = null;
		List<SparePartRequisitionTypeResponseModel> responseModelList = null;
		SparePartRequisitionTypeResponseModel responseModel = null;
		Query<SparePartRequisitionTypeResponseModel> query = null;
		
		String sqlQuery = "select Id, Req_Type from PA_WK_REQ_TYPE";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<SparePartRequisitionTypeResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new SparePartRequisitionTypeResponseModel();
					responseModel.setRequisitionId((Integer) row.get("Id"));
					responseModel.setRequisitionType((String) row.get("Req_Type"));
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

	@SuppressWarnings("rawtypes")
	
	public List<SparePartRequestedByEmployeeListResponseModel> fetchRequestedByEmpList(String userCode, Device device) {
		
		Session session = null;
	    List<SparePartRequestedByEmployeeListResponseModel> responseList = null;
	    SparePartRequestedByEmployeeListResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GET_Employee_By_List] :usercode";
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

	@SuppressWarnings("rawtypes")
	@Override
	public List<RequitionJobCardNoByResponseModel> fetchJobCardByDTLList(String userCode,
			RequitionJobCardNoByRequestModel requestModel) {
		
		Session session = null;
	    List<RequitionJobCardNoByResponseModel> responseList = null;
	    RequitionJobCardNoByResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GET_JOBCARD_LIST_By_Search] :RoNumber";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("RoNumber", requestModel.getRoNumber());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<RequitionJobCardNoByResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new RequitionJobCardNoByResponseModel();
					response.setBranchId((Integer) row.get("branch_id"));
					response.setVinId((BigInteger) row.get("Vin_Id"));;
					response.setChassisNo((String) row.get("Chassis_No"));
					response.setModelName((String) row.get("Model_name"));
					response.setRegistrationNumber((String) row.get("Registration_Number"));
					response.setCustomerName((String) row.get("customerName"));
					response.setJobcardDate((Date) row.get("JobcardDate"));
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
	public List<RequisitionJobCardListResponseModel> fetchJobCardList(String userCode,
			RequisitionJobCardListRequestModel ssRequestModel) {
		// TODO Auto-generated method stub
		    
		Session session = null;
	    List<RequisitionJobCardListResponseModel> responseList = null;
	    RequisitionJobCardListResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GET_JOBCARD_LIST] :jobcardNo, :usercode";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("jobcardNo", ssRequestModel.getJobCardNo());
			query.setParameter("usercode", userCode);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<RequisitionJobCardListResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new RequisitionJobCardListResponseModel();
					//response.setJobCardNo((String) row.get("RONumber"));
					//response.setClosingDate((Date) row.get("ClosingDate"));
					response.setVehSrNo((String) row.get("VehSrNo"));
					response.setRoId((BigInteger) row.get("ro_id"));
					response.setOpeningDate((Date) row.get("OpeningDate"));
					response.setStatus((String) row.get("Status"));
					response.setChassisNo((String) row.get("Chassis_No"));
					response.setEngineNo((String) row.get("Engine_No"));
					response.setRONumber((String) row.get("RONumber"));
					response.setRegistrationNumber((String) row.get("Registration_Number"));
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
	public List<RequisitionPartListResponseModel> fetchPartList(String userCode,
			RequisitionPartListRequestModel ssRequestModel) {
		  
		Session session = null;
	    List<RequisitionPartListResponseModel> responseList = null;
	    RequisitionPartListResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SearchPartDetailsrequisition] :Branch_ID, :Criteria, :Searchon, :Division, :PO_Category_Code";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Branch_ID", ssRequestModel.getBranchId());
			query.setParameter("Criteria", ssRequestModel.getCriteria());
			query.setParameter("Searchon", ssRequestModel.getSearchOn());
			query.setParameter("Division", ssRequestModel.getDivision());
			query.setParameter("PO_Category_Code", ssRequestModel.getPoCategoryCode());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<RequisitionPartListResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new RequisitionPartListResponseModel();
					response.setPartBranchId((Integer) row.get("partBranch_id"));
					response.setPartId((Integer) row.get("part_id"));
					response.setPartNo((String) row.get("PartNumber"));
					response.setPartDesc((String) row.get("PartDesc"));
					response.setRequisitionId((BigInteger) row.get("Requisition_Id"));
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
	public List<RequisitionPartDetailsResponseModel> fetchPartDetailsList(String userCode,
			RequisitionPartDetailsRequestModel ssRequestModel) {
		
		Session session = null;
	    List<RequisitionPartDetailsResponseModel> responseList = new ArrayList<RequisitionPartDetailsResponseModel>();
	    RequisitionPartDetailsResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SearchPartDetailsList] :usercode, :PartId, :Roid";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("usercode", userCode);
			query.setParameter("PartId", ssRequestModel.getPartId());
			query.setParameter("Roid", ssRequestModel.getRoId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				for (Object object : data) {
					response = new RequisitionPartDetailsResponseModel();
					Map row = (Map) object;
					BigDecimal MRP = (BigDecimal) row.get("MRP");
					if(MRP!=null &&  MRP.compareTo(BigDecimal.ZERO) > 0 ) {
				
					
					response.setPartBranchId((Integer) row.get("partBranch_id")!=null?(Integer) row.get("partBranch_id"):null);
					response.setPartNo((String) row.get("PartNumber"));					
					response.setPartDesc((String) row.get("PartDesc"));
					response.setUom((String) row.get("UomDesc"));
                    response.setCurrentStock((Integer) row.get("CurrentStock"));
					response.setMarp((BigDecimal) row.get("MRP"));
					response.setIssuedQty((BigDecimal) row.get("IssuedQty"));
					response.setPendingQty((BigDecimal) row.get("Pending_Qty"));
					response.setMsg((String) row.get("msg"));
					responseList.add(response);
					 }else {
						 response.setMsg((String) row.get("msg"));
						 responseList.add(response);
					 }
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
	public List<RequisitionVehicleDetailsResponseModel> fetchVehicleDetailsList(String userCode, String chassisNo) {
		Session session = null;
	    List<RequisitionVehicleDetailsResponseModel> responseList = null;
	    RequisitionVehicleDetailsResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GET_VEHICLEDETAILS] :chassisNo";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("chassisNo", chassisNo);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<RequisitionVehicleDetailsResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new RequisitionVehicleDetailsResponseModel();
					response.setVinId((BigInteger) row.get("vin_id"));
					response.setCustomerId((BigInteger) row.get("customer_id"));
					response.setChassisnNo((String) row.get("chassis_no"));
					response.setRegistrationNumber((String) row.get("registration_number"));
					response.setModelVariant((String) row.get("modelvariant"));
					response.setCustomerName((String) row.get("customerName"));
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
	public List<RequisitionTypeListModel> fetchRequisitionTypeList(String userCode, Integer roId,
			Integer requisitiontype) {
		Session session = null;
	    List<RequisitionTypeListModel> responseList = null;
	    RequisitionTypeListModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_Service_RO_LIST_Check_Point] :Roid, :Requisitiontype";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("Roid", roId);
			query.setParameter("Requisitiontype", requisitiontype);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<RequisitionTypeListModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new RequisitionTypeListModel();
					response.setRoId((BigInteger) row.get("Ro_Id"));
					response.setRequisitionType((Integer) row.get("RequisitionType"));
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
	public SparePartRequisitionResponseModel PartUpdateRequestedQty(String userCode,
			PartListRequestModel requestModel) {
		
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		SparePartRequisitionResponseModel responseModel=new SparePartRequisitionResponseModel();
		Query query = null;
		boolean isFailure = false;
		boolean isSuccess = true;
		try {
			
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			List<PartListUpdateModel> list=requestModel.getPartListUpdateModel();
			if(requestModel.getPartListUpdateModel()!=null)
			{
			 for(PartListUpdateModel obj:list) {
				 
				 String sqlQuery = "exec [SP_GET_PARTUPDATE_REQ_LIST] :PartBranchId, :RequisitionId, :RequestedQty";
					query = session.createSQLQuery(sqlQuery);
					query.setParameter("PartBranchId", obj.getPartBranchId());
					query.setParameter("RequisitionId", obj.getRequisitionId());
					query.setParameter("RequestedQty", obj.getRequestedQty());
					int k = query.executeUpdate();
			 }
			}
			
		if (isSuccess) {
			transaction.commit();
			session.close();
			//sessionFactory.close();
			responseModel.setStatusCode(WebConstants.STATUS_OK_200);
			
			msg = "Request QTY Upadte Successfully";
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

	
	}
  
	
	

