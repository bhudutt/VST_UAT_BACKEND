package com.hitech.dms.web.model.Installation.create.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstallationCreateResponseModel {

	private Integer installationId;
	private String installationNumber;
	private String msg;
	private Integer statusCode;
}
