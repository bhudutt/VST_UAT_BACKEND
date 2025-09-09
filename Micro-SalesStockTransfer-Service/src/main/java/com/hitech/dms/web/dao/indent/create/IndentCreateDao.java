package com.hitech.dms.web.dao.indent.create;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.indent.create.request.IndentCreateRequestModel;
import com.hitech.dms.web.model.indent.create.response.IndentCreateResponseModel;

public interface IndentCreateDao {
	public IndentCreateResponseModel createIndent(String authorizationHeader, String userCode,
			IndentCreateRequestModel requestModel, Device device);
}
