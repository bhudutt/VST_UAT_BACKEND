package com.hitech.dms.web.model.spare.sale.aprReturn.request;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AprReturnCancelRequest {

	
	 
	  private Integer aprReturnId;
	  private String remarks;
	  private String status;
	  
	  @JsonProperty(value="partDetails")
      private List<AprReturnCancelDtl> partDetails = new ArrayList<>();
		      
}
