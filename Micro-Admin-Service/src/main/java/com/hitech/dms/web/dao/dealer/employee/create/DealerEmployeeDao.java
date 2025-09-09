package com.hitech.dms.web.dao.dealer.employee.create;

import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.dealer.employee.create.request.DealerEmployeeHdrRequestModel;
import com.hitech.dms.web.model.dealer.employee.create.request.DealerEmployeeReportingRequestModel;
import com.hitech.dms.web.model.dealer.employee.create.response.DealerEmployeeCreateResponseModel;
import com.hitech.dms.web.model.dealer.employee.create.response.DealerEmployeeReportingResponseModel;

/**
 * @author vinay.gautam
 *
 */
public interface DealerEmployeeDao {
	
	public DealerEmployeeCreateResponseModel createDealerEmployee(String userCode, DealerEmployeeHdrRequestModel requestModel, Device device);
	
	public List<DealerEmployeeReportingResponseModel> dealerReportingEmployee(String userCode, DealerEmployeeReportingRequestModel requestModel, Device device);
	
	

}
