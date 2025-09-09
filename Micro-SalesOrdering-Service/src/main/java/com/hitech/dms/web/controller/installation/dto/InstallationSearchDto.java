package com.hitech.dms.web.controller.installation.dto;

import lombok.Data;

@Data
public class InstallationSearchDto {

	public String installationNo;
	public String chassisNo;
	public String fromDate;
	public String toDate;
	public Integer page;
    public Integer size;
    public String status;
}
