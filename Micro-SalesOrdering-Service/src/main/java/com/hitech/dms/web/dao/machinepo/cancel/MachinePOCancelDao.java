/**
 * 
 */
package com.hitech.dms.web.dao.machinepo.cancel;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.machinepo.cancel.request.MachinePOCancelRequestModel;
import com.hitech.dms.web.model.machinepo.cancel.response.MachinePOCancelResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
public interface MachinePOCancelDao {
	public MachinePOCancelResponseModel cancelMachinePO(String authorizationHeader, String userCode,
			MachinePOCancelRequestModel requestModel, Device device);
}
