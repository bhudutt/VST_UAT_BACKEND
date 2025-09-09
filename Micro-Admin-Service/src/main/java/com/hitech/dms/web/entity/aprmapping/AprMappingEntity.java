package com.hitech.dms.web.entity.aprmapping;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.*;

import lombok.Data;


@Table(name = "STG_DEALER_RETAILER_MAPPING")
@Entity
@Data
public class AprMappingEntity implements Serializable {

	private static final long serialVersionUID = -8843078251082005422L;

	@Id
	@Column(name = "Stg_Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;

	/*
	 * @Column(name = "PC_Id") private Integer pcId;
	 * 
	 * @Column(name = "Dealer_Code") private String dealerCode;
	 * 
	 * @Column(name = "Branch_Code") private String branchCode;
	 */
	
	@Column(name = "KPDCode")
	private String kpdCode;
	
	@Column(name = "KPDName")
	private String kpdName;
	
	@Column(name = "Authorized_Retailer_Name")
	private String autherizeRetailerName;
	
	@Column(name = "APR_Address")
	private String aprAddress;
	
	@Column(name = "APRPhone")
	private BigInteger aprPhone;
	
	@Column(name = "APR_State")
	private String aprState;
	
	@Column(name = "District")
	private String aprDistrictName;
	
	@Column(name = "Taluka")
	private String aprTalukaName;
	
	@Column(name = "City_Village")
	private String aprCityName;
	
	@Column(name = "APR_Pin_Code")
	private String aprPinCode;
	
	@Column(name = "APR_GSTNo")
	private String aprGSTNumber;
	
	@Column(name = "Created_By", updatable = false)
	private String createdBy;

	@Column(name = "Created_Date", updatable = false)
	private Date createdDate;

	
}
