package com.hitech.dms.web.model.enquiry.export;

import java.util.List;

import com.hitech.dms.web.model.enquiry.list.response.EnquiryListResponseModel;

import lombok.Data;

@Data
public class EnquiryListResultResponseModel {
	
	
	private List<EnquiryListResponseModel> enquiryList;
	private Integer recordCount;

}
