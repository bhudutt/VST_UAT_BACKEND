package com.hitech.dms.web.model.enquiry.list.response;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author dinesh.jakhar
 *
 */
@Data
public class EnquiryListResultResponseModel {
	private List<EnquiryListResponseModel> enquiryList;
	private Integer recordCount;
}
