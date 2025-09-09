package com.hitech.dms.web.service.spare.counterSale;

import java.math.BigInteger;
import java.util.HashMap;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.model.spare.counterSale.CounterSaleResponse;


public interface CounterSaleService {

	ApiResponse<HashMap<BigInteger, String>> searchCustomerDetails(String searchText, String userCode);

	CounterSaleResponse fetchCounterSale(int counterSaleId);

}
