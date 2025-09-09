package com.hitech.dms.web.model.bajaj.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StockRequestDTO {
	
	@JsonProperty("dealerCode")
    private String dealerCode;
	
	@JsonProperty("chassisNo")
    private String chassisNo;

    @JsonProperty("vinNo")
    private String vinNo;

    @JsonProperty("modelCode")
    private String modelCode;
    
   // @JsonProperty("custMobNo")
   // private String custMobNo;

    @JsonProperty("distributorCode")
    private String distributorCode;

   // @JsonProperty("Status")
   // private String status; // "0" = Block, "1" = Unblock

}
