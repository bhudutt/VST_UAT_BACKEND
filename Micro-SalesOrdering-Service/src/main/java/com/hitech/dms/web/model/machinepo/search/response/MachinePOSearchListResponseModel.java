package com.hitech.dms.web.model.machinepo.search.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MachinePOSearchListResponseModel {
	private List<MachinePOSearchResponseModel> searchList;
	private Integer recordCount;
}
