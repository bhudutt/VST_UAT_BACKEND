package com.hitech.dms.web.model.spara.customer.order.request;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NonNull;

/**
 * @author Vivek.Gupta
 *
 */
@Data
public class SpareCustomerOrderRequest {
	
	
	@JsonProperty(value="customerId")
	private BigInteger customerId;
	
	@JsonProperty(value="branchId")
//	@NotNull(message = "Branch Id cannot be null")
//	@Range(min = 0)
	private BigInteger branchId;
	
	@JsonProperty(value="customerOrderNumber")
	private String customerOrderNumber;
	
	@JsonProperty(value="partyType")
//	@NotNull(message = "Party Type cannot be null")
	private int partyType;
	
	@JsonProperty(value="address")
	private String address;
	
	@JsonProperty(value="tehsil")
	private int tehsil;
	
	@JsonProperty(value="pincode")
	private String pincode;
	
	@JsonProperty(value="productCategory")
//	@NotNull(message = "Product Category cannot be null")
	private int productCategory;
	
	//@JsonProperty(value="customerOrderDate")
	private Date customerOrderDate;
	
	@JsonProperty(value="partyCode")
//	@NotNull(message = "Party Code cannot be null")
	private int partyCode;
	
	@JsonProperty(value="state")
	private int state;
	
	@JsonProperty(value="cityVillage")
	private int cityVillage;
	
	
	@JsonProperty(value="customerOrderStatus")
	private String customerOrderStatus;
	
	@JsonProperty(value="partyName")
	private String partyName;
	
	@JsonProperty(value="distirct")
	private int distirct;
	
	@JsonProperty(value="postOffice")
	private String postOffice;
	
	@JsonProperty(value="totalQuantity")
	private Long totalQuantity;
	
	@JsonProperty(value="totalPart")
	private Long totalPart;
	
	private String documentType;
	
	private BigInteger invoicedQty;
	
	@JsonProperty(value="sparePartDetails")
	private List<SpareCustomerOrderDetailRequest> sparePartDetails = new ArrayList<>();

}
