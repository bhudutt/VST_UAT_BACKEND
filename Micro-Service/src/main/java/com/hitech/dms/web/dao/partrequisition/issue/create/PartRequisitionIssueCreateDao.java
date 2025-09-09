package com.hitech.dms.web.dao.partrequisition.issue.create;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.partrequisition.issue.create.request.SpareParSearchtJobCardRequestModel;
import com.hitech.dms.web.model.partrequisition.issue.create.request.SparePartIssueUpdateSingleBinRequestModel;
import com.hitech.dms.web.model.partrequisition.issue.create.request.SparePartIssueUpdateStockRequestModel;
import com.hitech.dms.web.model.partrequisition.issue.create.request.SparePartRequisitionIssueRequestModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.PartRequisitionDetailsResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.PartRequisitionNumberResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SpareParSearchtJobCardResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartDetailResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartIssureAvailableStockResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartRequestedByEmployeeListResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartRequisitionIssueResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartRequisitionIssueTypeResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartRequisitionNoSearchResponseModel;
import com.hitech.dms.web.model.partrequisition.issue.create.response.SparePartSearchtDTLJobCardResponseModel;

public interface PartRequisitionIssueCreateDao {

	List<SparePartRequisitionIssueTypeResponseModel> fetchIssueType(String userCode, Device device);

	SparePartRequisitionIssueResponseModel createPartIssue(SparePartRequisitionIssueRequestModel requestModel, String userCode,
			Device device);

	List<PartRequisitionNumberResponseModel> fetchPartRequisitionNo(String userCode, Device device,
			Integer roId);

	List<SparePartRequisitionNoSearchResponseModel> fetchRequisitionByDTLList(String userCode, Device device,
			Integer requisitionId);

	List<SparePartDetailResponseModel> fetchPartDetailsList(String userCode, Device device, Integer requisitionId);

	List<SpareParSearchtJobCardResponseModel> fetchjobCardlist(String userCode, Device device,
			SpareParSearchtJobCardRequestModel requestModel);

	List<SparePartSearchtDTLJobCardResponseModel> fetchjobCardDTLlist(String userCode, Device device, Integer roId, String flag, Integer requisitionId);

	List<PartRequisitionDetailsResponseModel> fetchRequisitionDTLlist(String userCode, Device device, Integer roId,String flag, Integer requisitionId);
			

	List<SparePartIssureAvailableStockResponseModel> fetchAvailableStockList(String userCode, Device device,
			BigInteger requisitionId,String partBranchId);

	SparePartRequisitionIssueResponseModel fetchUpdateStockQuantity(String userCode,
			SparePartIssueUpdateStockRequestModel requestModel);

	SparePartRequisitionIssueResponseModel fetchUpdateSingleBinStockQty(String userCode,
			SparePartIssueUpdateSingleBinRequestModel requestModel);

	List<SparePartRequestedByEmployeeListResponseModel> fetchRequestedByEmpList(String userCode, Device device);
	
	

}
