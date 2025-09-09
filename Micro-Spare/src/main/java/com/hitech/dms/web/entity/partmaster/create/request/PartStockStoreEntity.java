package com.hitech.dms.web.entity.partmaster.create.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "PA_STOCK_STORE")
@Data
public class PartStockStoreEntity {

	@Id
	@Column(name = "stock_store_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer stockStoreId;

	@Column(name = "branch_id")
	private Integer branchId;

	@Column(name = "partBranch_id")
	private Integer partBranchId;

	@Column(name = "branch_store_id")
	private Integer branchStoreId;

	@Column(name = "currentStock")
	private BigDecimal currentStock;

	@Column(name = "currentStockAmount")
	private BigDecimal currentStockAmount;

	@Column(name = "CreatedDate")
	private Date createdDate;

	@Column(name = "CreatedBy")
	private String createdBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;
}
