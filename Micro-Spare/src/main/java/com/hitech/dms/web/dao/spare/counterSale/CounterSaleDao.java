package com.hitech.dms.web.dao.spare.counterSale;

import java.math.BigInteger;
import java.util.HashMap;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.spare.counterSale.CounterSaleResponse;

public interface CounterSaleDao {

	ApiResponse<HashMap<BigInteger, String>> searchCustomerDetails(String searchText, String userCode);

	CounterSaleResponse searchCustomerDetails(int counterSaleId);

}
