package com.hitech.dms.web.entity.spare.inventorymanagement.stockadjustment;

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

/**
 * @author suraj.gaur
 */
@Getter
@Setter
@Entity
@Table(name = "SP_STOCK_ADJUSTMENT_HDR")
public class SpareStockAdjustmentHdr {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	@Column(name = "BRANCH_ID")
	private BigInteger branchId;
	
	@Column(name = "adjustment_no")
	private String adjustmentNo;
	
	@Column(name = "adjustment_date")
	private Date adjustmentDate;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "adj_done_by_id")
	private BigInteger adjDoneById;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "total_increase_qty")
	private BigDecimal totalIncreaseQty;
	
	@Column(name = "total_increase_value")
	private BigDecimal totalIncreaseValue;
	
	@Column(name = "total_decrease_qty")
	private BigDecimal totalDecreaseQty;
	
	@Column(name = "total_decrease_value")
	private BigDecimal totalDecreaseValue;
	
	@OneToMany(mappedBy = "adjustmentHdr",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<SpareStockAdjustmentDtl> adjustmentDtls;
	
	//added by Mahesh.kumar on 07-05-2024
	@OneToMany(mappedBy = "adjustmentHdr",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<SpareStockAdjustmentDtlTemp> adjustmentDtlsTemp;
	
	@Column(name = "created_by")
	private BigInteger createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "modified_by")
	private BigInteger modifiedBy;
	
	@Column(name = "modified_date")
	private Date modifiedDate;
	
	@Transient
	private boolean dealerFlag;

}
