package com.hitech.dms.web.model.dealermaster.create.request;

import lombok.Data;

@Data
public class DealerListRequestModel {
 
	private String dealerCode;
	private Integer page;
	private Integer size;
}
