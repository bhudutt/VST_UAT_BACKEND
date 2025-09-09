package com.hitech.dms.web.dao.service.booking;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.mobile.device.Device;

import com.hitech.dms.web.entity.customer.ServiceBookingEntity;

import com.hitech.dms.web.model.service.booking.ServiceBookingCategoryListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingCategoryListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingChassisListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingChassisListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingRepairOrderListRequest;
import com.hitech.dms.web.model.service.booking.ServiceBookingRepairOrderListResponse;
import com.hitech.dms.web.model.service.booking.ServiceBookingRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSearchByMobileRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSearchByMobileResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingServiceTypeListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingServiceTypeListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSourceListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSourceListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingStatusRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingStatusResponseModel;


public interface ServiceBookingDao {
	
	public  ServiceBookingResponseModel createServiceBooking(String userCode, ServiceBookingEntity RequestModel);
	
	public Map<String, Object> fetchUserDTLByUserCode(Session session, String userCode);
	
	public List<ServiceBookingSourceListResponseModel> fetchBookingSourceList(String userCode, ServiceBookingSourceListRequestModel RequestModel);
	
	public List<ServiceBookingChassisListResponseModel> fetchBookingChassisList(String userCode, Device device,
			BigInteger customerID);
	
	public List<ServiceBookingCategoryListResponseModel> fetchBookingCategoryList(String userCode, ServiceBookingCategoryListRequestModel RequestModel);
	
	public List<ServiceBookingServiceTypeListResponseModel> fetchBookingServiceTypeList(String userCode, ServiceBookingServiceTypeListRequestModel RequestModel);
	
	public List<ServiceBookingRepairOrderListResponse> fetchBookingRepairOrderTypeList(String userCode, ServiceBookingRepairOrderListRequest RequestModel);

	public List<ServiceBookingStatusResponseModel> fetchServiceBookingStatusList(String userCode, ServiceBookingStatusRequestModel RequestModel);
	
	public List<ServiceBookingSearchByMobileResponseModel> fetchMobileList(String userCode,
			ServiceBookingSearchByMobileRequestModel ssRequestModel);

	public List<ServiceBookingStatusResponseModel> fetchBookingStatusViewList(String userCode, Device device);

	
	
}
