package com.hitech.dms.web.entity.master.servicesourcemaster.request;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import lombok.Data;

@Table(name = "SV_LABOUR_MST")
@Entity
@Data
public class ServiceLabourMasterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Labour_Id")
	private BigInteger labourId;
	@Column(name = "Labour_Group_Id")
	private BigInteger labourGroupId;
	@Column(name = "LabourCode", updatable = false)
	private String labourCode;
	private transient Integer labourTechGrpId;
	@Column(name = "LabourDesc")
	private String labourDesc;
	private String displayValue;
	@Column(name = "StandardHrs")
	private Double standardHrs;
	@Column(name = "LabourType")
	private String labourType;
	@Column(name = "IsFromSAP")
	@Type(type = "yes_no")
	private Boolean isFromSAP;
	@Column(name = "IsModifiable")
	@Type(type = "yes_no")
	private Boolean modificationStatus;
	@Column(name = "IsActive")
	@Type(type = "yes_no")
	private Boolean activeStatus;
	@Column(name = "CreatedDate")
	private Date createdDate;
	@Column(name = "CreatedBy")
	private String createdBy;
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	@Column(name = "ModifiedBy")
	private String modifiedBy;
	@Column(name = "model_family_id")
	private Integer modelFamilyId;
	@Column(name = "ComplaintCode")
	private String complaintCode;
	@Column(name = "product_divison_id")
	private Integer productDivisionId;
	@Column(name = "IsLabAmtFixed")
	@Type(type = "yes_no")
	private Boolean amountStatus;
	
	@OneToOne(mappedBy = "serviceLabourMaster")
	private FixedLabourMasterEntity fixedLabourMasterEntity;

	@OneToMany(mappedBy = "labourMaster", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ServiceLabourRateEntity> labourRateList;
	
	@Transient
	private String fromDate;
	@Transient
	private String toDate;
	@Transient
	private Double amount;
	@Transient
	private String modelFamilyDesc;
	@Transient
	private Integer fixedLabourId;
	@Transient
	private List<Integer> modelFamilyIdList;
	@Transient
	private String labourGroup;
	@Transient
	private Double customerPaid = 0.00;
	@Transient
	private String accCtgry;
	@Transient
	private String taxCtgry;
	@Transient
	private Integer branchLbrTaxId;
	@Transient
	private Integer dlrLabourRateId;
	@Column(name = "branch_id")
	private Integer branchId;

	@Column(name = "SANCode", updatable = false)
	private String sanCode;
	@Transient
	private List<LabourSANCodeEntity> labourSANCodeList;
	@Transient
	private Double[] taxRate;
}
