package com.hitech.dms.web.entity.partmaster.create.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Table(name = "PA_STOCK_BIN")
@Data
public class PartStockBinEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_bin_id")
	private Integer stockBinId;

	@Column(name = "branch_id")
	private Integer branchId;

	@Column(name = "partBranch_id")
	private Integer partBranchId;

	@Column(name = "stock_store_id")
	private Integer stockStoreId;

	@Column(name = "BinName")
	private String binName;

	@Column(name = "BinType")
	private String binType;

	@Column(name = "currentStock")
	private BigDecimal currentStock;

	@Column(name = "currentStockAmount")
	private BigDecimal currentStockAmount;

	@Column(name = "IsDefault")
	@Type(type = "yes_no")
	private Boolean deafultStatus;

	@Column(name = "IsBinLocked")
	@Type(type = "yes_no")
	private Boolean binBlocked;

	@Column(name = "LastUpdateDate")
	private Date lastUpdatedDate;

	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean activeStatus;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate;

	@Column(name = "CreatedBy", updatable = false)
	private String createdBy;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private String modifiedBy;

	@Transient
	private Boolean partDetailGridSelect;

	@Transient
	private Integer branchStoreId;
}
