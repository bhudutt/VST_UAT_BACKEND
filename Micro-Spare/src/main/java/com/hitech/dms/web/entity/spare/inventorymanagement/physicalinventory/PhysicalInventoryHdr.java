package com.hitech.dms.web.entity.spare.inventorymanagement.physicalinventory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SP_PHYSICAL_INVENTORY_HDR")
public class PhysicalInventoryHdr {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	@Column(name = "BRANCH_ID")
	private BigInteger branchId;
	
	@Column(name = "physical_inv_no")
	private String physicalInvNo;
	
	@Column(name = "physical_inv_date")
	private Date physicalInvDate;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "product_cat_id")
	private BigInteger productCatId;
	
	@Column(name = "done_by")
	private BigInteger doneBy;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "adjustment_id")
	private BigInteger adjustmentId;
	
	@Column(name = "is_zero_qty")
	private BigDecimal isZeroQty;
	
	@Column(name = "total_inc_qty")
	private BigDecimal totalIncQty;
	
	@Column(name = "total_dec_qty")
	private BigDecimal totalDecQty;
	
	@Column(name = "total_inc_value")
	private BigDecimal totalIncValue;
	
	@Column(name = "total_dec_value")
	private BigDecimal totalDecValue;
	
	@Column(name = "created_by")
	private BigInteger createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "modified_by")
	private BigInteger modifiedBy;
	
	@Column(name = "modified_date")
	private Date modifiedDate;
	
	@OneToMany(mappedBy = "physicalInvHdr",cascade = CascadeType.ALL)
    @JsonManagedReference
	private List<PhysicalInventoryDtl> physicalInvDtl;
	
	@Transient
	private boolean dealerFlag;

}
