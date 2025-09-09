package com.hitech.dms.web.controller.registrationsearch.dto;

import lombok.Data;

@Data
public class RegistrationSearchDto {

	public String chassisNo;
	public String registrationNo;
	public Integer page;
    public Integer size;
    public String selectedValue;
}
