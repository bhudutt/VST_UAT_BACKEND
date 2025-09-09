package com.hitech.dms.web.model.bajaj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.model.bajaj.dao.StockValidationDao;
import com.hitech.dms.web.model.bajaj.request.StockRequestDTO;
import com.hitech.dms.web.model.bajaj.response.StockResponseDTO;

@Service
public class StockValidationServiceImpl implements StockValidationService {
	
	@Autowired
    private StockValidationDao stockValidationDao;

	/*
	 * @Override public List<StockResponseDTO>
	 * validateAndProcessStock(StockRequestDTO request) { // TODO Auto-generated
	 * method stub return stockValidationDao.validateAndProcessStock(request); }
	 */
	
	@Override
	public StockResponseDTO validateAndProcessStock(StockRequestDTO request) {
		// TODO Auto-generated method stub
		return stockValidationDao.validateAndProcessStock(request);
	}

}
