package com.hitech.dms.web.entity.pdi;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.hitech.dms.web.entity.sales.grn.SalesMachineInventoryLedgerEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SV_INWARD_PDI_HDR")
public class PdiEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "InwardNumber", length = 21, unique = true)
	private String pdiNo;// = "PDI/" + ThreadLocalRandom.current().nextInt(1000) + "/" + System.currentTimeMillis();

	@NotNull(message = "pdi date is mandatory")
	@Column(name = "InwardDate", updatable = false)
	private Date pdiDate = new Date();

	@Column(name = "InvoiceNumber")
	private String invoiceNumber;

	@Column(name = "InvoiceDate")
	private Date invoiceDate;

	@Column(updatable = false)
	private Date createdDate = new Date();

	@Column(updatable = false)
	private BigInteger createdBy;

	private BigInteger modifiedBy;

	private Date modifiedDate;

	@Column(name = "ChassisNumber")
	private String chassisNumber;

	@Column(name = "EngineNumber")
	private String engineNumber;

	@Column(name = "MachineModel")
	private String machineModel;

	@Column(name = "LRDate")
	private Date lrDate;

	@Column(name = "LRNumber")
	private String lrNumber;

	@Column(name = "TransporterName")
	private String transporterName;

	@Column(name = "PDIDoneBy")
	private Integer pdiDoneBy;

	@Column(name = "StarterMotorMakeNumber")
	private String starterMotorMakeNumber;

	@Column(name = "AlternatorMakeNumber")
	private String alternatorMakeNumber;

	@Column(name = "FIPMakeNumber")
	private String fipMakeNumber;

	@Column(name = "BatteryMakeNumber")
	private String batteryMakeNumber;

	@Column(name = "FrontTyerMakeRHNumber")
	private String frontTyerMakeRHNumber;

	@Column(name = "FrontTyerMakeLHNumber")
	private String frontTyerMakeLHNumber;

	@Column(name = "RearTyerMakeRHNumber")
	private String rearTyerMakeRHNumber;

	@Column(name = "RearTyerMakeLHNumber")
	private String rearTyerMakeLHNumber;
	
	  @ManyToOne
	  @JoinColumn(name = "MachineInventoryId") private
	  SalesMachineInventoryLedgerEntity machineInventory;
	  
	  @Column(name = "ProfitCenter")
		private String profitCenter;
	  
	  @Column(name = "ItemNumber")
		private String itemNumber;
	  
	  @Column(name = "ItemDescription")
		private String itemDescripton;
	  
	  @Column(name = "Obervation")
		private String obervation;
	  
	  @Column(name = "FileUpload")
		private String fileupload;
	  

	//@OneToMany(fetch = FetchType.EAGER, mappedBy = "pdiEntity", cascade = CascadeType.ALL)
	 @Transient
	private List<PdiDetailsEntity> pdiDeliveryDetailList;

}
