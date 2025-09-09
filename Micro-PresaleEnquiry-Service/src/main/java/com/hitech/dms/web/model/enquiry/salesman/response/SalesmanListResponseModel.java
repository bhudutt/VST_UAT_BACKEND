package com.hitech.dms.web.model.enquiry.salesman.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SalesmanListResponseModel {
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	private List<SalesmanListModel> salesmanList;
}
