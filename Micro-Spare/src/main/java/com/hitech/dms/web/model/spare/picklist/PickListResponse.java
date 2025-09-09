package com.hitech.dms.web.model.spare.picklist;


import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class PickListResponse {

	private BigInteger id;
	private String pickListNumber;
	private String pickListDate;
	private String referenceDocument;
	private String status;
	private String poNo;
	private String poDate;
	private String coNo;
	private String coDate;
	private String customerName;
	private String mobileNo;
	private String pinCode;
	private String postOffice;
	private String city;
	private String tehsil;
	private String district;
	private String state;
	private String country;
	private List<PickListPartDetailResponse> partNumberDetailResponse;
}
