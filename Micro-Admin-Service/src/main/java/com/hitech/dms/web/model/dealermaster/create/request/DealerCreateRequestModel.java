package com.hitech.dms.web.model.dealermaster.create.request;

import java.util.List;

import com.hitech.dms.web.entity.dealermaster.DealerMasterEntity;

import lombok.Data;
@Data
public class DealerCreateRequestModel {

	private List<DealerMasterEntity> dealerMasterEntity;
//	private List<String> pcList;
//	private List<String> dealerList;
//	private List<String> divisionCodeList;
//	private List<String> divisionNameList;
	
}
