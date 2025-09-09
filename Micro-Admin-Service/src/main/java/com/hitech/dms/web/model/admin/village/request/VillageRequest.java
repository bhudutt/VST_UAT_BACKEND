package com.hitech.dms.web.model.admin.village.request;

import lombok.Data;

@Data
public class VillageRequest {
	
	 
	private String country="India";
      private String state;
      private String district;
      private String tehsil;
      private String cityVillage;
      private String localityName;
      private Integer pincode;
     
      
      
      public VillageRequest() {
    	  
      }
      
      
	public VillageRequest(String country, String state, String district, String tehsil, String cityVillage,
			 String localityName, Integer pincode) {
		super();
		this.country = country;
		this.state = state;
		this.district = district;
		this.tehsil = tehsil;
		this.cityVillage = cityVillage;
		this.pincode = pincode;
		this.localityName = localityName;
	}
      
      
    
}
