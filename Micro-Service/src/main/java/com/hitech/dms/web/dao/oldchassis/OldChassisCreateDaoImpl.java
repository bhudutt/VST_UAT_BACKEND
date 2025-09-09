package com.hitech.dms.web.dao.oldchassis;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
import org.springframework.web.client.RestTemplate;

import com.hitech.dms.app.config.publisher.PublishModel;
import com.hitech.dms.constants.WebConstants;

import com.hitech.dms.web.model.oldchassis.OldChassisCreateRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisCreateResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisCustomerDTLByCustIDRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisCustomerDTLByCustIDResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisCustomerDTLByMobileRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisCustomerDTLByMobileResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisFetchDTLRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisFetchDTLResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisItemDescriptionRequestModel;
//import com.hitech.dms.web.model.oldchassis.OldChassisMachineDTLByChassisRequestModel;
//import com.hitech.dms.web.model.oldchassis.OldChassisMachineDTLByChassisResponseModel;
//import com.hitech.dms.web.model.oldchassis.OldChassisMachineDTLByVinIDRequestModel;
//import com.hitech.dms.web.model.oldchassis.OldChassisMachineDTLByVinIDResponseModel;
//import com.hitech.dms.web.model.oldchassis.OldChassisModelItemListRequestModel;
//import com.hitech.dms.web.model.oldchassis.OldChassisModelItemListResponseModel;
//import com.hitech.dms.web.model.oldchassis.OldChassisModelListRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisModelListResponseModel;
import com.hitech.dms.web.model.oldchassis.OldChassisNumberRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisNumberResponseModel;
//import com.hitech.dms.web.model.oldchassis.OldChassisSegmentListRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisSegmentListResponseModel;
//import com.hitech.dms.web.model.oldchassis.OldChassisSeriesListRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisSeriesListResponseModel;
//import com.hitech.dms.web.model.oldchassis.OldChassisVariantListRequestModel;
import com.hitech.dms.web.model.oldchassis.OldChassisVariantListResponseModel;
import com.hitech.dms.web.model.oldchassis.OldchassisItemDescriptionResponseModel;
import com.hitech.dms.web.model.oldchassis.PoPlantRequstModel;
import com.hitech.dms.web.model.oldchassis.PoPlantResponseModel;
import com.hitech.dms.web.model.oldchassis.list.response.OldchassisListResponseModel;
import com.hitech.dms.web.model.oldchassis.reponse.OldChassisEngVinRegDTLResponseModel;
import com.hitech.dms.web.model.oldchassis.reponse.OldChassisMachineItemIdDetailModel;
import com.hitech.dms.web.model.oldchassis.request.OldChassisEngVinRegDTLRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingModelDTLResponseModel;

import reactor.core.publisher.Mono;


@Repository
public class OldChassisCreateDaoImpl implements OldChassisCreateDao{

