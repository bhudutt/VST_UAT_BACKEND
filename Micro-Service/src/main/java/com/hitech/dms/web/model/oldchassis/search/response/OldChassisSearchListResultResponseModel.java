package com.hitech.dms.web.model.oldchassis.search.response;

import java.util.List;

import lombok.Data;

@Data
public class OldChassisSearchListResultResponseModel {

		private List<OldChassisSearchListResponseModel> searchResult;
		private Integer recordCount;
	
}
