package com.hitech.dms.web.model.partrequisition.search.response;

import java.util.List;



import lombok.Data;

@Data
public class PartRequisitionSearchListResultResponse {
 
	private List<PartRequisitionSearchResponseModel> searchResult;
	private Integer recordCount;
}
