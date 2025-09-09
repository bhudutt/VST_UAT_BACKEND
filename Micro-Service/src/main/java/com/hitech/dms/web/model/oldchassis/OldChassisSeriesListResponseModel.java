package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;

import javax.persistence.Column;

import lombok.Data;

@Data
public class OldChassisSeriesListResponseModel {
	
	@Column(name="model_id")
	private BigInteger model;
	@Column(name="series_name")
	private String  seriesName;
	private Integer pcId;
}
