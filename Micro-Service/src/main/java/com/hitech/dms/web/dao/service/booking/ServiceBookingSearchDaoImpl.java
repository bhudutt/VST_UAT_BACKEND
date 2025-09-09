package com.hitech.dms.web.dao.service.booking;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.model.service.booking.ServiceBookingChassisAllListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingChassisAllListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingCustomerListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingCustomerListResponseModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingByBookingNoRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingByBookingNoResponseModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingChassisByBookingnumberRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingChassisByBookingnumberResponseModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingModelDTLRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingModelDTLResponseModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingSearchCustDTLRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingSearchCustDTLResponseModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingSearchListRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingSearchListResponseModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingSearchListResultResponse;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingSearchVariantByRequestModel;
import com.hitech.dms.web.model.service.booking.SearchList.ServiceBookingSearchVariantByResponseModel;
import com.hitech.dms.web.model.service.bookingview.request.ServiceBookingViewRequestModel;
import com.hitech.dms.web.model.service.bookingview.response.ServiceBookingViewResponseModel;
import com.hitech.dms.web.model.service.bookingview.response.ServiceBookingViewStatusResponseModel;

import lombok.ToString;

@Repository
public class ServiceBookingSearchDaoImpl implements ServiceBookingSearchDao {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceBookingSearchDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private DozerBeanMapper mapper;

	@SuppressWarnings({ "deprecation", "rawtypes" })
	@Override
	public ServiceBookingSearchListResultResponse serviceBookingSearchList(String userCode,
			ServiceBookingSearchListRequestModel RequestModel) {
		
		if (logger.isDebugEnabled()) {
			logger.debug(
					"serviceBookingSearchList invoked.." + userCode + " " + RequestModel.toString());
		}
		Query query = null;
		Session session = null;
		ServiceBookingSearchListResultResponse responseListModel = null;
	    List<ServiceBookingSearchListResponseModel> responseModelList = null;
	    Integer recordCount = 0;
		String sqlQuery = "exec [SP_GET_SERVICE_SEARCHBY_LIST] :usercode, :bookingNo, :FromDate, :ToDate, :bookingStatus, :chassisNo, :serviceCategory, :customerName, :customerMobile, :Modelname, :BookingSource, :servicetype, :AppointmentFromDate, :AppointmentToDate,:page, :size, :pcId, :orgHierID, :dealerId, :branchId, :Stateid, :ZoneId, :TerritoryId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("usercode", userCode);
			query.setParameter("bookingNo", RequestModel.getBookingNo());
			query.setParameter("FromDate", RequestModel.getFromDate());
			query.setParameter("ToDate", RequestModel.getToDate());
			query.setParameter("bookingStatus", RequestModel.getBookingStatus());
			query.setParameter("chassisNo", RequestModel.getChassisNo());
			query.setParameter("serviceCategory", RequestModel.getServiceCategory());
			query.setParameter("customerName", RequestModel.getCustomerName());
			query.setParameter("customerMobile", RequestModel.getCustomerMobile());
			query.setParameter("Modelname", RequestModel.getModel());
			query.setParameter("BookingSource", RequestModel.getBookingSource());
			query.setParameter("servicetype", RequestModel.getServiceType());
			query.setParameter("AppointmentFromDate", RequestModel.getAppointmentFormDate());
			query.setParameter("AppointmentToDate", RequestModel.getAppointmentToDate());
			query.setParameter("page", RequestModel.getPage());
			query.setParameter("size", RequestModel.getSize());
			query.setParameter("pcId", RequestModel.getProfitCenterId());
			query.setParameter("orgHierID", RequestModel.getOrgHierarchyId());
			query.setParameter("dealerId", RequestModel.getDealerId());
			query.setParameter("branchId", RequestModel.getBranchId());
			query.setParameter("Stateid", RequestModel.getStateId());
			query.setParameter("ZoneId", RequestModel.getZone());
			query.setParameter("TerritoryId", RequestModel.getTerritory());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();
			if (data != null && !data.isEmpty()) {
				
				responseListModel=new ServiceBookingSearchListResultResponse();
				responseModelList = new ArrayList<ServiceBookingSearchListResponseModel>();
				ServiceBookingSearchListResponseModel response=null;
				for (Object object : data) {
					Map row = (Map) object;
					response = new ServiceBookingSearchListResponseModel();
					response.setId((BigInteger) row.get("id"));
					response.setBranchName((String) row.get("BranchName"));
					response.setBookingNo((String) row.get("bookingno"));
					response.setBookingDate((String) row.get("booking_date"));
					response.setBookingstatus((String) row.get("bookingstatus"));
					response.setAppointmentDate((String) row.get("appointment_date"));
					response.setAppointmentTime((String) row.get("appointment_time"));
					response.setChassisNo((String) row.get("chassis_no"));
					response.setEngineNo((String) row.get("engine_no"));
					response.setCustomerName((String) row.get("customerName"));
					response.setSourceOfBooking((String) row.get("source_of_booking"));
					response.setServiceCategory((String) row.get("category"));
					response.setServiceType((String) row.get("SrvTypeDesc"));
					response.setModel((String) row.get("MODELNAME"));
					response.setRemark((String) row.get("remarks"));
					response.setServiceRepairType((String) row.get("repair_order_desc"));
					//response.setCreatedBy((String) row.get("CreatedBy"));				
					response.setCallDate((String) row.get("calldate"));
					//response.setCreatedon((String) row.get("created_on"));
					
					if (recordCount.compareTo(0) == 0) {
						recordCount = (Integer) row.get("totalRecords");
					}
					responseModelList.add(response);
				}

				responseListModel.setRecordCount(recordCount);
				responseListModel.setSearchResult(responseModelList);

			}
		} catch (SQLGrammarException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (HibernateException exp) {
			logger.error(this.getClass().getName(), exp);
		} catch (Exception exp) {
			logger.error(this.getClass().getName(), exp);
		} finally {
		}
		return responseListModel;
	}
	

