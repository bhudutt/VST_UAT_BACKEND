package com.hitech.dms.web.spare.party.model.mapping.request;

import java.util.List;

import com.hitech.dms.web.entity.partmaster.create.request.AdminPartMasterEntity;
import com.hitech.dms.web.entity.partmaster.create.request.BranchPartMasterEntity;

import lombok.Data;

@Data
public class PartyMappingResponseModel {
	private int statusCode;
	private String statusMessage;
	private PartTableRequestModel partDetails;
	private PartBranchRequestModel partBranchDetail;
	private List<PartBranchStoreResponseModel> branchStoreList;
	private List<PartStockBinRequestModel> branchStockList;
	
	
	
//	private AdminPartMasterEntity adminPartMasterEntity;
//	private List<BranchPartMasterEntity> BranchPartMasterEntity;
//	private List<PartStockBinRequestModel> branchStockList;
	
	
	
	

}
