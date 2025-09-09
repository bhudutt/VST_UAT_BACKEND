/**
 * 
 */
package com.hitech.dms.web.dao.machinepo.update;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.machinepo.update.request.MachinePOUpdateRequestModel;
import com.hitech.dms.web.model.machinepo.update.response.MachinePOUpdateResponseModel;

/**
 * @author dinesh.jakhar
 *
 */
public interface MachinePOUpdateDao {
	public MachinePOUpdateResponseModel updateMachinePO(String authorizationHeader, String userCode,
			MachinePOUpdateRequestModel requestModel, Device device);
}
