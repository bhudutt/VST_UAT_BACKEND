package com.hitech.dms.web.model.dc.search.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockDetailsMainResponseModel {
	private List<StockDetailsResponseModel> searchList;
	private Integer recordCount;
	private String msg;
	private String status;
}
