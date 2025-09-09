package com.hitech.dms.web.model.competitor.master.response;

import javax.persistence.Column;

import lombok.Data;

@Data
public class CompetitorMasterChangeActiveStatusResponseModel {

	@Column(name="ID")
	private Integer id;
	@Column(name="IsActive")
	private Character active;
	private String msg;
	private int statusCode;
	
}
