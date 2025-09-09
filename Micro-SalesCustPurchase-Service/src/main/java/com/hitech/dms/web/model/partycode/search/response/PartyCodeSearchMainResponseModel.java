package com.hitech.dms.web.model.partycode.search.response;

import java.util.List;

import lombok.Data;

@Data
public class PartyCodeSearchMainResponseModel {
	private List<PartyCodeSearchResponseModel> searchList;
	private Integer recordCount;
}
