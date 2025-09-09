package com.hitech.dms.web.dao.tm.dao;

import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.tm.create.response.EnquiryTmTransferRequestModel;
import com.hitech.dms.web.model.tm.create.response.EnquiryTmTransferResponseModel;
import com.hitech.dms.web.model.tm.create.response.TmListModel;
import com.hitech.dms.web.model.tm.create.response.TmTransferENQRequestModel;
import com.hitech.dms.web.model.tm.create.response.TmTransferENQResponse;

public interface TmDao {

	List<TmListModel> fetchTMList(String userCode, Long dealerOrBranchID);
	public List<TmTransferENQResponse> fetchTmTransferENQList(String userCode, TmTransferENQRequestModel enqRequestModel);
	public EnquiryTmTransferResponseModel transferEnqTm(String authorizationHeader, String usercode,
			EnquiryTmTransferRequestModel enquiryTransferRequestModel, Device device);
}
