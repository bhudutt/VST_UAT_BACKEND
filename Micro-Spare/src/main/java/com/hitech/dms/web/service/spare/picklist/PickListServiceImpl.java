package com.hitech.dms.web.service.spare.picklist;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.dao.spare.picklist.PickListDao;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PicklistRequest;
import com.hitech.dms.web.model.spara.customer.order.picklist.response.SpareCustOrderForPickListResponse;
import com.hitech.dms.web.model.spara.customer.order.picklist.response.SpareCustOrderPartForPickListResponse;
import com.hitech.dms.web.model.spara.customer.order.response.CustomerOrderNumberResponse;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.picklist.PickListResponse;

@Service
public class PickListServiceImpl implements PickListService {

	@Autowired
	PickListDao pickListDao;

	@Override
	public HashMap<BigInteger, String> searchCustomerOrderNumber(String searchText, String searchFor, String userCode) {
		return pickListDao.searchCustomerOrderNumber(searchText, searchFor, userCode);
	}

	@Override
	public CustomerOrderNumberResponse createPicklist(String userCode, PicklistRequest picklistRequest) {
		return pickListDao.createPicklist(userCode, picklistRequest);
	}

	@Override
	public SpareCustOrderForPickListResponse fetchCustomerOrderHdrAndDtl(Integer customerOrderId, String userCode) {
		SpareCustOrderForPickListResponse response = pickListDao.getCustomerOrderById(userCode, customerOrderId);
		List<SpareCustOrderPartForPickListResponse> partDetailList = pickListDao.getCustomerOrderDetailsById(userCode,
				customerOrderId);
		response.setPartDetailList(partDetailList);
		return response;
	}

	@Override
	public List<PartNumberDetails> fetchPartNumberForPickList(Integer partId, String userCode, Integer branchId,
			Integer poHdrId, Integer coId, Integer refDocId, String flag) {
		return pickListDao.fetchPartNumberForPickList(partId, userCode, branchId,
				poHdrId, coId, refDocId, flag);
	}

	@Override
	public ApiResponse<List<PickListResponse>> fetchPickListDetails(String picklistNumber, String coNo, String poNumber, Date fromDate,
			Date toDate, String userCode, Integer page, Integer size) {
		return pickListDao.fetchPickListDetails(picklistNumber, coNo, poNumber, fromDate,
				toDate, userCode, page, size);
	}
	
	

	@Override
	public PickListResponse fetchHdrAndDtl(int pickListHdrId) {
		return pickListDao.fetchHdrAndDtl(pickListHdrId);
	}

	@Override
	public HashMap<BigInteger, String> searchPickListNumber(String searchText, int counterSaleId, String userCode) {
		return pickListDao.searchPickListNumber(searchText, counterSaleId, userCode);
	}

	@Override
	public List<String> getAllSearchPickListst(String searchText, String userCode) {
		
		return pickListDao.getAllPicListNo(searchText,userCode);
	}

}
