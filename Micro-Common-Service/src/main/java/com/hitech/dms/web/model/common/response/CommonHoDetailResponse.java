package com.hitech.dms.web.model.common.response;

import java.util.List;

import lombok.Data;

@Data
public class CommonHoDetailResponse {
	
	private List<ProfitCenterModel> pcList;
	private List<HoModel> hoModelList;
	private List<OrgHierLevelModel> orgHierLevelList;
	private List<OrgHierDealerList> orgHierDealerList;
	private List<OrgHierBranchList> orgHierBranchList;
	private List<OrgHierZoneModel> zoneList;
	private List<OrgHierStateModel> StateList;
	private List<OrgHierTerritoryModel> territoryList;
	private int statusCode;
	private String statusMessage;
	
	

}
