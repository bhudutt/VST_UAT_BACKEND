package com.hitech.dms.web.dao.spare.picklist;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.spara.customer.order.picklist.request.PicklistRequest;
import com.hitech.dms.web.model.spara.customer.order.picklist.response.SpareCustOrderForPickListResponse;
import com.hitech.dms.web.model.spara.customer.order.picklist.response.SpareCustOrderPartForPickListResponse;
import com.hitech.dms.web.model.spara.customer.order.response.CustomerOrderNumberResponse;
import com.hitech.dms.web.model.spara.delivery.challan.request.DeliveryChallanPartDetailRequest;
import com.hitech.dms.web.model.spare.branchTransfer.indent.PartNumberDetails;
import com.hitech.dms.web.model.spare.picklist.PickListResponse;

public interface PickListDao {

	HashMap<BigInteger, String> searchCustomerOrderNumber(String searchText, String searchFor, String userCode);

	CustomerOrderNumberResponse createPicklist(String userCode, PicklistRequest picklistRequest);

	SpareCustOrderForPickListResponse getCustomerOrderById(String userCode, Integer customerOrderId);

	List<SpareCustOrderPartForPickListResponse> getCustomerOrderDetailsById(String userCode, Integer customerOrderId);

	List<PartNumberDetails> fetchPartNumberForPickList(Integer partId, String userCode, Integer branchId,
			Integer poHdrId, Integer coId, Integer refDocId, String flag);

	ApiResponse<List<PickListResponse>> fetchPickListDetails(String picklistNumber, String coNo, String poNumber, Date fromDate, Date toDate,
			String userCode, Integer page, Integer size);

	PickListResponse fetchHdrAndDtl(int pickListHdrId);

	HashMap<BigInteger, String> searchPickListNumber(String searchText, int counterSaleId, String userCode);
	
	List<String> getAllPicListNo(String searchText,String userCode);

	void updateDcQty(List<DeliveryChallanPartDetailRequest> dcPartDetailList, String userCode);

}
