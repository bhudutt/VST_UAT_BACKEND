package com.hitech.dms.web.dao.service.booking;
   
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.doc.generator.DocumentNumberGenerator;
import com.hitech.dms.web.entity.customer.ServiceBookingEntity;
import com.hitech.dms.web.model.service.booking.ServiceBookingCategoryListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingCategoryListResponseModel;
//import com.hitech.dms.web.model.service.booking.ServiceBookingChassisListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingChassisListResponseModel;
//import com.hitech.dms.web.model.service.booking.ServiceBookingCustomerListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingRepairOrderListRequest;
import com.hitech.dms.web.model.service.booking.ServiceBookingRepairOrderListResponse;
import com.hitech.dms.web.model.service.booking.ServiceBookingResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSearchByMobileRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSearchByMobileResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingServiceTypeListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingServiceTypeListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSourceListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSourceListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingStatusRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingStatusResponseModel;

@Repository
public class ServiceBookingDaoImpl implements ServiceBookingDao {

	private static final Logger logger = LoggerFactory.getLogger(ServiceBookingDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private DocumentNumberGenerator docNumber;

	@Override
	public ServiceBookingResponseModel createServiceBooking(String userCode, ServiceBookingEntity RequestModel) {

		if (logger.isDebugEnabled()) {
			logger.debug("createServiceBooking invoked.." + userCode);
			logger.debug(RequestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		Map<String, Object> mapData = null;
		ServiceBookingResponseModel responseModel = new ServiceBookingResponseModel();
		String bookingNo = null;
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			BigInteger userId = null;

			mapData = fetchUserDTLByUserCode(session, userCode);
			if (mapData != null && mapData.get("SUCCESS") != null) {
				userId = (BigInteger) mapData.get("userId");
				bookingNo = docNumber.getDocumentNumber1("SBK", RequestModel.getBranch(), session);
				RequestModel.setServicebookingno(bookingNo);
				RequestModel.setCreatedby(userId);
				saveBookingNumberInCmDocBranch("Service Booking",RequestModel.getBranch(), bookingNo, session);
				session.save(RequestModel);

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
				mapData = fetchBookingNoByBookingId(session, RequestModel.getId());
				if (mapData != null && mapData.get("SUCCESS") != null) {
					responseModel.setId(RequestModel.getId());
					responseModel.setBookingNo(msg);
					// responseModel.setBookingNo((String)mapData.get("bookingNo"));
					responseModel
							.setMsg((String) mapData.get("bookingNo") + " " + "Booking Number Created Successfully.");
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
	private void saveBookingNumberInCmDocBranch(String documentType, BigInteger branchID,
			String bookingNo, Session session) {
		Query query = null;
		SimpleDateFormat formatter = getSimpleDateFormat();
		String currentDate = null;
		currentDate=formatter.format(new Date());
		String sqlQuery = "exec [Update_INV_Doc_No] :LastDocumentNumber, :DocumentTypeDesc,'"+currentDate +"',:BranchID";
		query = session.createSQLQuery(sqlQuery);
		query.setParameter("LastDocumentNumber", bookingNo);
		query.setParameter("DocumentTypeDesc", documentType);
		query.setParameter("BranchID", branchID);
//		query.setParameter("DocumentType", documentType);

		int k = query.executeUpdate();

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

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public Map<String, Object> fetchBookingNoByBookingId(Session session, BigInteger id) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		Query query = null;
		String sqlQuery = "Select bb.id id, bb.bookingno bookingno from SV_Booking bb where bb.id =:id";
		mapData.put("ERROR", "Booking Details Not Found");
		try {
			query = session.createNativeQuery(sqlQuery);
			query.setParameter("id", id);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				String bookingNo = null;
				for (Object object : data) {
					Map row = (Map) object;
					bookingNo = (String) row.get("bookingno");
				}
				mapData.put("bookingNo", bookingNo);
				mapData.put("SUCCESS", "FETCHED");
			}
		} catch (SQLGrammarException ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING BOOKING DETAILS");
			logger.error(this.getClass().getName(), ex);
		} catch (Exception ex) {
			mapData.put("ERROR", "ERROR WHILE FTECHING BooKing DETAILS");
			logger.error(this.getClass().getName(), ex);
		} finally {
		}
		return mapData;
	}

	@Override
	public List<ServiceBookingSourceListResponseModel> fetchBookingSourceList(String userCode,
			ServiceBookingSourceListRequestModel RequestModel) {

		Session session = null;
		List<ServiceBookingSourceListResponseModel> responseModelList = null;
		ServiceBookingSourceListResponseModel responseModel = null;
		Query<ServiceBookingSourceListResponseModel> query = null;
		String sqlQuery = "Select id, service_source_desc from SV_MST_SERVICE_SOURCE";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ServiceBookingSourceListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ServiceBookingSourceListResponseModel();
					responseModel.setId((Integer) row.get("id"));
					responseModel.setServiceSourceDesc((String) row.get("service_source_desc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	@Override
	public List<ServiceBookingChassisListResponseModel> fetchBookingChassisList(String userCode, Device device,
			BigInteger customerID) {

		Session session = null;
		List<ServiceBookingChassisListResponseModel> responseModelList = null;
		ServiceBookingChassisListResponseModel responseModel = null;
		Query<ServiceBookingChassisListResponseModel> query = null;
		String sqlQuery = "exec [SV_GET_CHASSIS_BOOKING_DTL] :customerID";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("customerID", customerID);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ServiceBookingChassisListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ServiceBookingChassisListResponseModel();
					responseModel.setVinId((BigInteger) row.get("vin_id"));
					responseModel.setChassisNumber((String) row.get("chassis_no"));
					responseModel.setCustomerId((BigInteger) row.get("customer_id"));
					responseModel.setMachineItemId((BigInteger) row.get("machine_item_id"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	@Override
	public List<ServiceBookingCategoryListResponseModel> fetchBookingCategoryList(String userCode,
			ServiceBookingCategoryListRequestModel RequestModel) {

		Session session = null;
		List<ServiceBookingCategoryListResponseModel> responseModelList = null;
		ServiceBookingCategoryListResponseModel responseModel = null;
		Query<ServiceBookingCategoryListResponseModel> query = null;
		String sqlQuery = "SELECT  sv.Service_Category_ID as id, sv.CategoryDesc as category from SV_SRV_CATEGORY(NOLOCK) sv where IsActive='Y'  order by DisplayOrder";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ServiceBookingCategoryListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ServiceBookingCategoryListResponseModel();
					responseModel.setId((Integer) row.get("id"));
					responseModel.setCategory((String) row.get("category"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	@Override
	public List<ServiceBookingServiceTypeListResponseModel> fetchBookingServiceTypeList(String userCode,
			ServiceBookingServiceTypeListRequestModel RequestModel) {
		Session session = null;
		List<ServiceBookingServiceTypeListResponseModel> responseModelList = null;
		ServiceBookingServiceTypeListResponseModel responseModel = null;
		Query<ServiceBookingServiceTypeListResponseModel> query = null;
		String sqlQuery = "select sv.Service_Type_oem_ID as Service_Type_ID, SrvTypeDesc from [SV_OEM_SRV_TYPE] sv where IsActive='Y'";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ServiceBookingServiceTypeListResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ServiceBookingServiceTypeListResponseModel();
					responseModel.setServiceTypeID((Integer) row.get("Service_Type_ID"));
					responseModel.setServiceTypeDescription((String) row.get("SrvTypeDesc"));

					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	@Override
	public List<ServiceBookingRepairOrderListResponse> fetchBookingRepairOrderTypeList(String userCode,
			ServiceBookingRepairOrderListRequest RequestModel) {

		Session session = null;
		List<ServiceBookingRepairOrderListResponse> responseModelList = null;
		ServiceBookingRepairOrderListResponse responseModel = null;
		Query<ServiceBookingRepairOrderListResponse> query = null;
		String sqlQuery = "Select id, repair_order_desc from SV_MST_SERVICE_REPAIR_ORDER";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);

			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ServiceBookingRepairOrderListResponse>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ServiceBookingRepairOrderListResponse();
					responseModel.setId((Integer) row.get("id"));
					responseModel.setRepairOrderDesc((String) row.get("repair_order_desc"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	@Override
	public List<ServiceBookingStatusResponseModel> fetchServiceBookingStatusList(String userCode,
			ServiceBookingStatusRequestModel RequestModel) {

		Session session = null;
		List<ServiceBookingStatusResponseModel> responseModelList = null;
		ServiceBookingStatusResponseModel responseModel = null;
		Query<ServiceBookingStatusResponseModel> query = null;
		String sqlQuery = "Select id, active_status, status from SV_MST_BOOKING_STATUS";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ServiceBookingStatusResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ServiceBookingStatusResponseModel();
					responseModel.setId((BigInteger) row.get("id"));
					responseModel.setActivestatus((String) row.get("active_status"));
					responseModel.setStatus((String) row.get("status"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
	}

	@Override
	public List<ServiceBookingSearchByMobileResponseModel> fetchMobileList(String userCode,
			ServiceBookingSearchByMobileRequestModel ssRequestModel) {

		Session session = null;
		List<ServiceBookingSearchByMobileResponseModel> responseList = null;
		ServiceBookingSearchByMobileResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_CM_CustomerAuto_ByMobile_for_service] :customerMobileNumber, :userCode, :dealerID";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("customerMobileNumber", ssRequestModel.getCustomerMobileNumber());
			query.setParameter("userCode", ssRequestModel.getUserCode());
			query.setParameter("dealerID", ssRequestModel.getDealerID());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ServiceBookingSearchByMobileResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new ServiceBookingSearchByMobileResponseModel();
					response.setCustomerId((BigInteger) row.get("customer_id"));
					response.setCustomerCode((String) row.get("customercode"));
					response.setDisplayValue((String) row.get("displayValue"));
					response.setProspectType((String) row.get("ProspectType"));
					response.setMobileNo((String) row.get("Mobile_No"));
					response.setUnderDealerTerritory((String) row.get("UnderDealerTerritory"));
					response.setErrorFlag((String) row.get("error_flag"));
					response.setErrorMsg((String) row.get("error_msg"));
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
	public List<ServiceBookingStatusResponseModel> fetchBookingStatusViewList(String userCode, Device device) {
		Session session = null;
		List<ServiceBookingStatusResponseModel> responseModelList = null;
		ServiceBookingStatusResponseModel responseModel = null;
		Query<ServiceBookingStatusResponseModel> query = null;
		String sqlQuery = "select status from SV_MST_BOOKING_STATUS where id IN(3, 5)";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				responseModelList = new ArrayList<ServiceBookingStatusResponseModel>();
				for (Object object : data) {
					@SuppressWarnings("rawtypes")
					Map row = (Map) object;
					responseModel = new ServiceBookingStatusResponseModel();
					responseModel.setStatus((String) row.get("status"));
					responseModelList.add(responseModel);
				}
			}
		} catch (Exception e) {
			logger.error(this.getClass().getName(), e);
		} finally {
			if (session.isOpen())
				session.close();
		}

		return responseModelList;
		
	}

}
