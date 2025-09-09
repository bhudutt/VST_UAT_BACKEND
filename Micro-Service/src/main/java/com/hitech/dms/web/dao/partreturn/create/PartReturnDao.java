package com.hitech.dms.web.dao.partreturn.create;

import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.partreturn.create.request.PartReturnCreateRequestModel;
import com.hitech.dms.web.model.partreturn.create.request.PartReturnQtyRequestModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnCreateResponseModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnIssueDetailsResponseModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnIssueSearchResponseModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnJobCardDetailsResponseModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnJobCardSearchResponseModel;
import com.hitech.dms.web.model.partreturn.create.response.PartReturnReasionTypeResponseModel;
import com.hitech.dms.web.model.partreturn.response.RestrictSpareReturnListModel;

public interface PartReturnDao {

	PartReturnCreateResponseModel createPartReturn(String userCode, Device device,
			PartReturnCreateRequestModel requestModel);

	List<PartReturnReasionTypeResponseModel> fetchReasonType(String userCode, Device device);

	List<PartReturnJobCardSearchResponseModel> fetchJobCardWorkshopType(String userCode, Device device,Integer searchId, Integer branchId);

//	List<PartReturnJobCardDetailsResponseModel> fetchJobCardWorkshopDetails(String userCode, Device device);

	List<PartReturnIssueSearchResponseModel> fetchIssueSearchList(String userCode, Device device,Integer searchId, Integer branchId);

	List<PartReturnIssueDetailsResponseModel> fetchPartReturnDetailsList(String userCode, Device device, Integer issueId,Integer roId);

	PartReturnCreateResponseModel fetchPartReturnQtyUpdate(String userCode, Device device,
			PartReturnQtyRequestModel partReturnRequestModel);

	List<RestrictSpareReturnListModel> fetchRestrictSpareReturnList(String userCode, Device device, Integer roId,
			String flag);
	

}
