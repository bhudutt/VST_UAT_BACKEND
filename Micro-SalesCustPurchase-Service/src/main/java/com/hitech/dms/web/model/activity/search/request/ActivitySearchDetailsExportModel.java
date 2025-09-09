package com.hitech.dms.web.model.activity.search.request;

import lombok.Data;

@Data
public class ActivitySearchDetailsExportModel {
   
	public String hoField;
	public String zoneField;
	public String stateField;
	public String territory;
	public String dealerId;
	public String branchId;
	public String fromDate;
	public String toDate;
	public String activityNo;
	public String pcId;
	public String orgHierId;
	public String actualActivityNo;
	public String includeInactive;
	private String size;
	private String page;
	
}
