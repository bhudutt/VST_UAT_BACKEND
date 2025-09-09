package com.hitech.dms.web.dao.spare.customer.order;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartDetailsRequest;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoListRequest;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderCalculationRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderCancelRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderUpdateRequest;
import com.hitech.dms.web.model.spara.customer.order.response.CustOrderProductCtgResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderCreateResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderPartDetailResponse;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustomerOrderCalculationResponse;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustomerOrderPartUploadResponse;
import com.hitech.dms.web.model.spara.delivery.challan.request.DcSelectCustomerOrderRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DcUpdateSparePartRequest;
import com.hitech.dms.web.model.spara.delivery.challan.request.DeliveryChallanPartDetailRequest;
import com.hitech.dms.web.model.spare.create.response.SparePoCategoryResponse;

public interface SpareCustomerOrderDao {

	SpareCustOrderCreateResponseModel createCustomerOrder(String authorizationHeader, String userCode,
			SpareCustomerOrderRequest requestModel, Device device);

	List<SparePoCategoryResponse> getAllCustOrderPoCategory();

	SpareCustomerOrderPartUploadResponse uploadSpareCustomerOrderPart(String authorizationHeader, String userCode, BigInteger  branchId, Integer dealerId, Integer productCategoryId,
			MultipartFile file);

	List<SpareCustOrderPartDetailResponse> fetchPartDetailsByPartNumber(String userCode, String key, Integer orderQty, Integer productCategoryId,BigInteger branchId, Integer partyTypeId);


	List<partSearchResponseModel> fetchPartNumber(String userCode, CustomerOrderPartNoRequest requestModel);

	List<SpareCustOrderPartDetailResponse> fetchCOPartDetailsByPartId(String userCode, CustomerOrderPartDetailsRequest bean);

	CustOrderProductCtgResponseModel getDocumentNumber(String documentType, BigInteger branchId);

	List<SpareCustomerOrderCalculationResponse> fetchSpareCoItemAmountCal(String userCode,SpareCustomerOrderCalculationRequest requestModel, Integer partyBranchId);

	List<SpareCustOrderPartDetailResponse> customerOrderPartDetail(String userCode,CustomerOrderPartNoRequest requestModel);

	List<SparePoCategoryResponse> getAllSubCategory();

	SpareCustOrderCreateResponseModel updateCustomerOrder(String authorizationHeader, String userCode,
			@Valid List<SpareCustomerOrderUpdateRequest> requestModel, Device device);

	SpareCustOrderCreateResponseModel cancelCustomerOrder(String authorizationHeader, String userCode,
			SpareCustomerOrderCancelRequest requestModel, Device device);

	void updateDcFlagToCO(List<DcSelectCustomerOrderRequest> dcSelectCO,String dcNumber, String userCode);

	SpareCustOrderCreateResponseModel editSpareCustomerOrder(String authorizationHeader, String userCode,
			@Valid SpareCustomerOrderRequest requestModel, Device device);

	void coQtyAndStatusUpdate(List<DeliveryChallanPartDetailRequest> dcPartDetailList, String userCode);

	Integer deletePartNofromDTL(String authorizationHeader, String userCode,
			CustomerOrderPartNoListRequest requestModel, Device device);

	void coBalanceAndStatusUpdate(List<DcUpdateSparePartRequest> sparePartDetails, String userCode);



	

	

}