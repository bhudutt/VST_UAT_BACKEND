package com.hitech.dms.web.model.bajaj.service;

import java.util.List;

import com.hitech.dms.web.model.bajaj.request.StockRequestDTO;
import com.hitech.dms.web.model.bajaj.response.StockResponseDTO;

public interface StockValidationService {
	//public List<StockResponseDTO> validateAndProcessStock(StockRequestDTO request);
	public StockResponseDTO validateAndProcessStock(StockRequestDTO request);

}
