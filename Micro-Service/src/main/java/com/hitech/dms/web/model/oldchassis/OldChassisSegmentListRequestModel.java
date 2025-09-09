package com.hitech.dms.web.model.oldchassis;

import java.math.BigInteger;

import javax.persistence.Column;

public class OldChassisSegmentListRequestModel {

	@Column(name="model_id")
	private BigInteger modelid;
	@Column(name="segment_name")
	private String segmentName;
}
