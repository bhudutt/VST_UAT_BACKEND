package com.hitech.dms.web.model.spare.sale.aprReturn.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CreateAprReturnRequest {
	
	
	  private Integer branchId;
	  private Integer  partyTypeId;
	  private Integer partyId;
	  private String aprReturnDate;
	  private String aprReturedDocNo;
	  private Integer invoiceId;
      private Date invoiceDate;
      private String invoiceNumber;
      private String aprReturnStatus;
      private BigDecimal totalTaxableAmount;  
      private BigDecimal totalGstAmount; 
      private BigDecimal returnAmount; 
      
      private String documentType;
      
      @JsonProperty(value="sparePartDetails")
      private List<AprReturnDtl> sparePartDetails = new ArrayList<>();

      

}
