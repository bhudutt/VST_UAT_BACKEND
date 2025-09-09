package com.hitech.dms.web.entity.pdi;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.hitech.dms.web.entity.sales.grn.SalesMachineInventoryLedgerEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SV_PDI_INWARD_DTL")
public class PdiDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	//@JoinColumn(name = "pdi_id", nullable=false)
	//@ManyToOne(fetch = FetchType.LAZY)
	//private PdiEntity pdiEntity;
	@Column(name = "PdiId")
	private Integer pdiEntity;

	@Column(name = "DealerId")
	private Integer dealerId;

	@Column(name = "BranchId")
	private Integer branchId;

	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name = "machine_inventory_id") private
	 * SalesMachineInventoryLedgerEntity machineInventory;
	 */

	/*
	 * @Column(name = "pdi_date") private Date pdiDate;
	 */

	@Column(name = "Remarks")
	private String remarks;

	/*
	 * @NotNull(message = "Draft flag is mandatory")
	 * 
	 * @Column(name = "draft_flag") private Boolean draftFlag = false;
	 */

	@Column(name = "OkFlag")
	private Boolean okFlag = false;

	@Column(name = "CreatedBy", updatable = false)
	private Long createdBy;

	@Column(name = "CreatedDate", updatable = false)
	private Date createdDate = new Date();

	private Long modifiedBy;

	private Date modifiedDate;

	@Column(name = "Attributes")
	private String attributes;

	@Column(name = "CheckPointId")
	private String checkPoint;

	/*
	 * @Column(name = "Observation") private String observation;
	 */
}
