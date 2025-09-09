package com.hitech.dms.web.entity.SparePo;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import lombok.Data;
/**
 * 
 * @author santosh.kumar
 *
 */
@Entity
@Table(name = "PA_PO_HDR", uniqueConstraints = { @UniqueConstraint(columnNames = "po_hdr_id") })
@Data
public class SparePoEntity implements Serializable {
	private static final long serialVersionUID = 6824841296698295172L;
	@Id
	@Column(name = "po_hdr_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger poHdrId;
	@Column(name = "branch_id")
	private BigInteger branchId;
	@Column(name="PONumber")
	private String poNumber;
	@Column(name="POStatus")
	private String poStatus;
	@Column(name="poType")
	private String pOType;
	@Column(name ="CategoryCode")
	private String productCategory;
	@Column(name="PartyCode")
	private String partyCode;
	@Column(name="partyName")
	private String partyName;
	@Column(name="poON")
	private String poON;
	@Column(name="jobcard_no")
	private String jobCardNo;
	@Column(name="Remarks")
	private String remarks;
	@Column(name="TotalItems")
	private int totelItem;
	@Column(name ="TotalQty")
	private int totalQty;
	@Column(name="POReleaseDate")
	private Date  poReleaseDate;
	@Column(name="POCreationDate")
	private Date  POCreationDate;
	@Column(name="CreatedBy")
	private String createdBy;
	@Column(name="rso_no")
	private int rsoNumber;
	@Column(name="totalBaseAmount")
	private BigDecimal totalBaseAmount;
	@Column(name="totalGstAmount")
	private BigDecimal totalGstAmount;
	@Column(name="tcsPercent")
	private String tcsPercent;
	@Column(name="totalPoAmount")
	private BigDecimal totalPoAmount;
	@Column(name="totalTcsAmount")
	private BigDecimal totalTcsAmount;
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	@Column(name="ModifiedBy")
	private BigInteger modifiedBy;
	@Column(name="PO_Cancel_Date")
	private Date poCancelDate;
	@Column(name="PO_Cancel_Resion_id")
	private Integer poCancelResionId;
	@Column(name = "PO_Cancel_Remarks")
	private String poCancelRemarks;
//	@Column(name="draft_mode")
//	@Type(type = "yes_no")
//	private Character draftMode;
	
	


	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sparePOHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<SparePoDetailEntity> sparePODtlList;

}
