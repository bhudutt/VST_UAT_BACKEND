package com.hitech.dms.web.service.spare.customer.order;

import java.text.ParseException;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderSearchRequest;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderSearchResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderViewListResponseModel;

public interface SpareCustomerOrderViewService {

	SpareCustOrderSearchResponseModel searchCODetailByStartAndEndDate(String authorizationHeader, String userCode,
			CustomerOrderSearchRequest requestMap, Device device)throws ParseException;

	SpareCustOrderViewListResponseModel getCustomerOrderById(String userCode, Integer customerOrderId);

}
