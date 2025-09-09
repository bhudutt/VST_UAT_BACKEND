package com.hitech.dms.web.model.spara.customer.order.request;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author Vivek.Gupta
 *
 */
@Data
public class SpareCustomerOrderUpdateRequest {
	
	private BigInteger id;
	
	@JsonProperty(value="customerId")
	private BigInteger customerId;

    private BigInteger partId;
    
    private String partNumber;
	
	private BigInteger orderQty;
	

}
