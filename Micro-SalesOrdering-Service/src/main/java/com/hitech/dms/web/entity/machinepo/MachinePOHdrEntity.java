/**
 * 
 */
package com.hitech.dms.web.entity.machinepo;

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
 * @author dinesh.jakhar
 *
 */
@Entity
@Table(name = "SA_PO_HDR", uniqueConstraints = { @UniqueConstraint(columnNames = "po_hdr_id") })
@Data
public class MachinePOHdrEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6824841296698295172L;
	@Id
	@Column(name = "po_hdr_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger poHdrId;
	
	@Column(name = "dealer_id")
	private BigInteger dealerId;
	
	@Column(name = "pc_id")
	private Integer pcId;
	
	@Column(name = "po_to_type_id")
	private Integer poTypeId;
	
	@Column(name = "po_to_dealer_id")
	private BigInteger poToDealerId;
	
	@Column(name = "prod_div_id")// need to remove
	private Integer productDivisionId;
	
	@Column(name = "po_to_party_name")
	private String poToPartyName;
	
	@Column(name = "po_number", updatable = false)
	private String poNumber;
	
	@Column(name = "draft_mode")
	@Type(type = "yes_no")
	private Boolean draftMode;
	
	@Column(name = "po_date", updatable = false)
	private Date poDate;
	
	@Column(name = "po_release_date", updatable = false)
	private Date poReleasedDate;
	
	@Column(name = "po_status")
	private String poStatus;
	
	@Column(name = "po_plant_id")
	private Integer poPlantId;
	
	@Column(name = "SO_No")
	private String sonNo;
	
	@Column(name = "SO_Date")
	private String soDate;
	
	@Column(name = "total_quantity")
	private Integer totalQty;
	
	@Column(name = "basic_amount")
	private BigDecimal basicAmount;
	
	@Column(name = "total_gst_amount")
	private BigDecimal totalGstAmount;
	
	@Column(name = "tcs_percent")
	private BigDecimal tcsPercent;
	
	@Column(name = "tcs_value")
	private BigDecimal tcsValue;
	
	@Column(name = "total_amount")
	private BigDecimal totalAmount;
	
	@Column(name = "Po_Cancel_Reason_id")
	private BigInteger poCancelReasonId;
	
	@Column(name = "PO_Cancel_Remarks")
	private String poCancelRemarks;
	
	@Column(name = "Remarks")
	private String remarks;
	
	@Column(name = "created_by", updatable = false)
	private BigInteger createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "last_modified_by")
	private BigInteger modifiedBy;

	@Column(name = "last_modified_date")
	private Date modifiedDate;
	
//	@Column(name = "IsMgmtApprovalReq")
//	@Type(type = "yes_no")
//	private Boolean isMgmtApprovalReq;
//	
//	@Column(name = "current_os")
//	private Double currentOs;
//	
//	@Column(name = "os0to30days")
//	private Double os0to30days;
//	
//	@Column(name = "os31to60days")
//	private Double os31to60days;
//	
//	@Column(name = "os61to90days")
//	private Double os61to90days;
//	
//	@Column(name = "os90days")
//	private Double os90days;
//	
//	@Column(name = "total_os")
//	private Double totalOs;
//	
//	@Column(name = "payment_pending")
//	private Double paymentPending;
//	
//	@Column(name = "net_os")
//	private Double netOs;
//	
//	@Column(name = "pending_order")
//	private Double pendingOrder;
//	
//	@Column(name = "order_under_process")
//	private Double orderUnderProcess;
//	
//	@Column(name = "channel_finance_available")
//	private Double channelFinanceAvailable;
//	
//	@Column(name = "exposure_amount")
//	private Double exposureAmount;
//	
//	@Column(name = "total_credit_limit")
//	private Double totalCreditLimit;
//	
//	@Column(name = "available_limit")
//	private Double availableLimit;
//	
//	@Column(name = "cheque_leaf")
//	private String chequeLeaf;
//	
//	@Column(name = "covering_letter")
//	private String coveringLetter;
//	
//	@Column(name = "credit_application")
//	private String creditApplication;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "machinePOHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<MachinePODtlEntity> machinePODtlList;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "machinePOHdr", cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<MachinePOApprovalEntity> machinePOApprovalList;
}
