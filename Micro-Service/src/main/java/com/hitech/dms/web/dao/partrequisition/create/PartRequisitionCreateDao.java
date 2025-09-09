package com.hitech.dms.web.dao.partrequisition.create;

import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.partrequisition.create.request.PartListRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.RequisitionJobCardListRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.RequisitionPartDetailsRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.RequisitionPartListRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.RequitionJobCardNoByRequestModel;
import com.hitech.dms.web.model.partrequisition.create.request.SparePartRequisitionRequestModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionJobCardListResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionPartDetailsResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionPartListResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionTypeListModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequisitionVehicleDetailsResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.RequitionJobCardNoByResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.SparePartRequestedByEmployeeListResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.SparePartRequisitionResponseModel;
import com.hitech.dms.web.model.partrequisition.create.response.SparePartRequisitionTypeResponseModel;

public interface PartRequisitionCreateDao {

	SparePartRequisitionResponseModel createPartRequisition(String userCode,
		 SparePartRequisitionRequestModel requestModel, Device device);

	List<SparePartRequisitionTypeResponseModel> fetchServiceTypeList(String userCode, Device device);

	List<SparePartRequestedByEmployeeListResponseModel> fetchRequestedByEmpList(String userCode, Device device);

	List<RequitionJobCardNoByResponseModel> fetchJobCardByDTLList(String userCode,
			RequitionJobCardNoByRequestModel requestModel);

	List<RequisitionJobCardListResponseModel> fetchJobCardList(String userCode,
			RequisitionJobCardListRequestModel ssRequestModel);

	List<RequisitionPartListResponseModel> fetchPartList(String userCode,
			RequisitionPartListRequestModel ssRequestModel);

	List<RequisitionPartDetailsResponseModel> fetchPartDetailsList(String userCode,
			RequisitionPartDetailsRequestModel ssRequestModel);

	List<RequisitionVehicleDetailsResponseModel> fetchVehicleDetailsList(String userCode,String chassisNo);

	List<RequisitionTypeListModel> fetchRequisitionTypeList(String userCode, Integer roId, Integer requisitiontype);

	SparePartRequisitionResponseModel PartUpdateRequestedQty(String userCode, PartListRequestModel requestModel);

	
}
