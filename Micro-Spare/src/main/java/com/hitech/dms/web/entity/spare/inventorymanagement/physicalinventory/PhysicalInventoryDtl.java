package com.hitech.dms.web.entity.spare.inventorymanagement.physicalinventory;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SP_PHYSICAL_INVENTORY_DTL")
public class PhysicalInventoryDtl {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "physical_inv_hdr_id", referencedColumnName = "id")
	private PhysicalInventoryHdr physicalInvHdr;
	
	@Column(name = "part_branch_id")
	private BigInteger partBranchId;
	
	@Column(name = "product_cat_id")
	private BigInteger productCatId;
	
	@Column(name = "prod_sub_cat_id")
	private BigInteger prodSubCatId;
	
	@Column(name = "from_store_id")
	private BigInteger fromStoreId;
	
	@Column(name = "from_bin_id")
	private BigInteger fromBinId;
	
	@Column(name = "to_store_id")
	private BigInteger toStoreId;
	
	@Column(name = "to_bin_id")
	private BigInteger toBinId;
	
	@Column(name = "stock_qty")
	private BigDecimal stockQty;
	
	@Column(name = "physical_inv")
	private BigDecimal physicalInv;
	
	@Column(name = "ndp")
	private BigDecimal ndp;
	
	@Column(name = "reason")
	private String reason;

}
