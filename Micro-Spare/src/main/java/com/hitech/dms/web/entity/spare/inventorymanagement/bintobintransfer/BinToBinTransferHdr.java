package com.hitech.dms.web.entity.spare.inventorymanagement.bintobintransfer;

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

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

/**
 * @author suraj.gaur
 */
@Getter
@Setter
@Entity
@Table(name = "SP_BIN_TO_BIN_TRANSFER_HDR")
public class BinToBinTransferHdr {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private BigInteger id;
	
	@Column(name = "BRANCH_ID")
	private BigInteger branchId;
	
	@Column(name = "issue_no")
	private String issueNo;
	
	@Column(name = "issue_date")
	private Date issueDate;
	
	@Column(name = "receipt_no")
	private String receiptNo;
	
	@Column(name = "transfer_done_by")
	private BigInteger transferDoneBy;
	
	@Column(name = "remarks")
	private String remarks;
	
	@OneToMany(mappedBy = "binTransferHdr",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BinToBinTransferDtl> binToBinTransferDtls;
	
	@Column(name = "created_by")
	private BigInteger createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "modified_by")
	private BigInteger modifiedBy;
	
	@Column(name = "modified_date")
	private Date modifiedDate;

}
