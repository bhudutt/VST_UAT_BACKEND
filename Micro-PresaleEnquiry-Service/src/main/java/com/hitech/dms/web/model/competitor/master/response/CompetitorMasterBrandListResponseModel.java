package com.hitech.dms.web.model.competitor.master.response;

import java.math.BigInteger;

import javax.persistence.Column;

import lombok.Data;

@Data
public class CompetitorMasterBrandListResponseModel {
    
	@Column(name="brand_id")
	private BigInteger brandid;
	@Column(name="BRAND_NAME")
	private String brandName;
	
}
