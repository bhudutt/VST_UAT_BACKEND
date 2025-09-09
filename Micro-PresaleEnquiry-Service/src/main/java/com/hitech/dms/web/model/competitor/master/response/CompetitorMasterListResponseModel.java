package com.hitech.dms.web.model.competitor.master.response;

import javax.persistence.Column;

import lombok.Data;
@Data
public class CompetitorMasterListResponseModel {
	
	@Column(name="pc_desc")
	private String profitCenter;
	@Column(name="BRAND_NAME")
	private String brandName;
	@Column(name="IsActive")
	private Character active;
	
	private Integer id;
}
