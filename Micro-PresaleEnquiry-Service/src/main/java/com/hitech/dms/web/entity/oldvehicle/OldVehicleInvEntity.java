package com.hitech.dms.web.entity.oldvehicle;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "SA_OLD_VEHICLE_INV")
@Entity
@Data
public class OldVehicleInvEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "old_veh_id")
	private BigInteger oldVehId;

	@Column(name = "Branch_Id")
	private BigInteger branchId;

	@Column(name = "enquiry_id")
	private BigInteger enquiryId;

	@Column(name = "brand_id")
	private BigInteger brandId;

	@Column(name = "BRAND_NAME")
	private String brandName;

	@Column(name = "MODEL_NAME")
	private String modelName;

	@Column(name = "Model_Year")
	private Integer modelYear;

	@Column(name = "Inv_In_Date")
	private Date invInDate;

	@Column(name = "Estimated_Exchange_Price")
	private BigDecimal estimatedExchnagePrice;

	@Column(name = "Status")
	private String status;

	@Column(name = "buyer_id")
	private BigInteger buyerId;

	@Column(name = "buyer_name")
	private String buyerName;
	
	@Column(name = "buyer_contact_no")
	private String buyerContactNo;

	@Column(name = "SaleDate")
	private Date saleDate;

	@Column(name = "SellingPrice")
	private BigDecimal sellingPrice;

	@Column(name = "SaleRemarks")
	private String saleRemarks;

	@Column(name = "CreatedBy", updatable = false)
	private BigInteger createdBy;
	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;
	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
}
