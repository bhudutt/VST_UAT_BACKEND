package com.hitech.dms.web.model.activity.create.response;

import java.util.List;

import lombok.Data;

@Data
public class ServiceActivitySearchListResultResponse {
	private List<ActivityViewResponseModel> searchResult;
	private Integer recordCount;
}
