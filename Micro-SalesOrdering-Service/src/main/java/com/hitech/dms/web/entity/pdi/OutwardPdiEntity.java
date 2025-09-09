package com.hitech.dms.web.entity.pdi;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.hitech.dms.web.entity.sales.grn.SalesMachineInventoryLedgerEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SV_OUTWARD_PDI_HDR")
public class OutwardPdiEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "OutwardNumber", length = 21, unique = true)
	private String outwardPdiNo;// = "PDI/" + ThreadLocalRandom.current().nextInt(1000) + "/" + System.currentTimeMillis();

	@NotNull(message = "pdi date is mandatory")
	@Column(name = "OutwardDate", updatable = false)
	private Date pdiDate = new Date();

	@Column(name = "InvoiceNumber")
	private String invoiceNumber;

	@Column(name = "InvoiceDate")
	private Date invoiceDate;
	
	@Column(name = "GrnNumber")
	private String grnNumber;

	@Column(name = "GrnDate")
	private Date grnDate;

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
	  
	  @Column(name = "ProfitCenter")
		private String profitCenter;
	  
	  
	  @Column(name = "Obervation")
		private String obervation;
	  
	  @Column(name = "FileUpload")
		private String fileupload;
	  

	//@OneToMany(fetch = FetchType.EAGER, mappedBy = "pdiEntity", cascade = CascadeType.ALL)
	 @Transient
	private List<OutwardPdiDetailsEntity> pdiDeliveryDetailList;


}