	@Override
	public List<ServiceBookingModelDTLResponseModel> fetchModelDTLList(String userCode,
			ServiceBookingModelDTLRequestModel ssModel) {
		
		Session session = null;
	    List<ServiceBookingModelDTLResponseModel> responseList = null;
	    ServiceBookingModelDTLResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GET_MODEL] :modelname";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("modelname", ssModel.getModelName());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ServiceBookingModelDTLResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new ServiceBookingModelDTLResponseModel();
					response.setModelId((BigInteger) row.get("model_id"));
					response.setModelName((String) row.get("Model_name"));				
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
	public List<ServiceBookingSearchVariantByResponseModel> fetchVariantByModelList(String userCode,
			ServiceBookingSearchVariantByRequestModel requestModel) {
		
		Session session = null;
	    List<ServiceBookingSearchVariantByResponseModel> responseList = null;
	    ServiceBookingSearchVariantByResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GETVARIANTBYMODEL] :modelId, :pcId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("modelId", requestModel.getModelid());
			query.setParameter("pcId", requestModel.getPcid());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ServiceBookingSearchVariantByResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new ServiceBookingSearchVariantByResponseModel();
					response.setPcId((Integer) row.get("pc_id"));
					response.setVariant((String) row.get("variant"));			
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
	public List<ServiceBookingByBookingNoResponseModel> fetchBookingNoList(String userCode,
			ServiceBookingByBookingNoRequestModel ssRequestModel) {
		
		Session session = null;
	    List<ServiceBookingByBookingNoResponseModel> responseList = null;
	    ServiceBookingByBookingNoResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GETBOOKINGNO] :userCode, :bookingNo";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", ssRequestModel.getUserCode());
			query.setParameter("bookingNo", ssRequestModel.getBookingNo());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ServiceBookingByBookingNoResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new ServiceBookingByBookingNoResponseModel();
					response.setId((BigInteger) row.get("ID"));
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

	@Override
	public List<ServiceBookingChassisByBookingnumberResponseModel> fetchChassisNoByBookingNoList(String userCode,
			ServiceBookingChassisByBookingnumberRequestModel ssRequestModel) {
		     
		Session session = null;
	    List<ServiceBookingChassisByBookingnumberResponseModel> responseList = null;
	    ServiceBookingChassisByBookingnumberResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_GETCHASSISNOBYBOOKINGNO] :userCode,:bookingNo";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("userCode", ssRequestModel.getUserCode());
			query.setParameter("bookingNo", ssRequestModel.getBookingNo());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ServiceBookingChassisByBookingnumberResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new ServiceBookingChassisByBookingnumberResponseModel();
					response.setVinId((BigInteger) row.get("vin_id"));
					response.setChassisNo((String) row.get("chassis_no"));	
					response.setCustomerId((BigInteger) row.get("customer_id"));
					response.setCustomerName((String) row.get("customerName"));
					response.setCustomerMobile((String) row.get("mobile_no"));
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

	// New module
	@Override
	public List<ServiceBookingSearchCustDTLResponseModel> serviceBookingSearchCustDTLList(String userCode,
			ServiceBookingSearchCustDTLRequestModel ssRequestModel) {
		
		Session session = null;
	    List<ServiceBookingSearchCustDTLResponseModel> responseList = null;
	    ServiceBookingSearchCustDTLResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_CM_Customer_Details_by_service] :customerId";
		try {
			
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("customerId", ssRequestModel.getCustomerId());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ServiceBookingSearchCustDTLResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new ServiceBookingSearchCustDTLResponseModel();
					response.setCustomerId((BigInteger) row.get("customer_id"));
					response.setCustomerCategoryId((BigInteger) row.get("CustomerCategory_Id"));
					response.setCustomerCategory((String) row.get("customerCategory"));
					response.setCustomerCode((String) row.get("customerCode"));;
					response.setCustomerName((String) row.get("CustomerName"));
					response.setChassisNo((String) row.get("chassis_no"));
					response.setEngineNo((String) row.get("engine_no"));
					response.setVinNo((String) row.get("vin_no"));
					response.setModelName((String) row.get("model_name"));
					response.setRegistrationNo((String) row.get("registration_number"));
					response.setMobile((String) row.get("mobile_no"));
					response.setAddress((String) row.get("address1"));
					response.setCity((String) row.get("citydesc"));
					response.setPreviousServiceType((String) row.get("SrvTypeDesc"));
					response.setPreviousServiceHour((Double) row.get("previous_hour"));
					response.setStatus((String) row.get("status"));
					//response.setNextDueServiceType((String) row.get(""));
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
	public List<ServiceBookingChassisAllListResponseModel> fetchChassisList(String userCode,
			ServiceBookingChassisAllListRequestModel ssRequestModel) {
		// TODO Auto-generated method stub
			Session session = null;
		    List<ServiceBookingChassisAllListResponseModel> responseList = null;
		    ServiceBookingChassisAllListResponseModel response = null;
			NativeQuery<?> query = null;
			String sqlQuery = "exec [SP_SA_GETCHASSIS_SERVICE] :chassisNo";
			try {
				session = sessionFactory.openSession();
				query = session.createSQLQuery(sqlQuery);
				query.setParameter("chassisNo", ssRequestModel.getChassisNo());
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List<?> data = query.list();
				if (data != null && !data.isEmpty()) {
					responseList = new ArrayList<ServiceBookingChassisAllListResponseModel>();
					for (Object object : data) {
						Map row = (Map) object;
						response = new ServiceBookingChassisAllListResponseModel();
						response.setChassisNo((String) row.get("chassis_no"));
						
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
	public List<ServiceBookingCustomerListResponseModel> fetchCustomerList(String userCode,
			ServiceBookingCustomerListRequestModel ssRequestModel) {   
		Session session = null;
	    List<ServiceBookingCustomerListResponseModel> responseList = null;
	    ServiceBookingCustomerListResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SP_SA_GETCUSTOMER_SERVICEBOOKING] :customerName";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("customerName", ssRequestModel.getCustomerName());
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ServiceBookingCustomerListResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new ServiceBookingCustomerListResponseModel();
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
	public List<ServiceBookingViewResponseModel> fetchServiceBookingListView(String userCode, BigInteger bookingId) {
		
		Session session = null;
	    List<ServiceBookingViewResponseModel> responseList = null;
	    ServiceBookingViewResponseModel response = null;
		NativeQuery<?> query = null;
		String sqlQuery = "exec [SV_GETSERVICEBOOKING_VIEW_LIST] :bookingId";
		try {
			session = sessionFactory.openSession();
			query = session.createSQLQuery(sqlQuery);
			query.setParameter("bookingId", bookingId);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<?> data = query.list();
			if (data != null && !data.isEmpty()) {
				responseList = new ArrayList<ServiceBookingViewResponseModel>();
				for (Object object : data) {
					Map row = (Map) object;
					response = new ServiceBookingViewResponseModel();
					response.setBranchName((String) row.get("BranchName"));
					response.setServiceBookingDate((Date) row.get("booking_date"));
					response.setServiceBookingStatus((String) row.get("status"));
					response.setCallDate((Date) row.get("callDate"));
					response.setBookingsourceName((BigInteger) row.get("source_of_Booking_id"));
					response.setCustomerMobileNo((String) row.get("mobile_no"));
					response.setCustomerName((String) row.get("CustomerName"));
					response.setChassisNo((String) row.get("chassis_no"));
					response.setEngineNo((String) row.get("engine_no"));
					response.setModeldesc((String) row.get("model_name"));
					response.setVinNo((String) row.get("vin_no"));
					response.setRegistrationNo((String) row.get("registration_number"));
					response.setAppointmentDate((Date) row.get("appointment_date"));
					response.setAppointmentTime((String) row.get("appointment_time"));
					response.setServiceCategory((BigInteger) row.get("service_category_id"));
					response.setServicetype((BigInteger) row.get("service_type_id"));
					response.setServiceRepairType((BigInteger) row.get("service_repair_type_id"));
					response.setServiceRemark((String) row.get("remarks"));
					response.setCustomerCode((String) row.get("CustomerCode"));
					response.setCustomerName((String) row.get("CustomerName"));
					response.setCustomerMobileNo((String) row.get("mobile_no"));
					response.setAddress((String) row.get("address"));
					response.setCity((String) row.get("citydesc"));
					response.setPreviousServiceType((String) row.get("previousServiceType"));
					response.setPreviousServiceDate((Date) row.get("previousServiceTypeDate"));
					response.setPreviousServiceHour((String) row.get("previous_hour"));
					response.setNextDueServiceType((String) row.get("nextDueServiceType"));
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
	public ServiceBookingViewStatusResponseModel fetchBookingAppointmentUpdate(String userCode,
			ServiceBookingViewRequestModel requestModel) {
		if (logger.isDebugEnabled()) {
			logger.debug("fetchBookingAppointmentUpdate invoked.." + requestModel.toString());
		}
		Session session = null;
		Transaction transaction = null;
		String msg = null;
		ServiceBookingViewStatusResponseModel responseModel = new ServiceBookingViewStatusResponseModel();
		Query query = null;
		boolean isFailure = false;
		boolean isSuccess = true;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query1 = session.createQuery("UPDATE ServiceBookingEntity SET status =:bookingstatus, appointment_date=:appointmentdate, appointment_time=:appointmenttime, service_category_id=:servicecategory, service_type_id=:servicetype, service_repair_type_id=:repairordertype, remarks=:servicebookingremark WHERE id = :id");
			query1.setParameter("bookingstatus", requestModel.getServiceBookingStatus());
			query1.setParameter("appointmentdate", requestModel.getAppointmentDate());
			query1.setParameter("appointmenttime",requestModel.getAppointmentTime());
			query1.setParameter("servicecategory",requestModel.getServiceCategory());
			query1.setParameter("servicetype",requestModel.getServiceType());
			query1.setParameter("repairordertype",requestModel.getServiceRepairType());
			query1.setParameter("servicebookingremark",requestModel.getRemarks());
			query1.setParameter("id",requestModel.getBookingId());
			int serviceBookingEntity = query1.executeUpdate();
			if (isSuccess) {
				transaction.commit();
				session.close();
				//sessionFactory.close();
				responseModel.setStatusCode(WebConstants.STATUS_CREATED_201);
				
				msg = "Appointment Upadte Successfully";
				responseModel.setMsg(msg);
			}else if(isFailure){
				responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
				msg = "Resource Not Found";
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


