package com.hitech.dms.app.utils;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hitech.dms.web.model.spare.inventorymanagement.stockadjustment.StockAdjustmentResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

	 private int status;
	 private String message;
	 private T result;
	 private Integer count;
	 private Long id;
	 private String token;
	
	 
	 
}
