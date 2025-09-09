/**
 * 
 */
package com.hitech.dms.web.model.enquiry.exchange.response;

import java.util.List;

import lombok.Data;

/**
 * @author dinesh.jakhar
 *
 */
@Data
public class ExchangeVehicleSearchMainResponseModel {
	private List<ExchangeVehicleSearchResponseModel> exchangeVehicleSearchList;
	private Integer countRecords;
}
