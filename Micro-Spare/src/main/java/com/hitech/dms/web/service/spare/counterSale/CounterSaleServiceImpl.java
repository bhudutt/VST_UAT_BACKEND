package com.hitech.dms.web.service.spare.counterSale;

import java.math.BigInteger;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.app.utils.ApiResponse;
import com.hitech.dms.web.dao.spare.counterSale.CounterSaleDao;
import com.hitech.dms.web.model.spare.counterSale.CounterSaleResponse;

@Service
public class CounterSaleServiceImpl implements CounterSaleService {

	@Autowired
	CounterSaleDao counterSaleDao;

	@Override
	public ApiResponse<HashMap<BigInteger, String>> searchCustomerDetails(String searchText, String userCode) {
		return counterSaleDao.searchCustomerDetails(searchText, userCode);
	}

	@Override
	public CounterSaleResponse fetchCounterSale(int counterSaleId) {
		return counterSaleDao.searchCustomerDetails(counterSaleId);
	}

}
