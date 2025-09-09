package com.hitech.dms.web.entity.branchTransfer.issue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity(name="PA_INDENT_ISSUE_DTL")
@Table
@Setter
@Getter
public class IssueDtlEntity {
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;
	
	@Column(name="pa_ind_issue_id")
	private BigInteger paIndIssueId;
	
	@Column(name="part_id")
	private Integer partId;
	
	@Column(name="part_branch_id")
	private Integer partBranchId;
	
	@Column(name="IndentQTY")
	private Integer indentQty;
	
	@Column(name="IssueQTY")
	private Integer issueQty;
	
	@Column(name="BasicUnitPrice")
	private BigDecimal basicUnitPrice;
	
	@Column(name="totalStock")
	private Integer totalStock;
	
	@Column(name="totalValue")
	private BigDecimal totalValue;
	
	
	@Column(name="branch_store_id")
	private Integer branchStoreId;
	
	@Column(name="store_bin_id")
	private BigInteger storeBinId;
	
	@Column(name="CreatedDate")
	private Date createdDate;
	
	@Column(name="CreatedBy")
	private BigInteger createdBy;
	
	@Column(name="ModifiedDate")
	private Date modifiedDate;
	
	@Column(name="ModifiedBy")
	private BigInteger modifiedBy;

}
