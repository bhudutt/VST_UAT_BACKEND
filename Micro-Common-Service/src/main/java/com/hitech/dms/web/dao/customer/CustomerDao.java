package com.hitech.dms.web.dao.customer;

import java.util.List;

import com.hitech.dms.web.model.customer.request.CustomerDTLByCustIDRequestModel;
import com.hitech.dms.web.model.customer.request.CustomerDTLByMobileRequestModel;
import com.hitech.dms.web.model.customer.response.CustomerDTLByCustIDResponseModel;
import com.hitech.dms.web.model.customer.response.CustomerDTLByMobileResponseModel;

public interface CustomerDao {
	public List<CustomerDTLByMobileResponseModel> fetchCustomerDTLByMobileNo(String userCode, String mobileNumber,
			String isFor, Long dealerId);

	public List<CustomerDTLByMobileResponseModel> fetchCustomerDTLByMobileNo(
			CustomerDTLByMobileRequestModel customerDTLByMobileRequestModel);

	public List<CustomerDTLByCustIDResponseModel> fetchCustomerDTLByCustID(String userCode, Long custID);

	public List<CustomerDTLByCustIDResponseModel> fetchCustomerDTLByCustID(String userCode,
			CustomerDTLByCustIDRequestModel customerDTLByCustIDRequestModel);
}
