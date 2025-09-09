package com.hitech.dms.web.entity.customer;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Data;

@Entity
@Table(name = "CM_CUST_DTL")
@Data
public class CustomerDTLEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 9059371254561964567L;
    @Id
    @Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "CUST_HDR_ID")
    private BigInteger customermstIds;
    @Column(name = "DISTRICT_ID")
    private Integer districtId;
    @Column(name = "TEHSIL_ID")
    private Integer tehsilId;
    @Column(name = "VILLAGE_ID")
    private Integer villageId;
    @Column(name = "STATE_ID")
    private Integer stateId;
    @Column(name = "COUNTRY")
    private Integer countryId;
    @Column(name = "PINCODE")
	private Integer pinCode;
}
