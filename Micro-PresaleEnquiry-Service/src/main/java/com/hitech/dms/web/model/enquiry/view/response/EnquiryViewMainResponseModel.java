package com.hitech.dms.web.model.enquiry.view.response;

import java.util.List;

import lombok.Data;

@Data
public class EnquiryViewMainResponseModel {
	private EnquiryViewResponseModel enquiryHDRDTL;
	private List<EnquiryCustFleetDTLResponseModel> enquiryCustFleetDTLList;
	private List<EnquiryCropDTLResponseModel> enquiryCropDTLList;
	private List<EnquirySoilTypeResponseModel> enquirySoilTypeList;
	private List<EnquiryViewImgResponseModel> enquiryAttachImgList;
	private List<EnquiryViewExchangeResponseModel> enquiryViewExchangeList;
}
