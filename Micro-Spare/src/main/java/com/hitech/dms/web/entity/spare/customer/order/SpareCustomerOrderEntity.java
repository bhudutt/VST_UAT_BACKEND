package com.hitech.dms.web.entity.spare.customer.order;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

/**
 * 
 * @author Vivek.Gupta
 *
 */
@Entity
@Table(name = "PA_Customer_Order_HDR")
@Data
public class SpareCustomerOrderEntity implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Customer_Id")
	private BigInteger customerId;
	
	@Column(name = "Branch_id")
	private Long branchId;
	
	@Column(name = "Customer_Order_Number")
	private String customerOrderNumber;
	
	@Column(name = "Party_Type")
	private Long partyType;
	
	@Column(name = "Address")
	private String address;
	
	@Column(name = "Tehsil")
	private Long tehsil;
	
	@Column(name = "Pincode")
	private Long pincode;
	
	@Column(name = "ProductCategory")
	private Long productCategory;
	
	@Column(name = "Customer_Order_Date")
	private Date customerOrderDate;
	
	@Column(name = "PartyCode")
	private Long partyCode;
	
	@Column(name = "State")
	private String state;
	
	@Column(name = "CityVillage")
	private Long cityVillage;
	
	@Column(name = "TotalPart")
	private Long totalPart;
	
	@Column(name = "Customer_Order_Status")
	private String customerOrderStatus;
	
	@Column(name = "Party_Name")
	private String partyName;
	
	@Column(name = "Distirct")
	private Long distirct;
	
	@Column(name = "PostOffice")
	private String postOffice;
	
	@Column(name = "TotalQuantity")
	private Long totalQuantity;
	
	@Column(name="Dc_Isselect")
	@Type(type = "yes_no")
	private Boolean dcSelect;
	
	@Column(name="Delivery_Challan_Number")
	private String dcNumber;
	
	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="CreatedBy")
	private BigInteger createdBy;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	
	@Column(name="ModifiedBy")
	private BigInteger modifiedBy;
	
	@Column(name="Remarks")
	private String remark;

}
