/**
 * 
 */
package com.hitech.dms.web.model.enquiry.exchange.request;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hitech.dms.app.utils.DateHandler;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ExchangeVehicleSearchRequestModel {
	private BigInteger pcID;
	private BigInteger orgHierID;
	private BigInteger dealerID;
	private BigInteger branchID;
	private String enquiryNumber;
	@JsonDeserialize(using = DateHandler.class)
	private Date exchangeFromDate;
	@JsonDeserialize(using = DateHandler.class)
	private Date exchangeToDate;
	private String vehicleStatus;
	private String includeInActive;
	private int page;
	private int size;
}
