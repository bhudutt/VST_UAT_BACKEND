package com.hitech.dms.web.model.jobcard.billing.response;

import java.util.List;

import lombok.Data;

@Data
public class JobCardBillingDetailsCommonResponseModel {

	private JobVechicleDetailsResponseModel jobVechicleDetailsResponseModel;
	private JobCustomerDetailsResponseModel jobCustomerDetailsResponseModel;
	private List<JobPartDetailsResponseModel> jobPartDetailsResponseModel;
	private List<JobLabourDetailsResponseModel> jobLabourDetailsResponseModel;
	private List<JobOutSideLabourDetailsResponseModel> jobOutSideLabourDetailsResponseModel;
}
