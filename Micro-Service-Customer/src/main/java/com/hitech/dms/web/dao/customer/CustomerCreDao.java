package com.hitech.dms.web.dao.customer;

import java.math.BigInteger;
import java.util.List;

import org.springframework.mobile.device.Device;

import com.hitech.dms.web.model.customer.create.CustServiceRep;
import com.hitech.dms.web.model.customer.create.CustServiceSaveReq;
import com.hitech.dms.web.model.customer.create.CustomerDetailsResponse;
import com.hitech.dms.web.model.customer.create.CustomerMobileRes;
import com.hitech.dms.web.model.customer.create.LookupResponse;

public interface CustomerCreDao {

	CustomerDetailsResponse fetchCustomerDTLByCustMob(String userCode,
			BigInteger customerId);

	List<CustomerMobileRes> fetchCustomerMobNameDtl(String userCode,
			String searchText);

	List<LookupResponse> fetchCustomerMaster(String lookupTypeCode);

	CustServiceRep createCustomerDetail(String authorizationHeader, String userCode, CustServiceSaveReq requestModel,
			Device device);

	BigInteger alreadyExistMobileNo(String mobileNo);

}
