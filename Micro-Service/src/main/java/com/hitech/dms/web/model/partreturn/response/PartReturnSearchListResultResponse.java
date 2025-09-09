package com.hitech.dms.web.model.partreturn.response;

import java.util.List;

import lombok.Data;

@Data
public class PartReturnSearchListResultResponse {

	private List<PartReturnSearchResponseModel> searchResult;
	private Integer recordCount;
}
