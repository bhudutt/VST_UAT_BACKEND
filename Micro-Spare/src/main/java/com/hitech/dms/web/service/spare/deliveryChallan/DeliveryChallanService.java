package com.hitech.dms.web.service.spare.deliveryChallan;

import java.math.BigInteger;
import java.util.List;

import javax.validation.Valid;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DcSearchRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DcUpdateStatusRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DeliveryChallanDetailRequest;
import com.hitech.dms.web.model.spara.delivery.challan.response.COpartDetailResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DCcustomerOrderResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DcPartQtyCalCulationResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DcSearchListResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DeliveryChallanHeaderAndDetailResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.DeliveryChallanNumberResponse;
import com.hitech.dms.web.model.spara.delivery.challan.response.SpareDcCreateResponse;


public interface DeliveryChallanService {

	SpareDcCreateResponse createDeliveryChallan(String authorizationHeader, String userCode,
			@Valid DeliveryChallanDetailRequest requestModel, Device device);

	List<DCcustomerOrderResponse> customerOrderDtl(Integer productCategoryId,Integer partyBranchId, String reqType, BigInteger branchId);

	List<COpartDetailResponse> getDcPartDetail(String customerOrderNumber, String flag, String userCode);

	List<DcPartQtyCalCulationResponse> getPartDetailsByDcQty(CustomerOrderPartNoRequest resquest);

	DcSearchListResponse searchByDcFields(String userCode, DcSearchRequest resquest, Device device);

	List<DeliveryChallanNumberResponse> searchDeliveryChallanNumber(String searchText, String userCode);

	DeliveryChallanHeaderAndDetailResponse getDeliveryChallanById(String userCode, Integer deliveryChallanId);

	SpareDcCreateResponse updateStatusAndRemark(String userCode, DcUpdateStatusRequest requestModel);


			



}
