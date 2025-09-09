package com.hitech.dms.web.model.targetSetting.response;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.hitech.dms.web.entity.targetSetting.TargetSettingDtlEntity;

import lombok.Data;

@Data
public class TargetSettingResponseModel {

	private BigInteger id;
	private String targetNumber;
	private String targetFor;
	private Date targetDate;
	private BigInteger versionNumber;
	private String productCategory;
	private List<TargetSettingDtlResponse> targetSettingDtlResponse;
	private Double totalTarget;
	private String msg;
	private Integer statusCode;
}
