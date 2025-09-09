package com.hitech.dms.web.dao.dealermaster.create;

import java.util.List;

import javax.validation.Valid;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.entity.dealermaster.DealerMasterEntity;
import com.hitech.dms.web.model.dealermaster.create.request.DealerCreateRequestModel;
import com.hitech.dms.web.model.dealermaster.create.request.DealerListRequestModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerDivisionCodeListModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerDivisionNameListModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerGroupListModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerListModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerListResponseModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerMasterCreateResponseModel;
import com.hitech.dms.web.model.dealermaster.create.response.DealerMasterSearchListResultResponse;
import com.hitech.dms.web.model.dealermaster.create.response.DealerNameListModel;
import com.hitech.dms.web.model.dealermaster.create.response.ProfitCenterUnderUserResponseModel;

public interface DealerMasterCreateDao {

	DealerMasterCreateResponseModel create(String userCode, DealerCreateRequestModel requestedData, Device device);

	List<DealerListResponseModel> fetchDealerList(String userCode, Device device);

	List<DealerGroupListModel> fetchDealerGroupList(String userCode, Device device);

	List<DealerNameListModel> fetchDealerNameList(String userCode, Device device,String dealerCode);

	List<DealerDivisionCodeListModel> fetchDivisionCodeList(String userCode, Device device);

	List<DealerDivisionNameListModel> fetchDivisionNameList(String userCode, Device device);

	DealerMasterSearchListResultResponse getDealerList(String userCode, Device device, DealerListRequestModel requestModel);

	List<DealerListModel> fetchDealerViewList(String userCode, Device device, Integer batchId);

	List<ProfitCenterUnderUserResponseModel> fetchProfitCenterList(String userCode, Device device);

	DealerMasterCreateResponseModel editDealerMaster(String status, String dealerId, String userCode);

}
