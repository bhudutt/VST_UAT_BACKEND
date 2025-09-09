package com.hitech.dms.web.entity.spare.inventorymanagement.stockadjustment;

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

/**
 * @author mahesh.kumar
 */
@Getter
@Setter
@Entity
@Table(name = "SP_STOCK_ADJUSTMENT_DTL_TEMP")
public class SpareStockAdjustmentDtlTemp {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	@ManyToOne
    @JsonBackReference
    @JoinColumn(name = "adj_hdr_id", referencedColumnName = "id")
    private SpareStockAdjustmentHdr adjustmentHdr;
	
	@Column(name = "part_no")
	private String partNo;
	
	@Column(name = "part_branch_id")
	private BigInteger partBranchId;
	
	@Column(name = "product_category_id")
	private BigInteger productCategoryId;
	
	@Column(name = "prod_sub_category_id")
	private BigInteger prodSubCategoryId;
	
	@Column(name = "stock_store_id")
	private BigInteger stockStoreId;
	
	@Column(name = "store")
	private String store;
	
	@Column(name = "bin_loc")
	private String binLoc;
	
	@Column(name = "adjustment_type")
	private String adjustmentType;
	
	@Column(name = "net_adjustment_qty")
	private BigDecimal netAdjustmentQty;
	
	@Column(name = "mrp")
	private BigDecimal mrp;
	
	@Column(name = "ndp")
	private BigDecimal ndp;
	
	@Column(name = "value")
	private BigDecimal value;
	
	@Column(name = "reason")
	private String reason;
	
	@Column(name = "branch_id")
	private BigInteger branchId;
	
	@Column(name = "dealer_id")
	private BigInteger dealerId;
	
	@Column(name = "branchStoreId")
	private BigInteger branchStoreId;
}
