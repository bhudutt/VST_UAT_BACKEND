package com.hitech.dms.web.service.spare.customer.order;

import java.math.BigInteger;
import java.util.List;

import javax.validation.Valid;

import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;

import com.hitech.dms.web.model.SpareModel.SparePoCreateResponseModel;
import com.hitech.dms.web.model.SpareModel.partSearchResponseModel;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderDownloadTemplate;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartDetailsRequest;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoListRequest;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderCancelRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderRequest;
import com.hitech.dms.web.model.spara.customer.order.request.SpareCustomerOrderUpdateRequest;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderCreateResponseModel;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustOrderPartDetailResponse;
import com.hitech.dms.web.model.spara.customer.order.response.SpareCustomerOrderPartUploadResponse;
import com.hitech.dms.web.model.spare.create.response.SparePoCategoryResponse;

/**
 * @author Vivek.Gupta
 *
 */
public interface SpareCustomerOrderService {

	SpareCustOrderCreateResponseModel createCustomerOrder(String authorizationHeader, String userCode,
			SpareCustomerOrderRequest requestModel, Device device);


	SpareCustomerOrderPartUploadResponse validateUploadedFile(String authorizationHeader, String userCode, BigInteger branchId,
			Integer productCategoryId, Integer dealerId, Integer partyTypeId, Integer partyBranchId, MultipartFile file);

	
	List<SparePoCategoryResponse> getAllCustOrderPoCategory();


	List<SpareCustOrderPartDetailResponse> fetchCOPartDetails(String userCode,CustomerOrderPartDetailsRequest bean);

	List<partSearchResponseModel> fetchPartNumber(String userCode, CustomerOrderPartNoRequest requestModel);

	List<SpareCustOrderPartDetailResponse> customerOrderPartDetail(String userCode,
			CustomerOrderPartNoRequest requestModel);

	List<SparePoCategoryResponse> getAllSubCategory();

	SpareCustOrderCreateResponseModel updateCustomerOrder(String authorizationHeader, String userCode,@Valid List<SpareCustomerOrderUpdateRequest> requestModel, Device device);

	SpareCustOrderCreateResponseModel cancelCustomerOrder(String authorizationHeader, String userCode,
			SpareCustomerOrderCancelRequest requestModel, Device device);

	SpareCustOrderCreateResponseModel editSpareCustomerOrder(String authorizationHeader, String userCode,
			@Valid SpareCustomerOrderRequest requestModel, Device device);

	SpareCustOrderCreateResponseModel deletePartNofromDTL(String authorizationHeader, String userCode,
			CustomerOrderPartNoListRequest requestModel, Device device);


	
}
