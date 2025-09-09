package com.hitech.dms.web.model.enquiry.lostDrop.request;

import lombok.Data;

@Data
public class LostDropResponseModel {
	private Integer reasonId;
	private String reasonCode;
	private String reasonDescription;
}
