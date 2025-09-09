package com.hitech.dms.web.entity.partrequisition;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Entity
@Table(name = "PA_STOCK_ISSUE_DTL")
@Data
public class SparePartIssueDTLEntity implements Serializable{

	private static final long serialVersionUID = 1867514944801926148L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="issue_dtl_id")
	private BigInteger issueDtlId;
	@Column(name="issue_id")
	private BigInteger issueId;
	@Column(name="sr_no")
	private Integer srNo;
	@Column(name="stock_bin_id")
	private BigInteger stockbinId;
	@Column(name="ref_partBranch_id")
	private BigInteger refpartBranchId;
	@Column(name="OriginalRequestQty")
	private BigDecimal originalrequestQty;
	@Column(name="BeforeIssue_PendingQty")
	private BigDecimal beforeIssuePendingQty;
	@Column(name="IssuedQty")
	private BigDecimal issuedQty;
	@Column(name="IssuedRate")
	private BigDecimal issuedRate;
	@Column(name="IsCancelled")
	private boolean isCancelled;
	@Column(name="Remarks")
	private String remarks;
	@Column(name="CancelDate")
	private Date cancelDate;
	@Column(name="CreatedBy")
	private String createdBy;
	@Column(name="CreatedDate")
	private Date createdDate =new Date();
	@Column(name="ModifiedBy")
	private String modifiedBy;
	@Column(name="ModifiedDate")
	private Date modifiedDate =new Date();
	@Column(name="receivedQty")
	private BigDecimal receivedQty;
	@Column(name="mrp")
	private BigDecimal mrp;
	@Column(name="ndp")
	private BigDecimal ndp;
	@Column(name="Chassi_SerialNo")
	private String chassisSerialNo;
	@Column(name="Version_No")
	private Integer versionNo;
	@Column(name="Requisition_id")
	private Integer requisitionId;	
	
	
	
}
