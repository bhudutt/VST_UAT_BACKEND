package com.hitech.dms.web.service.spare.customer.order;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.dao.spare.customer.order.SpareCustomerOrderViewDao;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderSearchRequest;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderPartDetailResponse;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderSearchResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderViewListResponseModel;

@Service
public class SpareCustomerOrderViewServiceImpl implements SpareCustomerOrderViewService{
	
	@Autowired
	private SpareCustomerOrderViewDao spareCustomerOrderViewDao;

	@Override
	public SpareCustOrderSearchResponseModel searchCODetailByStartAndEndDate(String authorizationHeader, String userCode,
			CustomerOrderSearchRequest requestMap, Device device)throws ParseException {
		return  spareCustomerOrderViewDao.searchCODetailByStartAndEndDate(authorizationHeader, userCode, requestMap, device);
	}

	@Override
	public SpareCustOrderViewListResponseModel getCustomerOrderById(String userCode, Integer customerOrderId) {
		List<SpareCustOrderPartDetailResponse> partDetailList =	spareCustomerOrderViewDao.getCustomerOrderDetailsById(userCode, customerOrderId);
		SpareCustOrderViewListResponseModel response =  spareCustomerOrderViewDao.getCustomerOrderById(userCode, customerOrderId);
		response.setPartDetailList(partDetailList);
		return response;
		
		
	}

}