	private static final Logger logger = LoggerFactory.getLogger(OldChassisCreateDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private RestTemplate restTemplate;
	
	//CustomerHDREntity customerHDREntity,
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public OldChassisCreateResponseModel createOldChassis(String userCode,
			OldChassisCreateRequestModel requestModel, Device device) {
		  
		if (logger.isDebugEnabled()) {
			logger.debug("createChassis invoked.." + userCode);
			logger.debug(requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		Map<String, Object> mapData = null;
		Map<String, Object> mapData1 = null;
		Map<String, Object> mapData2 = null;
		OldChassisCreateResponseModel responseModel = new OldChassisCreateResponseModel();	
		Query query = null;
		boolean isSuccess = true;
		boolean isFailure = false;
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		String HDR="NO";
		try {
			BigInteger userId = null;
			BigInteger  customermstId =null;
			BigInteger  customermstIds =null;
			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				
				 String sqlQuery = "exec [SP_CM_Customer_DetailsByMobileNo] :mobileNo";
				 query = session.createSQLQuery(sqlQuery);
					query.setParameter("mobileNo",requestModel.getCustomerHDREntity().getMobileNo());
					query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
					List data = query.list();
					if (data != null && !data.isEmpty()) {
						HDR="YES";
					}
			
				if(HDR.equalsIgnoreCase("YES")) {
					   mapData1 = fetchcustByCustomerId(session, requestModel.getCustomerHDREntity().getMobileNo());
					   customermstId = (BigInteger) mapData1.get("customermstId");
					    System.out.println(customermstId);
					    requestModel.getCustomerItemDTLEntity().setCustomermstId(customermstId); 
					   requestModel.getChassisMachineVinMstEntity().setCreatedBy(userId);
					   session.save(requestModel.getCustomerItemDTLEntity());
					   session.save(requestModel.getChassisMachineVinMstEntity());
					   session.save(requestModel.getCustomerDTLEntity()); 
					   
				}else {
					
					    requestModel.getCustomerHDREntity().setCreatedBy(userId);
					    session.save(requestModel.getCustomerHDREntity());
					    
					  
				}
				   
					
					mapData1 = fetchcustByCustomerId(session, requestModel.getCustomerHDREntity().getMobileNo());
				    mapData2 = fetchcustDTLByCustomerId(session, requestModel.getCustomerHDREntity().getMobileNo());
				    customermstId = (BigInteger) mapData1.get("customermstId");
				    System.out.println(customermstId);
				    customermstIds = (BigInteger) mapData2.get("customermstIds");
				    System.out.println(customermstIds);
				    
				   
				   requestModel.getCustomerItemDTLEntity().setCustomermstId(customermstId);     
				   requestModel.getChassisMachineVinMstEntity().setCustomermstId(customermstId);
				   requestModel.getChassisMachineVinMstEntity().setCreatedBy(userId);
				   requestModel.getChassisMachineVinMstEntity().setOldchassisFlag(1);// for identify old Chassis 
				   session.save(requestModel.getChassisMachineVinMstEntity());
				   requestModel.getCustomerDTLEntity().setCustomermstIds(customermstIds);
				   session.save(requestModel.getCustomerItemDTLEntity());
				   session.save(requestModel.getCustomerDTLEntity());  
			}
 
		if (isSuccess) {
			transaction.commit();
			session.close();
			responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
			msg = "Old Chassis Created Successfully";
			responseModel.setMsg(msg);
		}else if(isFailure)
		{
			responseModel.setStatusCode(WebConstants.STATUS_INTERNAL_SERVER_ERROR_500);
			msg= "Old Chassis not found";
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
			query = session.createSQLQuery(sqlQuery);
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
	
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchcustByCustomerId(Session session, String mobileNo) {
		Map<String, Object> mapData1 = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select Customer_Id from CM_CUST_hdr  where  Mobile_No=:mobileNo";
		mapData1.put("ERROR", "Customer Details Not Found");
		
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("mobileNo", mobileNo);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger customermstId = null;
				for (Object object : data) {
					Map row = (Map) object;
					customermstId = (BigInteger) row.get("Customer_Id");
					
				}
				mapData1.put("customermstId", customermstId);
				mapData1.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING Customer DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData1.put("ERROR", "ERROR WHILE FTECHING Customer DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData1;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchcustDTLByCustomerId(Session session, String mobileNo) {
		Map<String, Object> mapData2 = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select Customer_Id from CM_CUST_hdr  where  Mobile_No=:mobileNo";
		mapData2.put("ERROR", "Customer Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("mobileNo", mobileNo);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				BigInteger customermstIds = null;
				for (Object object : data) {
					Map row = (Map) object;
					customermstIds = (BigInteger) row.get("Customer_Id");
				}
				mapData2.put("customermstIds", customermstIds);
				mapData2.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData2.put("ERROR", "ERROR WHILE FTECHING Customer DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData2.put("ERROR", "ERROR WHILE FTECHING Customer DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData2;
	}
	
	@Override
	public List<OldChassisSeriesListResponseModel> fetchSeriesList(String userCode, Device device, Integer pcId) {
		Session session = null;
		List<OldChassisSeriesListResponseModel> responseModelList = null;
		OldChassisSeriesListResponseModel responseModel = null;
		Query<OldChassisSeriesListResponseModel> query = null;
		String sqlQuery = "select DISTINCT concat (series_name,' ',model_name)as series_name, pc_id, model_id from CM_MST_MODEL where pc_id=:pcId";
		try {
			
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("pcId", pcId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OldChassisSeriesListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new OldChassisSeriesListResponseModel();
					responseModel.setModel((BigInteger) row.get("model_id"));
					responseModel.setSeriesName((String) row.get("series_name"));
					responseModel.setPcId((Integer) row.get("pc_id"));
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
	public List<OldChassisSegmentListResponseModel> fetchSegmentList(String userCode,Device device, BigInteger modelId) {
		Session session = null;
		List<OldChassisSegmentListResponseModel> responseModelList = null;
		OldChassisSegmentListResponseModel responseModel = null;
		Query<OldChassisSegmentListResponseModel> query = null;
		String sqlQuery = "select DISTINCT segment_name, model_id from CM_MST_MODEL where model_id=:modelId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("modelId", modelId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OldChassisSegmentListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new OldChassisSegmentListResponseModel();
					responseModel.setModelid((BigInteger) row.get("model_id"));
					responseModel.setSegmentName((String) row.get("segment_name"));
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
	public List<OldChassisModelListResponseModel> fetchMachineItemByList(String userCode, Device device, Integer machineItemId) {
		
		Session session = null;
		List<OldChassisModelListResponseModel> responseModelList = null;
		OldChassisModelListResponseModel responseModel = null;
		Query<OldChassisModelListResponseModel> query = null;
		String sqlQuery = "exec [SP_GET_ITEMBY_ID] :MachineItemId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("MachineItemId", machineItemId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OldChassisModelListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new OldChassisModelListResponseModel();
					responseModel.setItemDescription((String) row.get("item_description"));
					responseModel.setSeries((String) row.get("series_name"));
					responseModel.setSegment((String) row.get("segment_name"));
					responseModel.setVariant((String) row.get("variant"));
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
	public List<OldChassisVariantListResponseModel> fetchVariantList(String userCode, Device device, BigInteger modelId) {
		
		Session session = null;
		List<OldChassisVariantListResponseModel> responseModelList = null;
		OldChassisVariantListResponseModel responseModel = null;
		Query<OldChassisVariantListResponseModel> query = null;
		String sqlQuery = "select DISTINCT ms.machine_item_id, ms.item_no, ms.item_description,mm.series_name, mm.segment_name, ms.variant from CM_MST_MACHINE_ITEM ms INNER JOIN CM_MST_MODEL(NOLOCK) mm on mm.model_id=ms.model_id\r\n"
				+ "  where ms.model_id=:modelId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("modelId", modelId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OldChassisVariantListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new OldChassisVariantListResponseModel();
					responseModel.setMachineitemID((BigInteger) row.get("machine_item_id"));
					responseModel.setVariant((String) row.get("variant"));
					responseModel.setItem((String) row.get("item_no"));
					responseModel.setItemDescription((String) row.get("item_description"));
					responseModel.setSeries((String) row.get("series_name"));
					responseModel.setSegment((String) row.get("segment_name"));
					responseModel.setVariant((String) row.get("variant"));
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
	public List<ServiceBookingModelDTLResponseModel> fetchModelAllList(String userCode,Device device
			) {
		
		Session session = null;
		List<ServiceBookingModelDTLResponseModel> responseModelList = null;
		ServiceBookingModelDTLResponseModel responseModel = null;
		Query<OldChassisVariantListResponseModel> query = null;
		String sqlQuery = "EXEC [SP_GET_MODEL_LIST]";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ServiceBookingModelDTLResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ServiceBookingModelDTLResponseModel();
					responseModel.setModelId((BigInteger) row.get("model_id"));
					responseModel.setModelName((String) row.get("Model_name"));
					responseModel.setMachineItemId((BigInteger) row.get("machine_item_id"));
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
	public List<OldChassisMachineItemIdDetailModel> fetchModelDetailsAllList(String userCode, Device device,
			BigInteger machineItemId) {
		
		Session session = null;
		List<OldChassisMachineItemIdDetailModel> responseModelList = null;
		OldChassisMachineItemIdDetailModel responseModel = null;
		Query<OldChassisVariantListResponseModel> query = null;
		String sqlQuery = "EXEC [SP_GET_DetailsByModelId] :MachineItemId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("MachineItemId", machineItemId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OldChassisMachineItemIdDetailModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new OldChassisMachineItemIdDetailModel();
					responseModel.setSeries((String) row.get("series_name"));
					responseModel.setSegment((String) row.get("segment_name"));
					responseModel.setVariant((String) row.get("variant"));
					responseModel.setItemNo((String) row.get("item_no"));
					responseModel.setItemDesc((String) row.get("item_description"));
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<OldChassisCustomerDTLByMobileResponseModel> fetchCustomerDTLByMobileNo(String userCode, String mobileNumber,
			String isFor, Long dealerId) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCustomerDTLByMobileNo invoked.." + mobileNumber + " " + isFor);
		}
		Session session = null;
		Query query = null;
		List<OldChassisCustomerDTLByMobileResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_CustomerAuto_ByMobile] :mobileNumber, :userCode, :isFor, :dealerID";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("mobileNumber", mobileNumber);
			query.setParameter("isFor", isFor);
			query.setParameter("dealerId", dealerId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OldChassisCustomerDTLByMobileResponseModel>();
				OldChassisCustomerDTLByMobileResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new OldChassisCustomerDTLByMobileResponseModel();
					responseModel.setCustomerId((BigInteger) row.get("customer_id"));
					responseModel.setCustomerCode((String) row.get("customerCode"));
					responseModel.setDisplayValue((String) row.get("displayValue"));
					responseModel.setProspectType((String) row.get("ProspectType"));
					responseModel.setMobileNo((String) row.get("Mobile_No"));
					responseModel.setErrorFlag((String) row.get("error_flag"));
					responseModel.setErrorMsg((String) row.get("error_msg"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<OldChassisCustomerDTLByMobileResponseModel> fetchCustomerDTLByMobileNo(
			OldChassisCustomerDTLByMobileRequestModel customerDTLByMobileRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCustomerDTLByMobileNo invoked.." + customerDTLByMobileRequestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<OldChassisCustomerDTLByMobileResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_CustomerAuto_ByMobile] :mobileNumber, :userCode, :isFor, :dealerID";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", customerDTLByMobileRequestModel.getUserCode());
			query.setParameter("mobileNumber", customerDTLByMobileRequestModel.getMobileNumber());
			query.setParameter("isFor", customerDTLByMobileRequestModel.getIsFor());
			query.setParameter("dealerID", customerDTLByMobileRequestModel.getDealerID());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OldChassisCustomerDTLByMobileResponseModel>();
				OldChassisCustomerDTLByMobileResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new OldChassisCustomerDTLByMobileResponseModel();
					responseModel.setCustomerId((BigInteger) row.get("customer_id"));
					responseModel.setCustomerCode((String) row.get("customerCode"));
					responseModel.setDisplayValue((String) row.get("displayValue"));
					responseModel.setProspectType((String) row.get("ProspectType"));
					responseModel.setMobileNo((String) row.get("Mobile_No"));
					responseModel.setErrorFlag((String) row.get("error_flag"));
					responseModel.setErrorMsg((String) row.get("error_msg"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<OldChassisCustomerDTLByCustIDResponseModel> fetchCustomerDTLByCustID(String userCode, Long custID) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCustomerDTLByCustID invoked.." + custID);
		}
		Session session = null;
		Query query = null;
		List<OldChassisCustomerDTLByCustIDResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_Customer_Details] :custID";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("custID", custID);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OldChassisCustomerDTLByCustIDResponseModel>();
				OldChassisCustomerDTLByCustIDResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new OldChassisCustomerDTLByCustIDResponseModel();
					responseModel.setCustomerId((BigInteger) row.get("customer_id"));
					responseModel.setCustomerCode((String) row.get("customerCode"));
					responseModel.setAddress1((String) row.get("address1"));
					responseModel.setAddress2((String) row.get("address2"));
					responseModel.setAddress3((String) row.get("address3"));
					responseModel.setAlternateNumber((String) row.get("alternate_no"));
					responseModel.setCityDesc((String) row.get("citydesc"));
					responseModel.setCityID((BigInteger) row.get("city_id"));
					responseModel.setCountryDesc((String) row.get("countryDesc"));
					responseModel.setCountryID((BigInteger) row.get("country_id"));
					responseModel.setCustomerCategoryID((BigInteger) row.get("CustomerCategory_Id"));
					responseModel.setDistrictDesc((String) row.get("districtDesc"));
					responseModel.setDistrictId((BigInteger) row.get("district_id"));
					responseModel.setEmailId((String) row.get("Email_id"));
					responseModel.setFirstName((String) row.get("Firstname"));
					responseModel.setLastName((String) row.get("lastname"));
					responseModel.setMiddleName((String) row.get("Middlename"));
					responseModel.setMobileNumber((String) row.get("mobile_no"));
					responseModel.setPanNo((String) row.get("PAN_NO"));
					responseModel.setPinCode((String) row.get("pincode"));
					responseModel.setPinID((BigInteger) row.get("pin_id"));
					responseModel.setStateDesc((String) row.get("stateDesc"));
					responseModel.setStateID((BigInteger) row.get("state_id"));
					responseModel.setTehsilDesc((String) row.get("TehsilDesc"));
					responseModel.setTehsilId((BigInteger) row.get("tehsil_id"));
					responseModel.setWatsupNumber((String) row.get("WhatsappNo"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<OldChassisCustomerDTLByCustIDResponseModel> fetchCustomerDTLByCustID(String userCode,
			OldChassisCustomerDTLByCustIDRequestModel customerDTLByCustIDRequestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchCustomerDTLByCustID invoked.." + customerDTLByCustIDRequestModel.toString());
		}
		Session session = null;
		Query query = null;
		List<OldChassisCustomerDTLByCustIDResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_CM_Customer_Details] :custID";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("custID", customerDTLByCustIDRequestModel.getCustomerID());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OldChassisCustomerDTLByCustIDResponseModel>();
				OldChassisCustomerDTLByCustIDResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new OldChassisCustomerDTLByCustIDResponseModel();
					responseModel.setCustomerId((BigInteger) row.get("customer_id"));
					responseModel.setCustomerCode((String) row.get("customerCode"));
					responseModel.setAddress1((String) row.get("address1"));
					responseModel.setAddress2((String) row.get("address2"));
					responseModel.setAddress3((String) row.get("address3"));
					responseModel.setAlternateNumber((String) row.get("alternate_no"));
					responseModel.setCityDesc((String) row.get("citydesc"));
					responseModel.setCityID((BigInteger) row.get("city_id"));
					responseModel.setCountryDesc((String) row.get("countryDesc"));
					responseModel.setCountryID((BigInteger) row.get("country_id"));
					responseModel.setCustomerCategoryID((BigInteger) row.get("CustomerCategory_Id"));
					responseModel.setDistrictDesc((String) row.get("districtDesc"));
					responseModel.setDistrictId((BigInteger) row.get("district_id"));
					responseModel.setEmailId((String) row.get("Email_id"));
					responseModel.setFirstName((String) row.get("Firstname"));
					responseModel.setLastName((String) row.get("lastname"));
					responseModel.setMiddleName((String) row.get("Middlename"));
					responseModel.setMobileNumber((String) row.get("mobile_no"));
					responseModel.setPanNo((String) row.get("PAN_NO"));
					responseModel.setAadharcardNo((String) row.get("AADHAR_CARD_NO"));
					responseModel.setPinCode((String) row.get("pincode"));
					responseModel.setPinID((BigInteger) row.get("pin_id"));
					responseModel.setStateDesc((String) row.get("stateDesc"));
					responseModel.setStateID((BigInteger) row.get("state_id"));
					responseModel.setTehsilDesc((String) row.get("TehsilDesc"));
					responseModel.setTehsilId((BigInteger) row.get("tehsil_id"));
					responseModel.setWatsupNumber((String) row.get("WhatsappNo"));
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public List<OldchassisItemDescriptionResponseModel> fetchOldChassisItemDTLList(String userCode,
			OldChassisItemDescriptionRequestModel ssModel) {
		
		Session session = null;
		List<OldchassisItemDescriptionResponseModel> responseList = null;
		OldchassisItemDescriptionResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SERVICE_GETITEMNO] :itemno";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			//query.setParameter("userCode", userCode);
			query.setParameter("itemno", ssModel.getItemno());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<OldchassisItemDescriptionResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new OldchassisItemDescriptionResponseModel();
					response.setItemId((BigInteger) row.get("machine_item_id"));
					response.setItemNumber((String) row.get("item_no"));
					response.setItemDescription((String) row.get("item_description"));
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
	public List<OldChassisNumberResponseModel> fetchOldChassisDTLList(String userCode,
			OldChassisNumberRequestModel ssModel) {
	
		Session session = null;
		List<OldChassisNumberResponseModel> responseList = null;
		OldChassisNumberResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SERVICE_GETCHASSIS_DTL] :vinid";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			//query.setParameter("userCode", userCode);
			query.setParameter("vinid", ssModel.getVinid());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<OldChassisNumberResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new OldChassisNumberResponseModel();
					response.setVinId((BigInteger) row.get("vin_id"));
					response.setMachineItemId((BigInteger) row.get("machine_item_id"));
					response.setChassisNo((String) row.get("chassis_no"));
					response.setEngineNo((String) row.get("engine_no"));
					response.setVinNo((String) row.get("vin_no"));
					response.setRegistrationNumber((String) row.get("registration_number"));
					response.setItemDescription((String) row.get("item_description"));
					response.setStatus((String) row.get("status"));
					response.setBookingNo((String) row.get("bookingno"));
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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public List<OldChassisFetchDTLResponseModel> fetchOldChassisMultiDTLList(String userCode,
			OldChassisFetchDTLRequestModel ssModel) {
			
		    Session session = null;
		    List<OldChassisFetchDTLResponseModel> responseList = null;
		    OldChassisFetchDTLResponseModel response = null;
			NativeQuery<?> query = null;
			String sqlQuery = "exec [SP_SA_GET_CHASSIS_LIST] :chassisNo";
			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("chassisNo", ssModel.getChassisNo());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List<?> data = query.list();
				if (data != null && !data.isEmpty()) {
					responseList = new ArrayList<OldChassisFetchDTLResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						response = new OldChassisFetchDTLResponseModel();
						response.setMachineItemId((BigInteger) row.get("machine_item_id"));
						response.setChassisNo((String) row.get("chassis_no"));
						response.setVinNo((String) row.get("vin_no"));
						response.setEngineNo((String)row.get("engine_no"));
						response.setRegisterationNumber((String)row.get("registration_number"));
						response.setSaleDate((Date) row.get("Sale_Date"));
						response.setSegmentName((String) row.get("segment_name"));
						response.setSeriesName((String) row.get("series_name"));
						response.setModelName((String) row.get("model_name"));
						response.setProfitDesc((String) row.get("pc_desc"));
						response.setVariant((String) row.get("variant"));
						response.setItemNo((String) row.get("item_no"));
						response.setItemDesc((String) row.get("item_description"));
						response.setMobileNo((String) row.get("Mobile_No"));
						response.setFirstName((String) row.get("FirstName"));
						response.setMiddleName((String) row.get("MiddleName"));
						response.setLastName((String) row.get("LASTNAME"));
						response.setWhatsappNo((String) row.get("WhatsappNo"));
						response.setAlternateNo((String) row.get("Alternate_No"));
						response.setPanNo((String) row.get("PAN_NO"));
						response.setEmailId((String) row.get("Email_id"));
						response.setAadharCardNo((String) row.get("AADHAR_CARD_NO"));
						response.setAddress1((String) row.get("address1"));
						response.setAddress2((String) row.get("address2"));
						response.setAddress3((String) row.get("address3"));
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
	public List<OldchassisListResponseModel> fetchOldchassisList(String userCode) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOldchassisList invoked.." + userCode);
		}
		Session session = null;
		
		List<OldchassisListResponseModel> responseModelList = null;
		try {
			session = sessionFactory.openSession();
			// pc_id, brand_id
			responseModelList = fetchOldchassisList(session, userCode);
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<OldchassisListResponseModel> fetchOldchassisList(Session session, String userCode
			) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchOldchassisList invoked.." + userCode + " ");
			
		}
		Query query = null;
		List<OldchassisListResponseModel> responseModelList = null;
		String sqlQuery = "Select VIN.chassis_no, VIN.engine_no, VIN.vin_no, VIN.registration_number, VIN.Sale_Date, \r\n"
				+ "ADM.CreatedBy, VIN.created_date, CM.Mobile_No, ISNULL(CM.firstname, '')+' '+ISNULL(CM.middlename, '')+' '+ ISNULL(CM.lastname,'')  \r\n"
				+ "customerName, CM.WhatsappNo, CM.Alternate_No, CM.Email_id, CM.PAN_NO, CM.AADHAR_CARD_NO, CM.address1,CM.address2, CM.address3, CM.CustomerCategory_Id, \r\n"
				+ "CM.customerCode, CM.prospectType  \r\n"
				+ "from SA_MACHINE_VIN_MASTER VIN \r\n"
				+ "inner join CM_CUST_HDR CM ON VIN.original_customer_master_id=CM.Customer_Id \r\n"
				+ "inner join ADM_USER ADM ON ADM.CreatedBy=CM.CreatedBy";
		try {
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<OldchassisListResponseModel>();
				OldchassisListResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new OldchassisListResponseModel();
				    
					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			exp.printStackTrace();
		} catch (HibernateException exp) {
			exp.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
		}
		return responseModelList;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<OldChassisEngVinRegDTLResponseModel> fetchOldChassisEnginVinRegistrationDTLList(String userCode,
			OldChassisEngVinRegDTLRequestModel ssRequestModel) {
		
		Session session = null;
	    List<OldChassisEngVinRegDTLResponseModel> responseList = null;
	    OldChassisEngVinRegDTLResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [sp_get_vin_details] :searchFor, :EngineNo, :Vinno, :RegistrationNo, :ChassisNo";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("searchFor", ssRequestModel.getSearchFor());
			query.setParameter("EngineNo", ssRequestModel.getEngineNo());
			query.setParameter("Vinno", ssRequestModel.getVinNo());
			query.setParameter("RegistrationNo", ssRequestModel.getRegistrationNo());
			query.setParameter("ChassisNo", ssRequestModel.getChassisNo());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<OldChassisEngVinRegDTLResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new OldChassisEngVinRegDTLResponseModel();
					response.setErrormsg((String) row.get("Message"));
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
	
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Mono<Map<String, Object>> oldChassisMail(String userCode, String eventName, BigInteger hDRId,String status) {
		Session session = null;
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		BigInteger mailItemId = null;
		String sqlQuery = "exec [SP_MAIL_CHASSIS_APPROVAL] :userCode, :eventName, :refId, :isIncludeActive";
		mapData.put("ERROR", "ERROR WHILE INSERTING OLD CHASSIS MAIL TRIGGERS..");
		try {
			session = sessionFactory.openSession();
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("eventName", eventName);
			query.setParameter("refId", hDRId);
			query.setParameter("isIncludeActive", "N");
			//query.setParameter("status", status); 
			
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
			mapData.put("ERROR", "ERROR WHILE INSERTING OLD CHASSIS SUBMISSION MAIL TRIGGERS.");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE INSERTING OLD CHASSIS SUBMISSION MAIL TRIGGERS.");
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

	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public List<PoPlantResponseModel> fetchPOPlantList(String userCode, PoPlantRequstModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchPOPlantList invoked.." + userCode);
		}
		Session session = null;
		Query query = null;
		List<PoPlantResponseModel> responseModelList = null;
		String sqlQuery = "exec [SP_SAORD_GETPLANTLIST] :userCode, :pcId, :dealerId, :branchId, :includeInactive";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", userCode);
			query.setParameter("pcId", requestModel.getPcId());
			query.setParameter("dealerId", requestModel.getDealerId());
			query.setParameter("branchId", requestModel.getBranchId());
			query.setParameter("includeInactive", requestModel.getIncludeInActive());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<PoPlantResponseModel>();
				PoPlantResponseModel responseModel = null;
				for (Object object : data) {
					Map row = (Map) object;
					responseModel = new PoPlantResponseModel();
					responseModel.setPoPlantId((Integer) row.get("plantId"));
					responseModel.setPlantCode((String) row.get("plantCode"));
					responseModel.setPlantName((String) row.get("plantName"));
					responseModel.setLocation((String) row.get("location"));

					responseModelList.add(responseModel);
				}
			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return responseModelList;
	}

	


}