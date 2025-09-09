package com.hitech.dms.web.model.partrequisition.issue.create.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hitech.dms.web.entity.partrequisition.SparePartIssueDTLEntity;
import com.hitech.dms.web.entity.partrequisition.SparePartIssueEntity;

import lombok.Data;

@Data
public class SparePartRequisitionIssueRequestModel {
	
	private  SparePartIssueEntity sparePartIssueEntity;
	private  List<SparePartIssueDTLEntity> sparePartIssueDTLEntity;
	@JsonProperty("singleBinModel")
	private List<SparePartIssueUpdateSingleBinModel> singleBinModel;

	
}
