package com.hitech.dms.web.partsStock.Model;

import java.util.List;

import lombok.Data;

@Data
public class PartBranchDetailStatus {
	
	
	private int partbranchId;
	private List<PartBranchDetailResponse> partBranchDetailResponse;
	private int statusCode;
	private String message;

}
