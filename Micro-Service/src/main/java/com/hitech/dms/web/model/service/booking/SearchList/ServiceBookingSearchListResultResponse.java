package com.hitech.dms.web.model.service.booking.SearchList;

import java.util.List;

import lombok.Data;

@Data
public class ServiceBookingSearchListResultResponse {
  
	private List<ServiceBookingSearchListResponseModel> searchResult;
	private Integer recordCount;
}
