package com.hitech.dms.web.serviceImpl.spare.deliveryChallan;

import java.math.BigInteger;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Service;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.dao.spare.customer.order.SpareCustomerOrderDao;
import com.hitech.dms.web.dao.spare.deliveryChallan.DeliveryChallanDao;
import com.hitech.dms.web.dao.spare.picklist.PickListDao;
import com.hitech.dms.web.model.spara.customer.order.request.CustomerOrderPartNoRequest;
import com.hitech.dms.web.model.spara.customer.order.response.CustOrderProductCtgResponseModel;
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
import com.hitech.dms.web.service.spare.deliveryChallan.DeliveryChallanService;

@Service
public class DeliveryChallanServiceImpl implements DeliveryChallanService{
	
	
	@Autowired
	private DeliveryChallanDao deliveryChallanDao;
	
	@Autowired
	private SpareCustomerOrderDao spareCustomerOrderDao;
	
	@Autowired
	private PickListDao pickListDao;
	

	@Override
	public SpareDcCreateResponse createDeliveryChallan(String authorizationHeader, String userCode,
			 DeliveryChallanDetailRequest requestModel, Device device) {
		CustOrderProductCtgResponseModel response = spareCustomerOrderDao.getDocumentNumber(requestModel.getDocumentType(), requestModel.getBranchId());
		if(response.getProductCtgNumber()==null) {
			SpareDcCreateResponse responseModel = new SpareDcCreateResponse();
			responseModel.setMsg("Unable to generate a DOC number Kindly inform admin.");
			responseModel.setStatusCode(WebConstants.STATUS_EXPECTATION_FAILED_417);
			responseModel.setDeliveryChallanNumber(response.getProductCtgNumber());
			return responseModel;
		}
		requestModel.setDcNumber(response.getProductCtgNumber());
		spareCustomerOrderDao.updateDcFlagToCO(requestModel.getDcSelectCO(),requestModel.getDcNumber(), userCode);
		spareCustomerOrderDao.coQtyAndStatusUpdate(requestModel.getDcPartDetailList(),userCode);
		deliveryChallanDao.deleteStockInventory(requestModel.getDcPartDetailList(),userCode);
		pickListDao.updateDcQty(requestModel.getDcPartDetailList(),userCode);
		return deliveryChallanDao.createDeliveryChallan(authorizationHeader, userCode, requestModel, device);
		
	}


	@Override
	public List<DCcustomerOrderResponse> customerOrderDtl(Integer productCategoryId, Integer partyId,  String reqType, BigInteger branchId) {
		
		return deliveryChallanDao.customerOrderDtl(productCategoryId,partyId,null,reqType, branchId);
	}


	@Override
	public List<COpartDetailResponse> getDcPartDetail(String customerOrderNumber, String flag, String userCode) {
		
		return deliveryChallanDao.getDcPartDetail(customerOrderNumber, flag, userCode);
	}



	@Override
	public List<DcPartQtyCalCulationResponse> getPartDetailsByDcQty(CustomerOrderPartNoRequest resquest) {
		return deliveryChallanDao.getPartDetailsByDcQty(resquest);
	}


	@Override
	public DcSearchListResponse searchByDcFields(String userCode, DcSearchRequest resquest, Device device) {
		return deliveryChallanDao.searchByDcFields(userCode,resquest,device);
	}


	@Override
	public List<DeliveryChallanNumberResponse> searchDeliveryChallanNumber(String searchText, String userCode) {
		return deliveryChallanDao.searchDeliveryChallanNumber(searchText,  userCode);
	}


	@Override
	public DeliveryChallanHeaderAndDetailResponse getDeliveryChallanById(String userCode, Integer deliveryChallanId) {
		
		DeliveryChallanHeaderAndDetailResponse headerResponse = deliveryChallanDao.getDeliveryChallanHeaderById(userCode,  deliveryChallanId);
		headerResponse.setCustomerOrderList(deliveryChallanDao.customerOrderDtl(headerResponse.getProductCategoryId().intValue(), headerResponse.getPartyBranchId().intValue(), headerResponse.getDcNumber() ,null, headerResponse.getBranchId()));
		headerResponse.setDetailList(deliveryChallanDao.getDeliveryChallanDetailById(userCode,  deliveryChallanId));
		return headerResponse;
	}


	@Override
	public SpareDcCreateResponse updateStatusAndRemark(String userCode, DcUpdateStatusRequest requestModel) {
		deliveryChallanDao.addStockInventory(requestModel.getSparePartDetails(),userCode);
		spareCustomerOrderDao.coBalanceAndStatusUpdate(requestModel.getSparePartDetails(),userCode);
		return deliveryChallanDao.updateStatusAndRemark(userCode,requestModel);
		
	}


}
