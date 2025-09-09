package com.hitech.dms.web.model.dealermaster.create.response;

import java.util.List;

import lombok.Data;

@Data
public class DealerMasterSearchListResultResponse {

	private List<DealerListModel>searchResult;
	private Integer recordCount;
}
