package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;

import javax.persistence.Column;

import lombok.Data;

@Data
public class OldChassisModelListResponseModel {

	
	private String itemDescription;
    private String series;
    private String segment;
    private String variant;
}
