package com.hitech.dms.web.entity.spare.partymaster.mapping;

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

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "PA_DISTRIBUTOR_RETAILER_MAPPING")
public class PartyDistributorRetailerEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id")
	private BigInteger id;
	
	@Column(name="Party_category_Id")
	private Integer partyCategoryId;
	
	@Column(name="Party_branch_Id")
	private Integer partyBranchId;
	
	@Column(name="Dealer_id")
	private Integer dealerId;
	
	@Column(name = "Is_Active")
	@Type(type = "yes_no")
	private Boolean isActive;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "Created_Date", updatable = false)
	private Date createdDate;

	@Column(name = "Created_By", updatable = false)
	private BigInteger createdBy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Column(name = "Modified_Date")
	private Date modifiedDate;

	@Column(name = "Modified_By")
	private BigInteger modifiedBy;
}
