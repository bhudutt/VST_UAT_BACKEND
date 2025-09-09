package com.hitech.dms.web.entity.admin.village;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
@Table(name="CM_GEO_INSERT_DATA")
public class AddVillageDetailEntity {
	

	  @Column(name="Id")
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  @Id
	  private Integer id;	
	
	  @Column(name="countryname")	
	  private String country;
	  
	  @Column(name="statename")
      private String state;
	  
	  @Column(name="districtname")
      private String district;
	  
	  @Column(name="tehsilname")
      private String tehsil;
	  
	  @Column(name="city_village_name")
      private String cityVillage;
	  
	  @Column(name="pincode")
      private Integer pincode;
	  
	  @Column(name="locality_name")
	  private String localityName;

}
