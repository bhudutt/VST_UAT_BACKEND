package com.hitech.dms.web.entity.branchTransfer.indent;

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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Table(name = "PA_INDENT_HDR")
@Entity(name = "IndentHdrEntity")
@Getter
@Setter
public class IndentHdrEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private BigInteger id;

	@Column(name = "branch_id")
	private BigInteger branchId;
	
	@OneToMany(mappedBy = "indentHdrEntity", cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
	        orphanRemoval = true)
	@JsonManagedReference
	private List<IndentDtlEntity> indentDtlEntity;
	
	@Column(name = "IndentNumber")
	private String indentNumber;
	
	@Column(name = "IndentDate")
	private Date indentDate;
	
	@Column(name = "IndentOnBranch_id")
	private String indentOnBranchId;

	@Column(name = "IndentRemarks")
	private String indentRemarks;
		
	@Column(name = "IndentBy")
	private Integer indentBy;
	
	@Column(name = "IndentByBranch")
	private Integer indentByBranch;
	
	@Column(name = "Status")
	private String status;
	
	@Column(name = "T_IndentQty")
	private Integer tIndentQty;
	
	@Column(name = "CreatedDate")
	private Date createdDate;
	
	@Column(name = "CreatedBy")
	private BigInteger createdBy;
	
	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "ModifiedBy")
	private BigInteger modifiedBy;
	
	@Column(name = "LastUpdateDate")
	private Date lastUpdateDate;
}

