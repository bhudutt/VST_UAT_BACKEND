package com.hitech.dms.web.dao.service.booking;

import java.math.BigInteger;
import java.util.List;

import com.hitech.dms.web.model.service.booking.ServiceBookingChassisAllListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingChassisAllListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingCustomerListRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingCustomerListResponseModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSearchByMobileRequestModel;
import com.hitech.dms.web.model.service.booking.ServiceBookingSearchByMobileResponseModel;
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

public interface ServiceBookingSearchDao {

	public ServiceBookingSearchListResultResponse serviceBookingSearchList(String userCode, ServiceBookingSearchListRequestModel requestModel);
	
	public List<ServiceBookingModelDTLResponseModel> fetchModelDTLList(String userCode,
			ServiceBookingModelDTLRequestModel ssRequestModel);

	public List<ServiceBookingSearchVariantByResponseModel> fetchVariantByModelList(String userCode,
			ServiceBookingSearchVariantByRequestModel ssRequestModel);

	public List<ServiceBookingByBookingNoResponseModel> fetchBookingNoList(String userCode,
			ServiceBookingByBookingNoRequestModel ssRequestModel);

	public List<ServiceBookingChassisByBookingnumberResponseModel> fetchChassisNoByBookingNoList(String userCode,
			ServiceBookingChassisByBookingnumberRequestModel ssRequestModel);

	public List<ServiceBookingSearchCustDTLResponseModel> serviceBookingSearchCustDTLList(String userCode,
			ServiceBookingSearchCustDTLRequestModel requestModel);

	public List<ServiceBookingChassisAllListResponseModel> fetchChassisList(String userCode,
			ServiceBookingChassisAllListRequestModel ssRequestModel);

	public List<ServiceBookingCustomerListResponseModel> fetchCustomerList(String userCode,
			ServiceBookingCustomerListRequestModel ssRequestModel);

	public List<ServiceBookingViewResponseModel> fetchServiceBookingListView(String userCode, BigInteger bookingId);

	public ServiceBookingViewStatusResponseModel fetchBookingAppointmentUpdate(String userCode,
			ServiceBookingViewRequestModel requestModel);

	
	
}
