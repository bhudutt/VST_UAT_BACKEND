package com.hitech.dms.web.entity.spare.counterSale;

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

@Data
@Entity
@Table(name = "PA_COUNTER_SALE")
public class CounterSaleEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Counter_Sale_Id")	
	private BigInteger counterSaleId;
		
	@Column(name="Customer_Name")	
	private String customerName;
	
	@Column(name="Mobile_No")	
	private String mobileNo;
	
	@Column(name="Pin_Id")	
	private BigInteger pinId;
	
	@Column(name="CreatedDate")	
	private Date createdDate;
	
	@Column(name="CreatedBy")	
	private String createdBy;
	
	@Column(name="ModifiedDate")	
	private Date modifiedDate;
	
	@Column(name="ModifiedBy")	
	private String modifiedBy;
}
