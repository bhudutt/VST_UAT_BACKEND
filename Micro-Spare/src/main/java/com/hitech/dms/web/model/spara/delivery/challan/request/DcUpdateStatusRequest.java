package com.hitech.dms.web.model.spara.delivery.challan.request;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DcUpdateStatusRequest {
	
	@JsonProperty(value="dchallanId")
	 private BigInteger dchallanId; 
	
//	@JsonProperty(value="customerId")
//	 private BigInteger customerId; 
	 
	@JsonProperty(value="status")
	 private String status;
	 
//	@JsonProperty(value="partId")
//	 private BigInteger partId; 
	 
	@JsonProperty(value="remarks")
	 private String remarks;
	 
	// private BigInteger basicUnitPrice;
	 
	 @JsonProperty(value="sparePartDetails")
	 private List<DcUpdateSparePartRequest> sparePartDetails = new ArrayList<>();
	 
	 

}
