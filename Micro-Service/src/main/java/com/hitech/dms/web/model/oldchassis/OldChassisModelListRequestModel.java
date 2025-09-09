package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;

import javax.persistence.Column;

import lombok.Data;

@Data
public class OldChassisModelListRequestModel {

	@Column(name="model_id")
	private BigInteger modelid;
	@Column(name="model_name")
	private String modelName;
	
}
